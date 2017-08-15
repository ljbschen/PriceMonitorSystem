package app.rest;

import app.domain.Category;
import app.service.ProductCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@RestController
public class ProductCrawlerRestController {
    private final DiscoveryClient discoveryClient;
    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    private ProductCrawlerService productCrawlerService;

    @Autowired
    public ProductCrawlerRestController(DiscoveryClient discoveryClient, ProductCrawlerService productCrawlerService) {
        this.discoveryClient = discoveryClient;
        this.productCrawlerService = productCrawlerService;
    }

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @RequestMapping("/api/greeting")
    public String greeting() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instanceId;
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.POST)
    public String executeTask(@RequestBody Category category) {
        this.productCrawlerService.startTask(category);
        return instanceId;
    }
}
