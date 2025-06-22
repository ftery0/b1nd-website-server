package b1nd.b1nd_website_server.global.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

    private Credentials credentials;
    private Region region;
    private S3 s3;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretAccessKey;
    }

    @Getter
    @Setter
    public static class Region {
        private String staticRegion;
    }

    @Getter
    @Setter
    public static class S3 {
        private String bucket;
    }
}