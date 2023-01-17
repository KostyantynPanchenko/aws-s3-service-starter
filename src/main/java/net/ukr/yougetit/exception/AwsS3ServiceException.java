package net.ukr.yougetit.exception;

public class AwsS3ServiceException extends RuntimeException {

  public AwsS3ServiceException(final String message, final Throwable exception) {
    super(message, exception);
  }
}
