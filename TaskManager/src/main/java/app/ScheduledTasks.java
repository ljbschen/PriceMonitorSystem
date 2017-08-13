package app;

import org.springframework.web.client.RestTemplate;

public class ScheduledTasks implements Runnable {
    private RestTemplate restTemplate;
    private String url;

    ScheduledTasks(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public void run() {
        String result = this.restTemplate.getForObject("http://ProductCrawler/api/greeting", String.class);
        System.out.println("Ping serverID " + result);
//        System.out.println("Send REST request to " + url);
    }
}
