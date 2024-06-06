package ressourceSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ressourceSharing.model.Ressource;
import java.util.List;
import java.util.Optional;
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    public List<Ressource> findAll();
    public Ressource save(Ressource ressource);
    public void deleteById(Long id);
    public Optional<List<Ressource>> findByNom(String titre);
    public Optional<List<Ressource>> findByDescription(String description);
    public Optional<List<Ressource>> findByType(String type);
    public Optional<List<Ressource>> findByUtilisateursId(Long id);
    public Optional<List<Ressource>> findByUtilisateursPseudo(String pseudo);
}
