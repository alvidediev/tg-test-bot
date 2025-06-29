package by.dediev.tg_test_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                ValidationAutoConfiguration.class,
                R2dbcAutoConfiguration.class
        }
)
public class TgTestBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgTestBotApplication.class, args);
    }

}
