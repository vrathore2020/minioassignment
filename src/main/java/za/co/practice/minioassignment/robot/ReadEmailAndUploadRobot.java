package za.co.practice.minioassignment.robot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ReadEmailAndUploadRobot implements IRobotCapabilities {
    private IEmailService emailService;
    private ICloudStorageService cloudStorageService;
    private Logger logger;

    @Override
    public Status perform() {
        final UserCredentials userCreds = new UserCredentials("vinay.rathor@corpus.com", "pwd");
        emailService.connect(userCreds);
        List<EmailAttachment> attachmentsToUpload = emailService.retrieveEmailAttachmentsToProcess();
        final UserCredentials cloudCreds = new UserCredentials("minioadmin", "minioadmin");
        ICloudStorageClient cloudStorageClient = cloudStorageService.initClient(cloudCreds);
        boolean isExist = cloudStorageClient.isBucketExists("attachments");
        if (isExist) {
            logger.error("Bucket already exists.");
        } else {
            cloudStorageClient.createBucket("attachments");
        }
        attachmentsToUpload.forEach(attachment -> cloudStorageClient.uploadObject("attachments", "reademailuploadrobot", attachment.getFileData().getName()));
        return Status.SUCCESS;
    }

    @Override
    public void init() {
        emailService = new EwsService();
        cloudStorageService = new MinioCloudService();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
