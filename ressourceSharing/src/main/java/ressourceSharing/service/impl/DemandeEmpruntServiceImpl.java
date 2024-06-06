package ressourceSharing.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ressourceSharing.model.DemandeEmprunt;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.model.UtilisateurRessource;
import ressourceSharing.repository.DemandeEmpruntRepository;
import ressourceSharing.repository.UtilisateurRessourceRepository;
import ressourceSharing.service.DemandeEmpruntService;
import ressourceSharing.service.UtilisateurRessourceService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeEmpruntServiceImpl implements DemandeEmpruntService {
    @Autowired
    private DemandeEmpruntRepository demandeEmpruntRepository;
    @Autowired
    private UtilisateurRessourceRepository utilisateurRessourceRepository;
    @Autowired
    private UtilisateurRessourceService utilisateurRessourceService;


    @Override
    public List<DemandeEmprunt> getAllDemandeEmprunt() {
        return demandeEmpruntRepository.findAll();
    }

    @Override
    public DemandeEmprunt updateDemandeEmprunt(DemandeEmprunt demandeEmprunt) {
        return demandeEmpruntRepository.save(demandeEmprunt);
    }

    @Override
    @Transactional
    public void deleteDemandeEmpruntById(Long id) {
        demandeEmpruntRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteDemandeEmprunt(DemandeEmprunt demandeEmprunt) {
        demandeEmpruntRepository.delete(demandeEmprunt);
    }

    @Override
    public DemandeEmprunt persistDemandeEmprunt(DemandeEmprunt demandeEmprunt) {
        return demandeEmpruntRepository.save(demandeEmprunt);
    }

    @Override
    public DemandeEmprunt getDemandeEmpruntById(Long id) {
        return demandeEmpruntRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DemandeEmprunt not found"));
    }

    @Override
    public List<DemandeEmprunt> getDemandeEmpruntByRessourceId(Long id) {
        return demandeEmpruntRepository.findByRessourceId(id).orElse(null);
    }

    @Override
    public List<DemandeEmprunt> getDemandeEmpruntByStatut(String statut) {
        return demandeEmpruntRepository.findByStatut(statut).orElse(null);
    }

    @Override
    public List<DemandeEmprunt> getDemandeEmpruntByRessourceIdAndStatut(Long id, String statut) {
        return demandeEmpruntRepository.findByRessourceIdAndStatut(id, statut).orElse(null);
    }

    @Override
    public boolean demandeExistsbetweenDates(Date dateDebut, Date dateFin, Long idRessource, Long idUtilisateur) {
        Optional<List<DemandeEmprunt>> demandes = demandeEmpruntRepository.findByRessourceIdAndProprietaireId(idRessource, idUtilisateur);

        if (demandes.isPresent()) {
            for (DemandeEmprunt demande : demandes.get()) {
                // Vérifie si la demande se situe entre les dates spécifiées
                boolean isInDateRange = (dateDebut.before(demande.getDate_fin()) || dateDebut.equals(demande.getDate_fin())) &&
                        (dateFin.after(demande.getDate_debut()) || dateFin.equals(demande.getDate_debut()));

                if (isInDateRange) {
                    // Vérifie si le nombre de demandes pour cette date est égal à la quantité de ressource
                    UtilisateurRessource association = utilisateurRessourceService.getUtilisateurRessourceByRessourceIdAndUtilisateurId(idRessource, idUtilisateur);
                    int quantite = association.getQuantite();
                    List<DemandeEmprunt> demandesValides = this.getDemandeEmpruntValidesByRessourceIdAndProprietaireId(idRessource, idUtilisateur);
                    long count = demandesValides.stream()
                            .filter(d -> d.getDate_debut().equals(demande.getDate_debut()) || d.getDate_fin().equals(demande.getDate_fin()))
                            .count();
                    if (count == quantite) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<DemandeEmprunt> getDemandeEmpruntValidesByRessourceId(Long id) {
        return demandeEmpruntRepository.findByRessourceIdAndStatut(id, "acceptee").orElse(null);
    }

    public List<DemandeEmprunt> getDemandeEmpruntValidesByRessourceIdAndProprietaireId(Long idRessource, Long idProprietaire) {
        return demandeEmpruntRepository.findByRessourceIdAndProprietaireIdAndStatut(idRessource, idProprietaire, "acceptee").orElse(null);
    }

    @Override
    public List<DemandeEmprunt> getDemandeEmpruntByDemandeurId(Long id) {
        return demandeEmpruntRepository.findByDemandeurId(id).orElse(null);
    }

    @Override
    public List<DemandeEmprunt> getDemandeEmpruntByProprietaireId(Long id) {
        return demandeEmpruntRepository.findByProprietaireId(id).orElse(null);
    }
}
