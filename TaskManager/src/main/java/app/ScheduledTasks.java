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
//        this.restTemplate.getForObject(url, String.class);
        System.out.println("Send REST request to " + url);
    }
}
