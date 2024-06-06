package ressourceSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ressourceSharing.model.Message;
import ressourceSharing.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAll();
    public Message save(Message message);
    public void deleteById(Long id);
    public Optional<Message> findById(Long id);
    public Optional<Message> findByUtilisateurEmetteur(Utilisateur utilisateur);
    public Optional<Message> findByUtilisateurRecepteur(Utilisateur utilisateur);
    public List<Message> findByUtilisateurRecepteurIdAndUtilisateurEmetteurId(Long utilisateurRecepteurId, Long utilisateurDestinataireId);
    public List<Message> findByUtilisateurRecepteurIdOrUtilisateurEmetteurId(Long utilisateurRecepteurId, Long utilisateurDestinataireId);

}
