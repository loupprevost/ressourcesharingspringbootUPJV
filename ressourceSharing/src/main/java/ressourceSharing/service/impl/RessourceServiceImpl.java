package ressourceSharing.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ressourceSharing.model.Ressource;
import ressourceSharing.repository.RessourceRepository;
import ressourceSharing.service.RessourceService;

import java.util.List;

@Service
public class RessourceServiceImpl implements RessourceService{
    @Autowired
    private RessourceRepository ressourceRepository;

    @Override
    public List<Ressource> getAllRessource() {
        return ressourceRepository.findAll();
    }

    @Override
    public Ressource updateRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public void deleteRessourceById(Long id) {
        ressourceRepository.deleteById(id);
    }

    @Override
    public Ressource persistRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public Ressource getRessourceById(Long id) {
        return ressourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ressource not found"));
    }

    @Override
    public List<Ressource> getRessourceByNom(String titre) {
        return ressourceRepository.findByNom(titre).orElse(null);
    }

    @Override
    public List<Ressource> getRessourceByDescription(String description) {
        return ressourceRepository.findByDescription(description).orElse(null);
    }

    @Override
    public List<Ressource> getRessourceByType(String type) {
        return ressourceRepository.findByType(type).orElse(null);
    }

    @Override
    public List<Ressource> getRessourceByUtilisateursId(Long id) {
        return ressourceRepository.findByUtilisateursId(id).orElse(null);
    }

    @Override
    public List<Ressource> getRessourceByUtilisateurPseudo(String pseudo) {
        return ressourceRepository.findByUtilisateursPseudo(pseudo).orElse(null);
    }

    @Override
    public void deleteRessource(Ressource ressource) {
        ressourceRepository.delete(ressource);
    }
}
