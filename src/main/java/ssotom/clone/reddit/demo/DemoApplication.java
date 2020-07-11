package ssotom.clone.reddit.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import ssotom.clone.reddit.demo.configuration.SwaggerConfiguration;

import java.util.Locale;

@EnableAsync
@Import(SwaggerConfiguration.class)
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(DemoApplication.class, args);
	}

}
