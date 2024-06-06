package ressourceSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ressourceSharing.model.DemandeEmprunt;
import ressourceSharing.model.Ressource;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.service.DemandeEmpruntService;
import ressourceSharing.service.UtilisateurService;
import java.util.List;
import jakarta.servlet.http.HttpSession;
@Controller
public class UtilisateurController {
    UtilisateurService utilisateurService;
    DemandeEmpruntService demandeEmpruntService;
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, DemandeEmpruntService demandeEmpruntService)
    {
        this.utilisateurService = utilisateurService;
        this.demandeEmpruntService = demandeEmpruntService;
    }

    @GetMapping("/utilisateurs")
    public String AfficherUtilisateurs(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateur();
        model.addAttribute("utilisateurs", utilisateurs);
        return "Utilisateurs/index";
    }

    @GetMapping("/utilisateur/profil")
    public String AfficherUtilisateur(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
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
        model.addAttribute("utilisateur", utilisateur);
        return "Utilisateurs/profil";
    }

    @GetMapping("/utilisateur/{id}")
    public String AfficherUtilisateur(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(Long.parseLong(id));
        model.addAttribute("utilisateur", utilisateur);
        return "Utilisateurs/profil";
    }
    @PostMapping("/utilisateurs/{id}")
    public String EditUtilisateur(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        return "Utilisateurs/index";
    }

    @PostMapping("/profil/update")
    public String UpdateUtilisateur(HttpSession session, Model model, Utilisateur utilisateur)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur oldUtilisateur = (Utilisateur) session.getAttribute("utilisateur");

        Utilisateur existsPseudo = utilisateurService.getUtilisateurByPseudo(utilisateur.getPseudo());
        Utilisateur existsEmail = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
        if (
                existsPseudo != null && existsPseudo.getId() != oldUtilisateur.getId()  ||
                existsEmail != null && !existsEmail.getEmail().equals(oldUtilisateur.getEmail())
        )
        {
            session.setAttribute("error", "Ce pseudo ou cet email est déjà utilisé");
            return "redirect:/utilisateur/profil";
        }

        oldUtilisateur.setEmail(utilisateur.getEmail());
        oldUtilisateur.setNom(utilisateur.getNom());
        oldUtilisateur.setPrenom(utilisateur.getPrenom());
        oldUtilisateur.setPseudo(utilisateur.getPseudo());
        oldUtilisateur.setVille(utilisateur.getVille());
        if (!utilisateur.getPassword().isEmpty()) {
            oldUtilisateur.setPassword(utilisateur.getPassword());
        }
        utilisateurService.updateUtilisateur(oldUtilisateur);
        session.setAttribute("utilisateur", oldUtilisateur);
        return "redirect:/utilisateur/profil";
    }

    @GetMapping("/utilisateur/create")
    public String showCreateUtilisateur(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("utilisateur", new Utilisateur());
        return "Utilisateurs/create";
    }

    @PostMapping("/utilisateur/create")
    public String createUtilisateur(HttpSession session, Model model, Utilisateur utilisateur)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur existsPseudo = utilisateurService.getUtilisateurByPseudo(utilisateur.getPseudo());
        Utilisateur existsEmail = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
        if (existsPseudo != null || existsEmail != null)
        {
            session.setAttribute("error", "Ce pseudo ou cet email est déjà utilisé");
            return "redirect:/utilisateur/create";
        }
        utilisateur.setRole("user");
        utilisateurService.persistUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/utilisateur/delete")
    public String deleteUtilisateur(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        utilisateurService.deleteUtilisateur(utilisateur);
        session.setAttribute("success", "Votre compte a été supprimé");
        return "redirect:/login";
    }

    @GetMapping("/utilisateur/delete/{id}")
    public String deleteUtilisateur(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(Long.parseLong(id));
        utilisateurService.deleteUtilisateur(utilisateur);
        session.setAttribute("success", "Le compte a été supprimé");
        return "redirect:/utilisateurs";
    }

    @GetMapping("/utilisateur/changeRole/{id}")
    public ResponseEntity changeRole(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à effectuer cette action");
        }
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(Long.parseLong(id));
        if (utilisateur.getRole().equals("user"))
        {
            utilisateur.setRole("admin");
        }
        else
        {
            utilisateur.setRole("user");
        }
        utilisateurService.updateUtilisateur(utilisateur);
        return ResponseEntity.ok().body("Le role de l'utilisateur a été modifié");
    }
}
