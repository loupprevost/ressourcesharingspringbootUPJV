package ressourceSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ressourceSharing.model.DemandeEmprunt;
import java.util.List;
import java.util.Optional;
public interface DemandeEmpruntRepository extends JpaRepository<DemandeEmprunt, Long> {
    public List<DemandeEmprunt> findAll();
    public DemandeEmprunt save(DemandeEmprunt demandeEmprunt);
    public void deleteById(Long id);
    public void delete(DemandeEmprunt demandeEmprunt);
    public Optional<List<DemandeEmprunt>> findByRessourceId(Long id);
    public Optional<List<DemandeEmprunt>> findByStatut(String statut);
    public Optional<List<DemandeEmprunt>> findByRessourceIdAndStatut(Long id, String statut);
    public Optional<List<DemandeEmprunt>> findByRessourceIdAndProprietaireId(Long idRessource, Long idProprietaire);
    public Optional<List<DemandeEmprunt>> findByRessourceIdAndProprietaireIdAndStatut(Long idRessource, Long idProprietaire, String statut);
    public Optional<List<DemandeEmprunt>> findByDemandeurId(Long id);
    public Optional<List<DemandeEmprunt>> findByProprietaireId(Long id);
}