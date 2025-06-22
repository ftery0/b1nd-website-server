package b1nd.b1nd_website_server.global.config.s3;

import b1nd.b1nd_website_server.global.properties.S3Properties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                s3Properties.getCredentials().getAccessKey(),
                s3Properties.getCredentials().getSecretAccessKey());

        return AmazonS3ClientBuilder.standard()
                .withRegion(s3Properties.getRegion().getStaticRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}