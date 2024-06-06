package ressourceSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ressourceSharing.model.Utilisateur;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public List<Utilisateur> findAll();
    public Utilisateur save(Utilisateur utilisateur);
    public void deleteById(Long id);
    public Optional<Utilisateur> findByEmail(String email);
    public Optional<Utilisateur> findByPseudo(String pseudo);
}
