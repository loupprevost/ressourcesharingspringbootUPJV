package ressourceSharing.service.impl;

import ressourceSharing.service.UtilisateurRessourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ressourceSharing.model.UtilisateurRessource;
import ressourceSharing.repository.UtilisateurRessourceRepository;

import java.util.List;

@Service

public class UtilisateurRessourceServiceImpl implements UtilisateurRessourceService {
    @Autowired
    private UtilisateurRessourceRepository utilisateurRessourceRepository;

    @Override
    public List<UtilisateurRessource> getAllUtilisateurRessource() {
        return utilisateurRessourceRepository.findAll();
    }

    @Override
    public UtilisateurRessource persistUtilisateurRessource(UtilisateurRessource utilisateurRessource) {
        return utilisateurRessourceRepository.save(utilisateurRessource);
    }

    @Override
    public UtilisateurRessource updateUtilisateurRessource(UtilisateurRessource utilisateurRessource) {
        return utilisateurRessourceRepository.save(utilisateurRessource);
    }

    @Override
    public List<UtilisateurRessource> getUtilisateurRessourceByUtilisateurId(Long id) {
        return utilisateurRessourceRepository.findByUtilisateurId(id);
    }

    @Override
    public List<UtilisateurRessource> getUtilisateurRessourceByRessourceId(Long id) {
        return utilisateurRessourceRepository.findByRessourceId(id);
    }

    @Override
    public UtilisateurRessource getUtilisateurRessourceByRessourceIdAndUtilisateurId(Long idRessource, Long idUtilisateur) {
        return utilisateurRessourceRepository.findByRessourceIdAndUtilisateurId(idRessource, idUtilisateur);
    }
}
