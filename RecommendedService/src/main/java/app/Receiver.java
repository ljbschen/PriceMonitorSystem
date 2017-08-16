package app;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.PriorityQueue;

@Component
public class Receiver {
    @Autowired
    private AdRepository repository;
    public PriorityQueue<Ad> pq = new PriorityQueue<>();

    public void receiveMessage(byte[] message) {
        try {
            JSONObject object = new JSONObject(new String(message, "UTF-8"));
            Ad ad = new Ad();
            ad.setCategory(object.getString("category"));
            ad.setPrice(object.getDouble("price"));
            ad.setPrevPrice(object.getDouble("prevPrice"));
            ad.setDetail_url(object.getString("detail_url"));
            ad.setTitle(object.getString("title"));
            ad.setAdId(object.getString("adId"));
            repository.save(ad);
            System.out.println("saving Ad:" + ad.getAdId());
//            System.out.println(new String(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
