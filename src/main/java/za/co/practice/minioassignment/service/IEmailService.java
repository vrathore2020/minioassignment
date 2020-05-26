package za.co.practice.minioassignment.service;

import java.util.List;

import za.co.practice.minioassignment.model.UserCredentials;
import za.co.practice.minioassignment.util.EmailAttachment;

public interface IEmailService {
	
	boolean connect(final UserCredentials creds);
	
	List<EmailAttachment> retrieveEmailAttachmentsToProcess();
}
