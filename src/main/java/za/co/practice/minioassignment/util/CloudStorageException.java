package za.co.practice.minioassignment.util;

public class CloudStorageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CloudStorageException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    public CloudStorageException(final Throwable throwable) {
        super(throwable);
    }
}
