package app;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ad {
    @Id
    private String adId; // done
    private String category; // n/a
    private String title; // done
    private double price; // done
    private double prevPrice;
    private String detail_url; // done

    @Override
    public String toString() {
        return "{\"adId\": \"" + adId + "\"," +
                "\"category\": \"" + category + "\"," +
                "\"title\": \"" + title + "\"," +
                "\"price\": " + price + "," +
                "\"prevPrice\": " + prevPrice + "," +
                "\"detail_url\": \"" + detail_url + "\"}";
    }
}
