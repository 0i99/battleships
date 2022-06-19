package pl.battleships.javaspringship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.battleships.javaspringship.config.GameConfig;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableScheduling
@Import({
        GameConfig.class
})
public class JavaSpringShipApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringShipApplication.class, args);
    }

}
