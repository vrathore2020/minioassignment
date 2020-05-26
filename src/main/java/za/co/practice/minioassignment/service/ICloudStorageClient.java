package za.co.practice.minioassignment.service;

public interface ICloudStorageClient {

	boolean createBucket(final String name);
	
	boolean isBucketExists(final String name);
	
	boolean uploadObject(final String bucketName, final String folderName, final String fileName);
}