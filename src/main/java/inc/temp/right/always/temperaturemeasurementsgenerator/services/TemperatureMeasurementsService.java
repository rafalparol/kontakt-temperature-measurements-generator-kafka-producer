package inc.temp.right.always.temperaturemeasurementsgenerator.services;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Log4j2
public class TemperatureMeasurementsService {
    @Value("${main.config.temperature.high}")
    private double high;
    @Value("${main.config.temperature.low}")
    private double low;
    @Value("${main.config.temperature.boost.value}")
    private double boostValue;
    @Value("${main.config.temperature.boost.probability}")
    private double boostProbability;
    @Autowired
    private KafkaMessagesProducerService kafkaMessagesProducerService;
    @Autowired
    private Random random;

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public TemperatureMeasurement generateTemperatureMeasurement(String roomId, String thermometerId) {
        double temperature = low + (high - low) * random.nextDouble();
        temperature = (random.nextDouble() < boostProbability) ? temperature + boostValue : temperature;
        return new TemperatureMeasurement(roomId, thermometerId, this.getCurrentTimeMillis(), temperature);
    }

    public void sendTemperatureMeasurementToKafka(String roomId, String thermometerId) {
        kafkaMessagesProducerService.sendMessageToKafka(this.generateTemperatureMeasurement(roomId, thermometerId));
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setBoostValue(double boostValue) {
        this.boostValue = boostValue;
    }

    public void setBoostProbability(double boostProbability) {
        this.boostProbability = boostProbability;
    }

    public void setKafkaMessagesProducerService(KafkaMessagesProducerService kafkaMessagesProducerService) {
        this.kafkaMessagesProducerService = kafkaMessagesProducerService;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
