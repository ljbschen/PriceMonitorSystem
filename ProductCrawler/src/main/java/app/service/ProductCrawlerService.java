package app.service;

import app.domain.Ad;
import app.domain.AdRepository;
import app.domain.Category;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductCrawlerService {
    private AdRepository adRepository;
    private final static Logger logger = Logger.getLogger(ProductCrawlerService.class);
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; xIntel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

    @Autowired
    public ProductCrawlerService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public void startTask(Category category) {
        try {
            String url = category.getUrl();
            Document doc = Jsoup.connect(url).maxBodySize(0).userAgent(USER_AGENT).timeout(10000).get();
            List<Element> regularList = doc.getElementsByClass("s-result-item  celwidget ");
            regularList.addAll(doc.getElementsByClass("s-result-item s-result-card-for-container s-carded-grid celwidget "));
            System.out.println("list size is " + regularList.size());
            for (Element element : regularList) {
                Ad ad = parseToAd(element);
                ad.setCategory(category.getCategory());
                if (ad.getAdId() != null && !ad.getAdId().equals("") && ad.getPrice() > 0.0) {
                    this.adRepository.save(ad);
                    System.out.println("save into database");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private Ad parseToAd(Element element) {
        Ad ad = new Ad();
        try {
            // get asin
            String asin = "";
            asin = element.attr("data-asin");
            ad.setAdId(asin);
            System.out.println(asin.trim());

            // get title
            String title = "";
            Elements titleEleList = element.getElementsByAttribute("title");
            if (titleEleList.size() > 0) {
                title = titleEleList.get(0).attr("title");
                ad.setTitle(title);
            }

            // get price
            double price = 0;
            Elements priceWholeList = element.getElementsByClass("a-price-whole");
            Elements priceFragList = element.getElementsByClass("a-price-fraction");
            if (priceFragList.size() == 0) {
                priceWholeList = element.getElementsByClass("sx-price-whole");
                priceFragList = element.getElementsByClass("sx-price-fractional");
            }
            if (priceWholeList.size() > 0 && priceFragList.size() > 0) {
                String whole = priceWholeList.get(0).text().replace(".", "").replace(",", "");
                String frag = priceFragList.get(0).text();
                price = Integer.parseInt(whole) + Double.parseDouble(frag) / 100;
                ad.setPrice(price);
            }
            System.out.println(price);

            // get detail_rul
            String detail_url = "";
            Elements detailUrlEleList = element.getElementsByAttribute("href");
            if (detailUrlEleList.size() > 0) {
                detail_url = detailUrlEleList.get(0).attr("href");
                if (!detail_url.contains("https://www.amazon.com/")) detail_url = detailUrlEleList.get(1).attr("href");
                ad.setDetail_url(detail_url);
            }
//            System.out.println(detail_url.trim());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return ad;
    }

    public Ad getAdByAdId(String adId) {
        return adRepository.getAdByAdId(adId);
    }
}
