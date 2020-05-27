package za.co.practice.minioassignment.util;

public class EmailServiceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EmailServiceException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    public EmailServiceException(final Throwable throwable) {
        super(throwable);
    }
}
