package net.ukr.yougetit.autoconfigure;

import net.ukr.yougetit.service.AwsS3Service;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@AutoConfiguration
@EnableConfigurationProperties(S3Properties.class)
public class AwsS3ServicesAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public S3Client s3Client(final S3Properties s3Properties) {
    return S3Client.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .region(Region.of(s3Properties.getRegion()))
        .build();
  }

  @Bean
  public AwsS3Service awsS3Service(final S3Client s3Client) {
    return new AwsS3Service(s3Client);
  }
}
