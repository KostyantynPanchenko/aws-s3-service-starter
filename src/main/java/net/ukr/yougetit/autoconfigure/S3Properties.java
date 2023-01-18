package net.ukr.yougetit.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AWS S3 properties placeholder.
 */
@ConfigurationProperties(prefix = "config.aws.s3")
public class S3Properties {

  /**
   * AWS region to configure for S3.
   */
  private String region;

  public String getRegion() {
    return region;
  }

  public void setRegion(final String region) {
    this.region = region;
  }

}
