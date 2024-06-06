package ressourceSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ressourceSharing.model.UtilisateurRessource;
import ressourceSharing.service.UtilisateurRessourceService;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRessourceRepository extends JpaRepository<UtilisateurRessource, Long> {
    public List<UtilisateurRessource> findAll();
    public UtilisateurRessource save(UtilisateurRessource utilisateurRessource);
    public void deleteById(Long id);
    public Optional<UtilisateurRessource> findById(Long id);
    public List<UtilisateurRessource> findByUtilisateurId(Long id);
    public List<UtilisateurRessource> findByRessourceId(Long id);
    public List<UtilisateurRessource> findByUtilisateurIdAndRessourceId(Long utilisateurId, Long ressourceId);
    public UtilisateurRessource findByRessourceIdAndUtilisateurId(Long idRessource, Long idUtilisateur);
}
