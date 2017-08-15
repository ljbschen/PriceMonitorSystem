package app;

import app.service.MessageConsumer;
import app.service.PriceMonitorService;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableDiscoveryClient
public class PriceMonitorApplication {
    private static final String EXCHANGE_NAME = "ads";
    private static PriceMonitorService priceMonitorService;

    @Autowired
    public PriceMonitorApplication(PriceMonitorService priceMonitorService) {
        PriceMonitorApplication.priceMonitorService = priceMonitorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PriceMonitorApplication.class, args);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = channel.queueDeclare().getQueue();

            if (args.length < 1){
                System.err.println("Please specify the queue you want to listen to.");
                System.exit(1);
            }

            for(String severity : args){
                System.out.println("topic name is " + severity);
                channel.queueBind(queueName, EXCHANGE_NAME, severity);
            }

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new MessageConsumer(channel, priceMonitorService);
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
