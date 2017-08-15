package app.service;

import app.domain.Ad;
import app.domain.AdRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceMonitorService {
    private final AdRepository adRepository;

    @Autowired
    public PriceMonitorService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public void saveAd(String message) {
        Ad ad = parseAd(message);
        Ad prevAd = adRepository.getAdByAdId(ad.getAdId());
        if (prevAd != null && prevAd.getPrice() > ad.getPrice()) {
            // push to queue
            System.out.println("push to queue");

        }
        adRepository.save(ad);
    }

    private Ad parseAd(String message) {
        JSONObject object = new JSONObject(message);
        Ad ad = new Ad();
        ad.setDetail_url(object.getString("detail_url"));
        ad.setCategory(object.getString("category"));
        ad.setPrice(object.getDouble("price"));
        ad.setTitle(object.getString("title"));
        ad.setAdId(object.getString("adId"));
        return ad;
    }
}
