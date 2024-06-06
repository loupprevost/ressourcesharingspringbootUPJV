package ressourceSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ressourceSharing.model.DemandeEmprunt;
import ressourceSharing.model.Ressource;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.service.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class DemandeEmpruntController {
    RessourceService ressourceService;
    DemandeEmpruntService demandeEmpruntService;
    UtilisateurService utilisateurService;
    @Autowired
    public DemandeEmpruntController(RessourceService ressourceService, UtilisateurService utilisateurService, DemandeEmpruntService demandeEmpruntService)
    {
        this.ressourceService = ressourceService;
        this.demandeEmpruntService = demandeEmpruntService;
        this.utilisateurService = utilisateurService;
    }
    @GetMapping("/demandeEmprunt/create/{ressourceId}")
    public String AfficherDemandeEmprunt(Model model, HttpSession session, @PathVariable String ressourceId)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(ressourceId));
        DemandeEmprunt demandeEmprunt = new DemandeEmprunt();
        demandeEmprunt.setRessource(ressource);
        model.addAttribute("demandeEmprunt", demandeEmprunt);
        return "DemandeEmprunts/create";
    }

    @GetMapping("/demandeEmprunt/create/{ressourceId}/{utilisateurId}")
    public String AfficherDemandeEmprunt(Model model, HttpSession session, @PathVariable String ressourceId, @PathVariable String utilisateurId)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        Utilisateur proprietaire = utilisateurService.getUtilisateurById(Long.parseLong(utilisateurId));
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(ressourceId));
        DemandeEmprunt demandeEmprunt = new DemandeEmprunt();
        demandeEmprunt.setRessource(ressource);
        demandeEmprunt.setProprietaire(proprietaire);
        model.addAttribute("demandeEmprunt", demandeEmprunt);
        return "DemandeEmprunts/create";
    }

    @PostMapping("/demandeEmprunt/create")
    public String CreerDemandeEmprunt(
            Model model,
            HttpSession session,
            DemandeEmprunt demandeEmprunt,
            @RequestParam("ressourceId") Long ressourceId,
            @RequestParam("proprietaireId") Long proprietaireId,
            @RequestParam("dateDebut") String date_debut,
            @RequestParam("dateFin") String date_fin)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = null;
        Date fin = null;
        try {
            debut = dateFormat.parse(date_debut);
            fin = dateFormat.parse(date_fin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // On vérifie s'il existe autant de demande que de quantité entre les dates
        if (demandeEmpruntService.demandeExistsbetweenDates(debut, fin, ressourceId, proprietaireId)) {
            session.setAttribute("error", "Une demande existe déjà entre les dates sélectionnées");
            return "redirect:/demandeEmprunt/create/" + ressourceId + "/" + proprietaireId;
        }

        if (date_debut.equals("") || date_fin.equals("")) {
            session.setAttribute("error", "Veuillez saisir une période valide");
            return "redirect:/demandeEmprunt/create/" + ressourceId + "/" + proprietaireId;
        }

        Utilisateur proprietaire = utilisateurService.getUtilisateurById(proprietaireId);
        Ressource ressource = ressourceService.getRessourceById(ressourceId);

        demandeEmprunt.setDate_debut(debut);
        demandeEmprunt.setDate_fin(fin);

        demandeEmprunt.setRessource(ressource);
        demandeEmprunt.setProprietaire(proprietaire);
        demandeEmprunt.setDemandeur((Utilisateur) session.getAttribute("utilisateur"));
        demandeEmprunt.setStatut("en attente");
        demandeEmpruntService.persistDemandeEmprunt(demandeEmprunt);
        return "redirect:/demandes";
    }

    @GetMapping("/demandeEmprunt/delete/{id}")
    public String SupprimerDemandeEmprunt(Model model, HttpSession session, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        DemandeEmprunt demandeEmprunt = demandeEmpruntService.getDemandeEmpruntById(Long.parseLong(id));
        demandeEmpruntService.deleteDemandeEmprunt(demandeEmprunt);

        session.setAttribute("utilisateur", utilisateurService.getUtilisateurById(((Utilisateur) session.getAttribute("utilisateur")).getId()));
        return "redirect:/utilisateur/profil";
    }

    @GetMapping("/demandeEmprunt/accept/{id}")
    public String AccepterDemandeEmprunt(Model model, HttpSession session, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        DemandeEmprunt demandeEmprunt = demandeEmpruntService.getDemandeEmpruntById(Long.parseLong(id));
        if (demandeEmprunt.getProprietaire().getId() == ((Utilisateur) session.getAttribute("utilisateur")).getId())
        {
            demandeEmprunt.setStatut("acceptee");
            demandeEmpruntService.updateDemandeEmprunt(demandeEmprunt);
        }

        session.setAttribute("utilisateur", utilisateurService.getUtilisateurById(((Utilisateur) session.getAttribute("utilisateur")).getId()));
        return "redirect:/demandes";
    }

    @GetMapping("/demandeEmprunt/refuse/{id}")
    public String RefuserDemandeEmprunt(Model model, HttpSession session, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        DemandeEmprunt demandeEmprunt = demandeEmpruntService.getDemandeEmpruntById(Long.parseLong(id));
        if (demandeEmprunt.getProprietaire().getId() == ((Utilisateur) session.getAttribute("utilisateur")).getId())
        {
            demandeEmprunt.setStatut("refusee");
            demandeEmpruntService.updateDemandeEmprunt(demandeEmprunt);
        }

        session.setAttribute("utilisateur", utilisateurService.getUtilisateurById(((Utilisateur) session.getAttribute("utilisateur")).getId()));
        return "redirect:/demandes";
    }

    @GetMapping("/demandes")
    public String indexDemandes(HttpSession session, Model model)
    {
        if (session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        // On rafrachit les données de l'utilisateur en session
        long id = ((Utilisateur) session.getAttribute("utilisateur")).getId();
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        utilisateur.getDemandesEmpruntEnvoyees().clear();
        demandeEmpruntService.getDemandeEmpruntByDemandeurId(id)
                .forEach(demandeEmprunt -> utilisateur.getDemandesEmpruntEnvoyees().add(demandeEmprunt));

        utilisateur.getDemandesEmpruntRecues().clear();
        demandeEmpruntService.getDemandeEmpruntByProprietaireId(id)
                .forEach(demandeEmprunt -> utilisateur.getDemandesEmpruntRecues().add(demandeEmprunt));
        session.setAttribute("utilisateur", utilisateur);
        model.addAttribute(utilisateur);
        return "DemandeEmprunts/index";
    }
}
