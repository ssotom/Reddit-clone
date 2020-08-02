package ssotom.clone.reddit.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="app")
public class AppConfiguration {

    private long jwtExpirationTime = 43200000;
    private boolean emailVerificationRequired = false;
    private String emailVerificationUrl = "";

}
