package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class QueueSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage hellp = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("My message")
                .build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, hellp);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        Message sendAndReceive = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_AND_RECEIVE_QUEUE, session -> {
            try {
                Message message = session.createTextMessage(objectMapper.writeValueAsString(HelloWorldMessage
                        .builder()
                        .id(UUID.randomUUID())
                        .message("My message")
                        .build()));
                message.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                System.out.println("Sending hello");
                return message;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        });
        if (sendAndReceive != null) {
            System.out.println(sendAndReceive.getBody(String.class));
        }
    }
}
