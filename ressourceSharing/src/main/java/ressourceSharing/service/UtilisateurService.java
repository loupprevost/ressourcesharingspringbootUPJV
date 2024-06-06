package ressourceSharing.service;

import ressourceSharing.model.Ressource;
import ressourceSharing.model.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    public List<Utilisateur> getAllUtilisateur();
    public Utilisateur persistUtilisateur(Utilisateur utilisateur);
    public Utilisateur getUtilisateurById(Long id);
    public Utilisateur updateUtilisateur(Utilisateur utilisateur);
    public void deleteUtilisateurById(Long id);
    public Utilisateur getUtilisateurByEmail(String email);
    public Utilisateur getUtilisateurByPseudo(String pseudo);
    public void removeRessourceFromUtilisateur(Utilisateur utilisateur, Ressource ressource);
    public void deleteUtilisateur(Utilisateur utilisateur);
}
