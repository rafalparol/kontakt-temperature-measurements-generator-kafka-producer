package inc.temp.right.always.temperaturemeasurementsgenerator;

import inc.temp.right.always.temperaturemeasurementsgenerator.services.TemperatureMeasurementsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Log4j2
@Configuration
public class TemperatureMeasurementsGeneratorApplication implements CommandLineRunner {
	@Value("${main.config.thermometers}")
	private int thermometersCount;
	@Value("${main.config.rooms}")
	private int roomsCount;
	@Value("${main.config.interval}")
	private int timeInterval;
	@Autowired
	private TemperatureMeasurementsService temperatureMeasurementsService;

	public static void main(String[] args) {
		SpringApplication.run(TemperatureMeasurementsGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Temperature measurements generator started...");

		while (true) {
			for (int i = 0; i < thermometersCount; i++) {
				temperatureMeasurementsService.sendTemperatureMeasurementToKafka("room-" + ((i % roomsCount) + 1), "thermometer-" + (i + 1));
			}
			sleep(timeInterval);
		}
	}
}
