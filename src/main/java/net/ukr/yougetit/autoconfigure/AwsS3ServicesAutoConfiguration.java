package net.ukr.yougetit.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConditionalOnProperty(value = "config.aws.s3.enabled", havingValue = "true")
@EnableConfigurationProperties(S3Properties.class)
public class AwsS3ServicesAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(S3Client.class)
  public S3Client s3Client(final S3Properties s3Properties) {
    return S3Client.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .region(Region.of(s3Properties.getRegion()))
        .build();
  }
}
