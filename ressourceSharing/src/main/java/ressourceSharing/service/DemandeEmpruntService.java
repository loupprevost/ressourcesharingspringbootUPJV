package ressourceSharing.service;
import ressourceSharing.model.DemandeEmprunt;

import java.util.Date;
import java.util.List;
public interface DemandeEmpruntService {
    public List<DemandeEmprunt> getAllDemandeEmprunt();
    public DemandeEmprunt persistDemandeEmprunt(DemandeEmprunt demandeEmprunt);
    public DemandeEmprunt getDemandeEmpruntById(Long id);
    public DemandeEmprunt updateDemandeEmprunt(DemandeEmprunt demandeEmprunt);
    public void deleteDemandeEmpruntById(Long id);
    public void deleteDemandeEmprunt(DemandeEmprunt demandeEmprunt);
    public List<DemandeEmprunt> getDemandeEmpruntByRessourceId(Long id);
    public List<DemandeEmprunt> getDemandeEmpruntByStatut(String statut);
    public List<DemandeEmprunt> getDemandeEmpruntByRessourceIdAndStatut(Long id, String statut);
    public boolean demandeExistsbetweenDates(Date dateDebut, Date dateFin, Long idRessource, Long idUtilisateur);
    public List<DemandeEmprunt> getDemandeEmpruntValidesByRessourceId(Long id);
    public List<DemandeEmprunt> getDemandeEmpruntValidesByRessourceIdAndProprietaireId(Long idRessource, Long idProprietaire);
    public List<DemandeEmprunt> getDemandeEmpruntByDemandeurId(Long id);
    public List<DemandeEmprunt> getDemandeEmpruntByProprietaireId(Long id);
}
