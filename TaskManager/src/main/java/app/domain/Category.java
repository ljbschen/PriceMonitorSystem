package app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class Category {
    private String category;
    private int interval;
    private String url;

    public Category(String category, int interval, String url) {
        this.category = category;
        this.interval = interval;
        this.url = url;
    }
}
