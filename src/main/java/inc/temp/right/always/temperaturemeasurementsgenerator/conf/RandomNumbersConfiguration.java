package inc.temp.right.always.temperaturemeasurementsgenerator.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomNumbersConfiguration {
    @Value("${main.config.seed}")
    private long seed;

    @Bean
    public Random random() {
        return new Random(seed);
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
}
