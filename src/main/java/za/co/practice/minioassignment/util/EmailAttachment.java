package za.co.practice.minioassignment.util;

import microsoft.exchange.webservices.data.property.complex.FileAttachment;

public class EmailAttachment {

	private final FileAttachment fileData;

	public EmailAttachment(FileAttachment fileData) {
		super();
		this.fileData = fileData;
	}

	public FileAttachment getFileData() {
		return fileData;
	}
}
