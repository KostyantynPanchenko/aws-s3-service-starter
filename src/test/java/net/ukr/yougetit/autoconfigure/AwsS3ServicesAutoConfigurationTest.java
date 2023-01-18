package net.ukr.yougetit.autoconfigure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class AwsS3ServicesAutoConfigurationTest {

  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withConfiguration(UserConfigurations.of(AwsS3ServicesAutoConfiguration.class));

  @Test
  void testAutoconfigurationShouldBeEnabled() {
    runner.withPropertyValues("config.aws.s3.region=us-east-1")
        .run(context -> {
          assertTrue(context.containsBean("s3Client"),
              "Application context SHOULD contain bean 's3Client'");
          assertTrue(context.containsBean("awsS3Service"),
              "Application context SHOULD contain bean 'awsS3Service'");
        });
  }

}
