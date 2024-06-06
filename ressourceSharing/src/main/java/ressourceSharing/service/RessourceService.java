package ressourceSharing.service;
import ressourceSharing.model.Ressource;

import java.util.List;
public interface RessourceService {
    public List<Ressource> getAllRessource();
    public Ressource persistRessource(Ressource ressource);
    public Ressource getRessourceById(Long id);
    public Ressource updateRessource(Ressource ressource);
    public void deleteRessourceById(Long id);
    public List<Ressource> getRessourceByNom(String titre);
    public List<Ressource> getRessourceByDescription(String description);
    public List<Ressource> getRessourceByType(String type);
    public List<Ressource> getRessourceByUtilisateursId(Long id);
    public List<Ressource> getRessourceByUtilisateurPseudo(String pseudo);
    public void deleteRessource(Ressource ressource);
}
