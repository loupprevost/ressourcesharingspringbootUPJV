package ressourceSharing.service;

import ressourceSharing.model.UtilisateurRessource;

import java.util.List;

public interface UtilisateurRessourceService {
    public List<UtilisateurRessource> getAllUtilisateurRessource();
    public UtilisateurRessource persistUtilisateurRessource(UtilisateurRessource utilisateurRessource);
    public UtilisateurRessource updateUtilisateurRessource(UtilisateurRessource utilisateurRessource);
    public List<UtilisateurRessource> getUtilisateurRessourceByUtilisateurId(Long id);
    public List<UtilisateurRessource> getUtilisateurRessourceByRessourceId(Long id);
    public UtilisateurRessource getUtilisateurRessourceByRessourceIdAndUtilisateurId(Long idRessource, Long idUtilisateur);
}
