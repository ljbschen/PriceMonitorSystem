package app.service;

import app.domain.Category;
import org.springframework.web.client.RestTemplate;

public class ScheduledTasks implements Runnable {
    private RestTemplate restTemplate;
    private Category category;
    private static final String TASK_URL = "http://ProductCrawler/api/task";

    ScheduledTasks(RestTemplate restTemplate, Category category) {
        this.restTemplate = restTemplate;
        this.category = category;
    }

    @Override
    public void run() {
        String result = this.restTemplate.postForObject(TASK_URL, category, String.class);
        System.out.println("Ping serverID " + result);
    }
}
