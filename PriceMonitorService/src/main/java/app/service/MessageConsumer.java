package app.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MessageConsumer extends DefaultConsumer {
    private PriceMonitorService priceMonitorService;

    public MessageConsumer(Channel channel, PriceMonitorService priceMonitorService) {
        super(channel);
        this.priceMonitorService = priceMonitorService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
        priceMonitorService.saveAd(message);
    }
}
