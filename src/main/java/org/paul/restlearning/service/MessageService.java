package org.paul.restlearning.service;

import org.paul.restlearning.dao.IMessageDao;
import org.paul.restlearning.model.Message;

import java.util.List;

public class MessageService {
    private final IMessageDao messageDao;
    private final AccountService accountService;

    public MessageService(IMessageDao messageDao, AccountService accountService) {
        this.messageDao = messageDao;
        this.accountService = accountService;
    }

    /**
     * Requirement #3: Create new message
     * Successful iff:
     * - message_text is not blank
     * - message_text <= 255 chars
     * - posted_by refers to a real account
     *
     * @param message incoming message (no message_id)
     * @return persisted message or null if invalid
     */
    public Message createMessage(Message message) {
        if (message == null) return null;

        String text = safeTrim(message.getMessage_text());
        if (text == null || text.isEmpty() || text.length() > 255) return null;

        int postedBy = message.getPosted_by();
        if (!accountService.accountExists(postedBy)) return null;

        message.setMessage_text(text);
        return messageDao.createMessage(message);
    }

    /**
     * Requirement #4: Retrieve all messages
     *
     * @return list of all messages (empty if none)
     */
    public List<Message> getAllMessages() {
        return messageDao.findAllMessages();
    }

    /**
     * Requirement #5: Retrieve message by id
     *
     * @param messageId id
     * @return message or null if not found
     */
    public Message getMessageById(int messageId) {
        return messageDao.findMessageById(messageId);
    }

    /**
     * Requirement #6: Delete message by id
     *
     * @param messageId id
     * @return deleted message or null if not found
     */
    public Message deleteMessage(int messageId) {
        return messageDao.deleteMessageById(messageId);
    }

    /**
     * Requirement #7: Update message text
     * Successful iff:
     * - message exists
     * - new text is not blank
     * - new text <= 255 chars
     *
     * @param messageId id
     * @param newMessageText new text
     * @return updated message or null if invalid
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        String text = safeTrim(newMessageText);
        if (text == null || text.isEmpty() || text.length() > 255) return null;

        return messageDao.updateMessageText(messageId, text);
    }

    /**
     * Requirement #8: Retrieve all messages by user
     *
     * @param accountId user id
     * @return list of messages (empty if none)
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDao.findMessagesByAccountId(accountId);
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
