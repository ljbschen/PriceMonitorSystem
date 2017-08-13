package app;

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


    private HashMap<String, Integer> scheduleMap;

    @Value("${category.file}")
    private String categoryFile;

    void init() {
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
                scheduleMap.put(object.getString("category"), object.getInt("interval"));
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

    void start() {
        for (String category : scheduleMap.keySet()) {
            this.scheduledExecutorService.scheduleAtFixedRate(new ScheduledTasks(restTemplate, category), 0,
                    (long)scheduleMap.get(category), TimeUnit.MINUTES);
        }
    }
}
