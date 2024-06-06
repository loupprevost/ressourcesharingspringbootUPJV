package ressourceSharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.service.UtilisateurService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    UtilisateurService utilisateurService;

    @Autowired
    public LoginController(UtilisateurService utilisateurService)
    {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String AfficherLoginPage(Model model)
    {
        System.out.println(model.getAttribute("error"));
        return "login";
    }

    @PostMapping("/login")
    public String LoginUser(String email, String password, Model model, HttpSession session)
    {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        if(utilisateur != null)
        {
            if(utilisateur.getPassword().equals(password))
            {
                session.setAttribute("utilisateur", utilisateur);
                return "redirect:/";
            }
            else
            {
                session.setAttribute("error", "Mauvais mot de passe ou email");
            }
        }
        else
        {
            session.setAttribute("error", "Mauvais mot de passe ou email");
        }
        return "login";
    }

    @GetMapping("/register")
    public String AfficherRegisterPage(Model model)
    {
        model.addAttribute("utilisateur", new Utilisateur());
        return "register";
    }

    @PostMapping("/register")
    public String RegisterUser(@ModelAttribute("utilisateur") Utilisateur newUtilisateur, Model model, HttpSession session)
    {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(newUtilisateur.getEmail());
        if(utilisateur != null)
        {
            model.addAttribute("error", "Email already used");
        }
        else
        {
            utilisateur = utilisateurService.getUtilisateurByPseudo(newUtilisateur.getPseudo());
            if(utilisateur != null)
            {
                model.addAttribute("error", "Pseudo already used");
            }
            else
            {
                newUtilisateur.setRole("user");
                utilisateurService.persistUtilisateur(newUtilisateur);
            }
        }
        if (model.containsAttribute("error")) {
            return "register";
        } else {
            session.setAttribute("success", "Compte créé avec succès, veuillez vous connecter pour continuer");
            return "redirect:/login";
        }
    }
}
