package cz.adamzrcek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class })
@EnableCaching
public class RememberHunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RememberHunApplication.class, args);
    }

}
