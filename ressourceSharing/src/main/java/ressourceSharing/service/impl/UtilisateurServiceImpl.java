package ressourceSharing.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ressourceSharing.model.Ressource;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.repository.UtilisateurRepository;
import ressourceSharing.service.UtilisateurService;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public List<Utilisateur> getAllUtilisateur() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateurById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public Utilisateur persistUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur not found"));
    }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudo) {
        return utilisateurRepository.findByPseudo(pseudo).orElse(null);
    }

    @Override
    @Transactional
    public void removeRessourceFromUtilisateur(Utilisateur utilisateur, Ressource ressource) {
        utilisateur.removeRessource(ressource);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
    }
}
