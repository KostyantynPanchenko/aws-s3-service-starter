package net.ukr.yougetit.autoconfigure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class AwsS3ServicesAutoConfigurationTest {

  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withConfiguration(UserConfigurations.of(AwsS3ServicesAutoConfiguration.class));

  @Test
  void testAutoconfigurationShouldBeEnabled() {
    runner.withPropertyValues("config.aws.s3.enabled=true", "config.aws.s3.region=us-east-1")
        .run(context -> assertTrue(context.containsBean("s3Client"),
            "Application context SHOULD contain bean 's3Client'"));
  }

  @Test
  void testAutoconfigurationShouldBeDisabled() {
    runner.withPropertyValues("config.aws.s3.enabled=false", "config.aws.s3.region=us-east-1")
        .run(context -> assertFalse(context.containsBean("s3Client"),
            "Application context SHOULD NOT contain bean 's3Client'"));
  }
}
