package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloWorldListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage hwMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message) {
    }

    @JmsListener(destination = JmsConfig.MY_SEND_AND_RECEIVE_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage hwMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message) throws JMSException {
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("My message")
                .build());
    }
}
