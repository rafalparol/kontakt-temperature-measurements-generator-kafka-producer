package inc.temp.right.always.temperaturemeasurementsgenerator;

import inc.temp.right.always.temperaturemeasurementsgenerator.services.KafkaMessagesProducerService;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.sql.Timestamp;

import static org.mockito.Mockito.*;

public class KafkaMessagesProducerServiceTests {
    @Test
    public void sendMessageToKafka_Test() {
        KafkaMessagesProducerService kafkaMessagesProducerService = new KafkaMessagesProducerService();

        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:00").toInstant().toEpochMilli(), 31.0);

        KafkaTemplate<String, TemperatureMeasurement> kafkaTemplate = mock(KafkaTemplate.class);
        when(kafkaTemplate.send("topic-1", temperatureMeasurement.getThermometerId(), temperatureMeasurement)).thenReturn(null);

        kafkaMessagesProducerService.setKafkaTemplate(kafkaTemplate);
        kafkaMessagesProducerService.setTopicName("topic-1");

        kafkaMessagesProducerService.sendMessageToKafka(temperatureMeasurement);

        verify(kafkaTemplate, times(1)).send("topic-1", temperatureMeasurement.getThermometerId(), temperatureMeasurement);
    }
}