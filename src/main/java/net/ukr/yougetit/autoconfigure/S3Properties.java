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

  /**
   * Specifies whether AWS S3 services starter should be enabled.
   */
  private boolean enabled;

  public String getRegion() {
    return region;
  }

  public void setRegion(final String region) {
    this.region = region;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

}
