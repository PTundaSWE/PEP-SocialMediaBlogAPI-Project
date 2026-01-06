package org.paul.restlearning.dao;

import org.paul.restlearning.model.Message;
import java.util.List;

public interface IMessageDao {
    Message createMessage(Message message);
    List<Message> findAllMessages();
    Message findMessageById(int messageId);
    Message deleteMessageById(int messageId);
    Message updateMessageText(int messageId, String newMessageText);
    List<Message> findMessagesByAccountId(int accountId);
}
