package app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Data
@Table(value = "ADS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ad {
    @PrimaryKeyColumn(name = "adId",ordinal = 0,type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    public String adId; // done
    @PrimaryKeyColumn(name = "category", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    public String category; // n/a
    public String title; // done
    public double price; // done
    public String detail_url; // done
}
