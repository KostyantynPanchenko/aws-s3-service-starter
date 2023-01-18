package net.ukr.yougetit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.ukr.yougetit.exception.AwsS3ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

@ExtendWith(MockitoExtension.class)
class AwsS3ServiceTest {

  private AwsS3Service sut;
  @Mock
  private S3Client s3Client;

  @BeforeEach
  void setUp() {
    sut = new AwsS3Service(s3Client);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testDownloadShouldPass(@TempDir Path destFolder) {
    ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes
        .fromByteArray(GetObjectResponse.builder().build(), "data".getBytes());
    given(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).willReturn(responseBytes);

    Path file = sut.download("bucket", "fileName", destFolder.toString());

    assertThat(file).exists();
  }

  @Test
  void testDownloadShouldFailToWriteTheFile() {
    ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes
        .fromByteArray(GetObjectResponse.builder().build(), "data".getBytes());
    given(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).willReturn(responseBytes);

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.download("bucket", "fileName", "thisShouldCauseTheException"));

    assertThat(thrown.getMessage()).isEqualTo("Failed to write the file fileName");
    assertThat(thrown.getCause()).isInstanceOf(IOException.class);
  }

  @Test
  void testDownloadShouldFailBecauseOfSdkException() {
    given(s3Client.getObjectAsBytes(any(GetObjectRequest.class)))
        .willThrow(SdkClientException.create("Boom"));

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.download("bucket", "fileName", "destFolder"));

    assertThat(thrown.getMessage()).isEqualTo("Failed to download the file fileName");
    assertThat(thrown.getCause()).isInstanceOf(SdkClientException.class);
  }

  @Test
  void testUploadShouldPass() {
    PutObjectResponse response = PutObjectResponse.builder().eTag("eTag").build();
    Path sourcePath = Paths.get("sourcePath");
    given(s3Client.putObject(any(PutObjectRequest.class), eq(sourcePath))).willReturn(response);

    String result = sut.upload("bucket", sourcePath);
    assertThat(result).isEqualTo("eTag");
  }

  @Test
  void testUploadShouldFail() {
    Path sourcePath = Paths.get("sourcePath");
    given(s3Client.putObject(any(PutObjectRequest.class), eq(sourcePath)))
        .willThrow(SdkClientException.create("Boom"));

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.upload("bucket", sourcePath));

    assertThat(thrown.getMessage()).isEqualTo("Failed to upload the file sourcePath");
    assertThat(thrown.getCause()).isInstanceOf(SdkClientException.class);
  }

  @Test
  void testDeleteShouldPass() {
    given(s3Client.deleteObject(any(DeleteObjectRequest.class)))
        .willReturn(DeleteObjectResponse.builder().build());
    sut.delete("bucket", "fileName");
  }

  @Test
  void testDeleteShouldFail() {
    doThrow(SdkClientException.create("Boom"))
        .when(s3Client).deleteObject(any(DeleteObjectRequest.class));

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.delete("bucket", "fileName"));

    assertThat(thrown.getMessage()).isEqualTo("Failed to delete the file fileName");
    assertThat(thrown.getCause()).isInstanceOf(SdkClientException.class);
  }

  @Test
  void testCopyShouldPass() {
    given(s3Client.copyObject(any(CopyObjectRequest.class)))
        .willReturn(CopyObjectResponse.builder().build());
    sut.copy("source", "copy");
  }

  @Test
  void testCopyShouldFail() {
    given(s3Client.copyObject(any(CopyObjectRequest.class)))
        .willThrow(SdkClientException.create("Boom"));

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.copy("source", "copy"));

    assertThat(thrown.getMessage()).isEqualTo("Failed to copy the file source");
  }

  @Test
  void testListFilesShouldPass() {
    S3Object s3Object = S3Object.builder().key("theKey").build();
    given(s3Client.listObjectsV2(any(ListObjectsV2Request.class)))
        .willReturn(ListObjectsV2Response.builder().contents(List.of(s3Object)).build());

    List<String> files = sut.listFiles("bucket");
    assertThat(files).hasSize(1).containsOnly("theKey");
  }

  @Test
  void testListFilesShouldFail() {
    given(s3Client.listObjectsV2(any(ListObjectsV2Request.class)))
        .willThrow(SdkClientException.create("Boom"));

    AwsS3ServiceException thrown = assertThrows(AwsS3ServiceException.class, () ->
        sut.listFiles("bucket"));

    assertThat(thrown.getMessage()).isEqualTo("Failed to list the files in the bucket bucket");
  }
}
