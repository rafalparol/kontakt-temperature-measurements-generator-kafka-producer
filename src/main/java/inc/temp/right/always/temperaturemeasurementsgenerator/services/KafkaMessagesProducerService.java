package inc.temp.right.always.temperaturemeasurementsgenerator.services;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaMessagesProducerService {
    @Value("${main.config.kafka.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, TemperatureMeasurement> kafkaTemplate;

    public void sendMessageToKafka(TemperatureMeasurement temperatureMeasurement) {
        log.info(String.format("Message sent to Kafka, key: %s, value: %s.", temperatureMeasurement.getThermometerId(), temperatureMeasurement.toString()));
        kafkaTemplate.send(topicName, temperatureMeasurement.getThermometerId(), temperatureMeasurement);
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setKafkaTemplate(KafkaTemplate<String, TemperatureMeasurement> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
