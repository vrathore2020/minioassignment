package za.co.practice.minioassignment;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.XmlParserException;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceVersionException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

/**
 * Hello world!
 *
 */
public class MinioAssignment
{
	public static void main(String[] args)
	{
		System.out.println("Hello World!");
		ExchangeCredentials credentials = new WebCredentials("vinay.rathor@corpus.com", "pwd");
		try
		{
			readEmailConversations(credentials);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//    Read email message and attachment collection to get attachment ID
	private static void readEmailConversations(ExchangeCredentials credentials) throws Exception
	{
		System.out.println("readEmailConversations");
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);

		service.setCredentials(credentials);
		service.autodiscoverUrl("vinay.rathor@corpus.com", new RedirectionUrlCallback());

		/*
		 * // Enumerating conversations Collection<Conversation> conversations =
		 * service.findConversation( new ConversationIndexedItemView(10), new
		 * FolderId(WellKnownFolderName.Inbox));
		 * 
		 * for (Conversation conversation : conversations) {
		 * System.out.println("Conversation Id : "+conversation.getId());
		 * System.out.println("Conversation Importance : "+conversation.getImportance())
		 * ; System.out.println("Conversation Has Attachments : "+conversation.
		 * getHasAttachments());
		 * System.out.println("Conversation UnreadCount : "+conversation.getUnreadCount(
		 * ));
		 * 
		 * If email has attachment then get the message details.
		 * getAttachment(conversation.getId()); }
		 */
		ItemView view = new ItemView(10);
		FindItemsResults<Item> findResults = service.findItems(WellKnownFolderName.Inbox, view);

		// MOOOOOOST IMPORTANT: load messages' properties before
		service.loadPropertiesForItems(findResults, PropertySet.FirstClassProperties);

		for (Item item : findResults.getItems())
		{
			// Do something with the item as shown
			System.out.println("id==========" + item.getId());
			System.out.println("sub==========" + item.getSubject());
			if (item instanceof EmailMessage && item.getHasAttachments())
			{
				// If the item is an e-mail message, write the sender's name.
				System.out.println(((EmailMessage) item).getSender().getName());
				getAttachment(service, item.getId());
			}

		}
	}

	/* Get attachment using unique attachment ID */
	private static void getAttachment(ExchangeService service, ItemId itemID) throws Exception
	{
		System.out.println("getAttachment");

		// Bind to an existing message item and retrieve the attachments collection.
		// This method results in an GetItem call to EWS.
		EmailMessage message = EmailMessage.bind(service, itemID, new PropertySet(ItemSchema.Attachments));
		AttachmentCollection attachments = message.getAttachments();
		for (int i = 0; i < attachments.getCount(); i++)
		{
			FileAttachment attachment = (FileAttachment) attachments.getPropertyAtIndex(i);
			
			if(!attachment.getName().contains("image"))
			{
				attachment.load("C:\\temp\\" +attachment.getName());
				sendToMinio(attachment);
			}
		}

	}

	private static void sendToMinio(FileAttachment attachment)
			throws InvalidEndpointException, InvalidPortException, InvalidKeyException, InvalidBucketNameException,
			IllegalArgumentException, NoSuchAlgorithmException, InsufficientDataException, XmlParserException,
			ErrorResponseException, InternalException, InvalidResponseException, IOException, RegionConflictException, ServiceVersionException
	{
		String fileName = attachment.getName();
		System.out.println("attachment file name:" + fileName);

		String accessKey = "minioadmin";
		String secretKey = "minioadmin";

		MinioClient minioClient = new MinioClient("http://127.0.0.1:9000", accessKey, secretKey);

		boolean isExist = minioClient.bucketExists("attachments");
		if (isExist)
		{
			System.out.println("Bucket already exists.");
		}
		else
		{
			minioClient.makeBucket("attachments");
		}
		minioClient.listBuckets().forEach(b -> System.out.println(b.name()));
		minioClient.putObject("attachments", fileName, "C:\\temp\\Transfer to individual capacity.pdf", null);
	}

	
	/*
	 * private static void test(FileAttachment attachment) { //Extract File
	 * Attachments try { FileAttachment fileAttachment = (FileAttachment)
	 * attachment; // if we don't call this, the Content property may be null.
	 * fileAttachment.load();
	 * 
	 * //extract the attachment content, it's not base64 encoded. byte[]
	 * attachmentContent = fileAttachment.getContent();
	 * 
	 * if (attachmentContent != null && attachmentContent.length > 0) {
	 * 
	 * //check the size int attachmentSize = attachmentContent.length;
	 * 
	 * 
	 * fileAttachments.put(UtilConstants.ATTACHMENT_SIZE,
	 * String.valueOf(attachmentSize));
	 * 
	 * //get attachment name String fileName = fileAttachment.getName();
	 * fileAttachments.put(UtilConstants.ATTACHMENT_NAME, fileName);
	 * 
	 * String mimeType = fileAttachment.getContentType();
	 * fileAttachments.put(UtilConstants.ATTACHMENT_MIME_TYPE, mimeType);
	 * 
	 * log.info("File Name: " + fileName + "  File Size: " + attachmentSize);
	 * 
	 * 
	 * if (attachmentContent != null && attachmentContent.length > 0) { //convert
	 * the content to base64 encoded string and add to the collection. String
	 * base64Encoded = UtilFunctions.encodeToBase64(attachmentContent);
	 * fileAttachments.put(UtilConstants.ATTACHMENT_CONTENT, base64Encoded); }
	 * 
	 * 
	 * }
	 */
	static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl
	{
		public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl)
		{
			return redirectionUrl.toLowerCase().startsWith("https://");
		}
	}

}
