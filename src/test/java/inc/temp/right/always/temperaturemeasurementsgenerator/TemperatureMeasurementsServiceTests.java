package inc.temp.right.always.temperaturemeasurementsgenerator;

import inc.temp.right.always.temperaturemeasurementsgenerator.services.KafkaMessagesProducerService;
import inc.temp.right.always.temperaturemeasurementsgenerator.services.TemperatureMeasurementsService;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TemperatureMeasurementsServiceTests {
    @Test
    public void sendTemperatureMeasurementToKafka_Test() {
        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:00").toInstant().toEpochMilli(), 31.0);

        TemperatureMeasurementsService temperatureMeasurementsService = spy(TemperatureMeasurementsService.class);

        KafkaMessagesProducerService kafkaMessagesProducerService = mock(KafkaMessagesProducerService.class);
        doNothing().when(kafkaMessagesProducerService).sendMessageToKafka(temperatureMeasurement);
        Random random = mock(Random.class);

        temperatureMeasurementsService.setKafkaMessagesProducerService(kafkaMessagesProducerService);
        temperatureMeasurementsService.setRandom(random);
        temperatureMeasurementsService.setHigh(30.0);
        temperatureMeasurementsService.setLow(20.0);
        temperatureMeasurementsService.setBoostProbability(0.1);
        temperatureMeasurementsService.setBoostValue(10.0);

        when(temperatureMeasurementsService.generateTemperatureMeasurement("room-1", "thermometer-1")).thenReturn(temperatureMeasurement);

        temperatureMeasurementsService.sendTemperatureMeasurementToKafka("room-1", "thermometer-1");

        verify(kafkaMessagesProducerService, times(1)).sendMessageToKafka(temperatureMeasurement);
    }

    @Test
    public void generateTemperatureMeasurement_Test() {
        TemperatureMeasurement expectedResult = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:00").toInstant().toEpochMilli(), 35.0);

        TemperatureMeasurementsService temperatureMeasurementsService = spy(TemperatureMeasurementsService.class);

        KafkaMessagesProducerService kafkaMessagesProducerService = mock(KafkaMessagesProducerService.class);
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.50).thenReturn(0.01);

        temperatureMeasurementsService.setKafkaMessagesProducerService(kafkaMessagesProducerService);
        temperatureMeasurementsService.setRandom(random);
        temperatureMeasurementsService.setHigh(30.0);
        temperatureMeasurementsService.setLow(20.0);
        temperatureMeasurementsService.setBoostProbability(0.1);
        temperatureMeasurementsService.setBoostValue(10.0);

        when(temperatureMeasurementsService.getCurrentTimeMillis()).thenReturn(Timestamp.valueOf("2024-02-01 00:00:00").toInstant().toEpochMilli());

        TemperatureMeasurement result = temperatureMeasurementsService.generateTemperatureMeasurement("room-1", "thermometer-1");

        verify(random, times(2)).nextDouble();
        verify(temperatureMeasurementsService, times(1)).getCurrentTimeMillis();

        assertEquals(expectedResult, result, String.format("Expected result: %s and returned result: %s are not the same when generating temperature measurements.", expectedResult, result));
    }
}