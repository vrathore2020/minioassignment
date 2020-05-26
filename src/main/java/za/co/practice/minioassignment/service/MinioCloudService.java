package za.co.practice.minioassignment.service;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import za.co.practice.minioassignment.model.UserCredentials;
import za.co.practice.minioassignment.util.CloudStorageClient;
import za.co.practice.minioassignment.util.CloudStorageException;

public class MinioCloudService implements ICloudStorageService {
	
	public ICloudStorageClient initClient(final UserCredentials creds) {
		try {
			return new CloudStorageClient(new MinioClient("http://127.0.0.1:9000", creds.getUsername(), creds.getPassword()));
		} catch (InvalidEndpointException | InvalidPortException e) {
			throw new CloudStorageException(e);
		}
	}
}
