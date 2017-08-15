package app.service;

import app.domain.Category;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TaskManagerService {
    private final RestTemplate restTemplate;
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    public TaskManagerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    private HashMap<String, Category> scheduleMap;

    @Value("${category.file}")
    private String categoryFile;

    public void init() {
        scheduleMap = new HashMap<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(categoryFile);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                System.out.println("category is " + object.getString("category"));
                String category = object.getString("category");
                int interval = object.getInt("interval");
                String url = object.getString("url");
                scheduleMap.put(object.getString("category"), new Category(category, interval, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fr != null) try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.scheduledExecutorService = Executors.newScheduledThreadPool(scheduleMap.keySet().size());
    }

    public void start() {
        for (String category : scheduleMap.keySet()) {
            this.scheduledExecutorService.scheduleAtFixedRate(new ScheduledTasks(restTemplate, scheduleMap.get(category)), 0,
                    (long)scheduleMap.get(category).getInterval(), TimeUnit.MINUTES);
        }
    }
}
