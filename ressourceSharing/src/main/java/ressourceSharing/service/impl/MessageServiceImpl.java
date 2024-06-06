package ressourceSharing.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ressourceSharing.model.Message;
import ressourceSharing.repository.MessageRepository;
import ressourceSharing.service.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message persistMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Message not found"));
    }

    @Override
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Message> getMessageByRecepteurIdAndDestinataireId(Long recepteurId, Long destinataireId) {
        return messageRepository.findByUtilisateurRecepteurIdAndUtilisateurEmetteurId(recepteurId, destinataireId);
    }
    @Override
    public List<Message> getMessageByRecepteurIdOrDestinataireId(Long recepteurId, Long destinataireId) {
        return messageRepository.findByUtilisateurRecepteurIdOrUtilisateurEmetteurId(recepteurId, destinataireId);
    }
}
