# Getting Started

## Usage

* Create an artifact `./gradlew publishToMavenLocal`
* Set the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables - [Set up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html)
* Add dependency to your Spring Boot app `implementation 'net.ukr.yougetit:aws-s3-services-starter:1.0.1'`
* Add configuration properties:
```yaml
config:
  aws:
    s3:
      region: us-east-1 # specify your region
```
* Inject bean of type `AwsS3Service`:
```java
@Service
public class SomeService {

  private final AwsS3Service awsS3Service;
  
  public SomeService(final AwsS3Service awsS3Service) {
    this.awsS3Service = awsS3Service;
  }
  
  ...
}
```
* Call the required method
```java
  // download
  final Path downloaded = awsS3Service.download("bucket", "fileName", "destFolder");

  // upload
  final Path sourcePath = Paths.get("somePath");
  final String eTag = awsS3Service.upload("bucket", sourcePath);

  // delete
  awsS3Service.delete("bucket", "fileName");

  // copy file
  awsS3Service.copy("sourceKey", "destinationKey");
```

### Reference Documentation
For further reference, please consider the following sections:

* [Creating Your Own Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.1/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

