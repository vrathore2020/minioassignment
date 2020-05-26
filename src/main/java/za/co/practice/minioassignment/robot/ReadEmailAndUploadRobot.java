package za.co.practice.minioassignment.robot;

import java.util.List;

import za.co.practice.minioassignment.model.Status;
import za.co.practice.minioassignment.model.UserCredentials;
import za.co.practice.minioassignment.service.EwsService;
import za.co.practice.minioassignment.service.ICloudStorageClient;
import za.co.practice.minioassignment.service.ICloudStorageService;
import za.co.practice.minioassignment.service.IEmailService;
import za.co.practice.minioassignment.service.MinioCloudService;
import za.co.practice.minioassignment.util.EmailAttachment;

/**
 * Hello world!
 *
 */
public class ReadEmailAndUploadRobot implements IRobotCapabilities
{
	private IEmailService emailService;
	private ICloudStorageService cloudStorageService;
	
	@Override
	public Status perform() {
		final UserCredentials userCreds = new UserCredentials("vinay.rathor@corpus.com", "pwd");
		emailService.connect(userCreds);
		List<EmailAttachment> attachmentsToUpload = emailService.retrieveEmailAttachmentsToProcess();
		final UserCredentials cloudCreds = new UserCredentials("minioadmin", "minioadmin");
		ICloudStorageClient cloudStorageClient = cloudStorageService.initClient(cloudCreds);
		boolean isExist = cloudStorageClient.isBucketExists("attachments");
		if (isExist)
		{
			System.out.println("Bucket already exists.");
		}
		else
		{
			cloudStorageClient.createBucket("attachments");
		}
		attachmentsToUpload.forEach(attachment -> cloudStorageClient.uploadObject("attachments", "reademailuploadrobot", attachment.getFileData().getName()));
		return Status.SUCCESS;
	}

	@Override
	public void init() {
		emailService = new EwsService();
		cloudStorageService = new MinioCloudService();
	}

}
