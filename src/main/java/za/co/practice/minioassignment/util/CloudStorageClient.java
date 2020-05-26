package za.co.practice.minioassignment.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.XmlParserException;
import za.co.practice.minioassignment.service.ICloudStorageClient;

public class CloudStorageClient implements ICloudStorageClient {

	private final MinioClient innerClient;

	public CloudStorageClient(MinioClient innerClient) {
		super();
		this.innerClient = innerClient;
	}

	public MinioClient getInnerClient() {
		return innerClient;
	}

	@Override
	public boolean createBucket(String name) {
		try {
			innerClient.makeBucket(name);
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | RegionConflictException
				| NoSuchAlgorithmException | InsufficientDataException | XmlParserException | ErrorResponseException
				| InternalException | InvalidResponseException | IOException e) {
			throw new CloudStorageException(e);
		}
		return true;
	}

	@Override
	public boolean isBucketExists(String name) {
		boolean bucketExist = false;
		try {
			bucketExist = innerClient.bucketExists(name);
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | NoSuchAlgorithmException
				| InsufficientDataException | XmlParserException | ErrorResponseException | InternalException
				| InvalidResponseException | IOException e) {
			throw new CloudStorageException(e);
		}
		return bucketExist;
	}

	@Override
	public boolean uploadObject(String bucketName, String folderName, String fileName) {
		try {
			innerClient.putObject(bucketName, folderName, fileName, null);
		} catch (InvalidKeyException | InvalidBucketNameException | IllegalArgumentException | NoSuchAlgorithmException | InsufficientDataException | XmlParserException | ErrorResponseException
				| InternalException | InvalidResponseException | IOException e) {
			throw new CloudStorageException(e);
		}
		return true;
	}
}
