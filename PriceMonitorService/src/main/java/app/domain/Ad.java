package app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Data
@Table(value = "ADS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ad {
    @PrimaryKeyColumn(name = "adId",ordinal = 0,type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    private String adId; // done
    @PrimaryKeyColumn(name = "category", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String category; // n/a
    private String title; // done
    private double price; // done
    private String detail_url; // done

    @Override
    public String toString() {
        return "{\"adId\": " + adId + "," +
                "\"category\": " + category + "," +
                "\"title\": " + title + "," +
                "\"price\": " + price + "," +
                "\"detail_url\": " + detail_url + "}";
    }
}
