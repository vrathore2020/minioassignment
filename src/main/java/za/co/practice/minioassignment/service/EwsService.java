package za.co.practice.minioassignment.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
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
import za.co.practice.minioassignment.model.UserCredentials;
import za.co.practice.minioassignment.util.EmailAttachment;
import za.co.practice.minioassignment.util.EmailServiceException;

public class EwsService implements IEmailService {

    private final ExchangeService service;
    private final Logger logger;

    public EwsService() {
        service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public boolean connect(UserCredentials creds) {
        ExchangeCredentials credentials = new WebCredentials(creds.getUsername(), creds.getPassword());
        service.setCredentials(credentials);
        try {
            service.autodiscoverUrl("vinay.rathor@corpus.com", redirectionUrl -> redirectionUrl.toLowerCase().startsWith("https://"));
        } catch (Exception e) {
            throw new EmailServiceException(e);
        }

        return true;
    }

    @Override
    public List<EmailAttachment> retrieveEmailAttachmentsToProcess() {
        ItemView view = new ItemView(10);
        final List<EmailAttachment> results = new ArrayList<>();
        try {
            FindItemsResults<Item> findResults = service.findItems(WellKnownFolderName.Inbox, view);
            // MOOOOOOST IMPORTANT: load messages' properties before
            service.loadPropertiesForItems(findResults, PropertySet.FirstClassProperties);
            for (Item item : findResults.getItems()) {
                // Do something with the item as shown
                logger.debug("id=========={}", item.getId());
                logger.debug("sub=========={}", item.getSubject());
                if (item instanceof EmailMessage && item.getHasAttachments()) {
                    // If the item is an e-mail message, write the sender's name.
                    logger.debug(((EmailMessage) item).getSender().getName());
                    results.addAll(getAttachments(service, item.getId()));
                }
            }
        } catch (Exception ex) {
            throw new EmailServiceException(ex);
        }
        return results;
    }

    private List<EmailAttachment> getAttachments(ExchangeService service, ItemId itemID) throws Exception {
        logger.debug("getAttachments");
        final List<EmailAttachment> list = new ArrayList<>();
        // Bind to an existing message item and retrieve the attachments collection.
        // This method results in an GetItem call to EWS.
        EmailMessage message = EmailMessage.bind(service, itemID, new PropertySet(ItemSchema.Attachments));
        AttachmentCollection attachments = message.getAttachments();
        for (int i = 0; i < attachments.getCount(); i++) {
            FileAttachment attachment = (FileAttachment) attachments.getPropertyAtIndex(i);
            list.add(new EmailAttachment(attachment));
        }
        return list;
    }

}
