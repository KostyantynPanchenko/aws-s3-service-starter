package net.ukr.yougetit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.ukr.yougetit.exception.AwsS3ServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@RequiredArgsConstructor
public class AwsS3Service {

  private final S3Client s3Client;

  public Path download(final String bucket, final String fileName, final String destFolder) {
    final var getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .build();

    try {
      final byte[] objectAsBytes = s3Client.getObjectAsBytes(getRequest).asByteArray();
      return Files.write(Paths.get(destFolder, fileName), objectAsBytes);
    } catch (final IOException ioException) {
      throw new AwsS3ServiceException("Failed to write the file " + fileName, ioException);
    } catch (final SdkException sdkException) {
      throw new AwsS3ServiceException("Failed to download the file " + fileName, sdkException);
    }
  }

  public String upload(final String bucket, final Path sourcePath) {
    final var putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(sourcePath.getFileName().toString())
        .build();

    try {
      return s3Client.putObject(putRequest, sourcePath).eTag();
    } catch (final SdkException sdkException) {
      throw new AwsS3ServiceException(
          "Failed to upload the file " + sourcePath.getFileName().toString(), sdkException);
    }
  }

  public void delete(final String bucket, final String fileName) {
    final var deleteRequest = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .build();

    try {
      s3Client.deleteObject(deleteRequest);
    } catch (final SdkException sdkException) {
      throw new AwsS3ServiceException("Failed to delete the file " + fileName, sdkException);
    }
  }

  public void copy(final String fromBucket, final String fileName, final String toBucket) {
    final var copyRequest = CopyObjectRequest.builder()
        .sourceKey(fileName)
        .sourceBucket(fromBucket)
        .destinationBucket(toBucket)
        .destinationKey(fileName)
        .copy()
        .build();

    try {
      s3Client.copyObject(copyRequest);
    } catch (final SdkException sdkException) {
      throw new AwsS3ServiceException("Failed to copy the file " + fileName, sdkException);
    }
  }

  public List<String> listFiles(final String bucket) {
    final var listRequest = ListObjectsV2Request.builder().bucket(bucket).build();
    try {
      return s3Client.listObjectsV2(listRequest).contents().stream().map(S3Object::key).toList();
    } catch (final SdkException exc) {
      throw new AwsS3ServiceException("Failed to list the files in the bucket " + bucket, exc);
    }
  }

}
