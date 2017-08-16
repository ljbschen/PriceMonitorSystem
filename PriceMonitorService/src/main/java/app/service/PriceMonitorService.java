package app.service;

import app.domain.Ad;
import app.domain.AdRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class PriceMonitorService {
    private final AdRepository adRepository;
    private static final String QUEUE_NAME = "deals";

    @Autowired
    public PriceMonitorService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    void saveAd(String message) {
        Ad ad = parseAd(message);
        Ad prevAd = adRepository.getAdByAdId(ad.getAdId());
        if (prevAd == null) ad.setPrice(99999999.0);
        else if (prevAd.getPrice() > ad.getPrice()) {
            // push to queue
            System.out.println("push to queue");
            ad.setPrevPrice(prevAd.getPrice());
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = null;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, ad.toString().getBytes());
                System.out.println(" [x] Sent '" + ad.toString() + "'");

                channel.close();
                connection.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
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
