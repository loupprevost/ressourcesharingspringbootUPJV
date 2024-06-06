package ressourceSharing.service;
import ressourceSharing.model.Message;

import java.util.List;

public interface MessageService {
    public List<Message> getAllMessage();
    public Message persistMessage(Message message);
    public Message getMessageById(Long id);
    public Message updateMessage(Message message);
    public void deleteMessageById(Long id);
    public void deleteMessage(Message message);
    public List<Message> getMessageByRecepteurIdAndDestinataireId(Long recepteurId, Long destinataireId);
    public List<Message> getMessageByRecepteurIdOrDestinataireId(Long recepteurId, Long destinataireId);

}
