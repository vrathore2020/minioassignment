package za.co.practice.minioassignment.service;

import za.co.practice.minioassignment.model.UserCredentials;

public interface ICloudStorageService {

    ICloudStorageClient initClient(final UserCredentials creds);
}
