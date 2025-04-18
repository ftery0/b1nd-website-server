package b1nd.b1nd_website_server.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.oauth")
public class OAuthProperties {
    private String clientId;
    private String clientSecret;
}
