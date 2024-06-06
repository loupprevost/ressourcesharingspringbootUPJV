package ressourceSharing.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import ressourceSharing.model.UtilisateurRessource;
import ressourceSharing.service.DemandeEmpruntService;
import ressourceSharing.service.RessourceService;
import ressourceSharing.service.UtilisateurRessourceService;
import ressourceSharing.service.UtilisateurService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class RessourceController {
    RessourceService ressourceService;
    UtilisateurService utilisateurService;
    DemandeEmpruntService demandeEmpruntService;
    UtilisateurRessourceService utilisateurRessourceService;

    @Autowired
    public RessourceController(RessourceService ressourceService, UtilisateurService utilisateurService, DemandeEmpruntService demandeEmpruntService, UtilisateurRessourceService utilisateurRessourceService)
    {
        this.ressourceService = ressourceService;
        this.utilisateurService = utilisateurService;
        this.demandeEmpruntService = demandeEmpruntService;
        this.utilisateurRessourceService = utilisateurRessourceService;
    }

    @GetMapping("/ressources")
    public String AfficherRessources(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        List<Ressource> ressources = ressourceService.getAllRessource();
        model.addAttribute("ressources", ressources);
        return "Ressources/index";
    }

    @GetMapping("/ressource/{id}")
    public String AfficherRessource(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(id));
        Set<Utilisateur> utilisateurs = ressource.getUtilisateurs();
        model.addAttribute("ressource", ressource);
        model.addAttribute("utilisateurs", utilisateurs);
        return "Ressources/ressource";
    }

    @GetMapping("/ressource/create")
    public String AfficherCreationRessource(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        model.addAttribute("ressource", new Ressource());
        return "Ressources/create";
    }

    @PostMapping("/ressource/create")
    public String CreationRessource(HttpSession session, Model model, Ressource ressource)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        ressourceService.persistRessource(ressource);

        return "redirect:/ressources";
    }

    @GetMapping("/ressource/addUser/{id}")
    public String AjouterUtilisateur(HttpSession session, Model model, @PathVariable String id, HttpServletRequest request)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(id));

        Optional<UtilisateurRessource> existingAssociation = utilisateur.getUtilisateurRessources().stream()
                .filter(ur -> ur.getRessource().equals(ressource))
                .findFirst();

        if (existingAssociation.isPresent()) {
            // Si l'utilisateur possède déjà la ressource, ajoute 1 à la quantité
            existingAssociation.get().addQuantite(1);
            utilisateurRessourceService.persistUtilisateurRessource(existingAssociation.get());
        } else {
            // Si l'utilisateur ne possède pas encore la ressource, crée une nouvelle association
            UtilisateurRessource newAssociation = new UtilisateurRessource(utilisateur, ressource);
            newAssociation.setQuantite(1); // Initialise la quantité à 1
            utilisateurRessourceService.persistUtilisateurRessource(newAssociation);
            utilisateur.addRessource(ressource);
            utilisateur.addUtilisateurRessource(newAssociation);
            ressource.addUtilisateur(utilisateur);
            ressource.addUtilisateurRessource(newAssociation);
        }
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            // Si l'URL précédente n'est pas disponible, rediriger vers une URL par défaut
            return "redirect:/ressources";
        }
    }

    @GetMapping("/ressource/removeOne/{id}")
    public String SupprimerUneRessource(HttpSession session, Model model, @PathVariable String id, HttpServletRequest request)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(id));
        Optional<UtilisateurRessource> existingAssociation = utilisateur.getUtilisateurRessources().stream()
                .filter(ur -> ur.getRessource().equals(ressource))
                .findFirst();

        if (existingAssociation.isPresent() && existingAssociation.get().getQuantite() > 1) {
            // Si l'utilisateur possède plus d'une unité de la ressource, retire 1 à la quantité
            existingAssociation.get().addQuantite(-1);
            utilisateurRessourceService.persistUtilisateurRessource(existingAssociation.get());
        } else if (existingAssociation.isPresent() && existingAssociation.get().getQuantite() == 1) {
            // Si l'utilisateur possède une seule unité de la ressource, retire la ressource
            utilisateur.removeRessource(ressource);
            utilisateur.removeUtilisateurRessource(existingAssociation.get());
        }
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            // Si l'URL précédente n'est pas disponible, rediriger vers une URL par défaut
            return "redirect:/ressources";
        }
    }

    @GetMapping("/ressource/removeUser/{id}")
    public String SupprimerUtilisateur(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(id));

        utilisateur.removeRessource(ressource);
        utilisateur.removeUtilisateurRessource(utilisateur.getUtilisateurRessources().stream()
                .filter(ur -> ur.getRessource().equals(ressource))
                .findFirst()
                .get());

        return "redirect:/ressources";
    }

    @GetMapping("/ressource/getDatesInvalid/{id}/{propId}")
    public ResponseEntity<List<String[]>> getDatesInvalid(HttpSession session, Model model, @PathVariable String id, @PathVariable String propId)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return null;
        }
        Long ressourceId = Long.parseLong(id);
        Long proprietaireId = Long.parseLong(propId);
        // On récupère les demandes d'emprunt validées de la ressource
        List<DemandeEmprunt> demandesValides = demandeEmpruntService.getDemandeEmpruntValidesByRessourceIdAndProprietaireId(ressourceId, proprietaireId);
        // On crée une liste de dates invalides
        List<String[]> datesInvalides = new ArrayList<String[]>();
        UtilisateurRessource association = utilisateurRessourceService.getUtilisateurRessourceByRessourceIdAndUtilisateurId(ressourceId, proprietaireId);
        int quantite = association.getQuantite();
        Map<String, Integer> dateDemandeCount = new HashMap<>();
        // On ajoute les dates de début et de fin des demandes validées à la liste
        for (DemandeEmprunt demande : demandesValides) {
            if (quantite > 1) {
                String dateDebut = demande.getDate_debut().toString();
                String dateFin = demande.getDate_fin().toString();

                // Convertir la date de début en Calendar
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(demande.getDate_debut());

                // Tant que la date n'est pas égale à la date de fin
                while (!calendar.getTime().after(demande.getDate_fin())) {
                    // Obtenir la date actuelle
                    Date currentDate = calendar.getTime();

                    // Incrémenter le compteur pour cette date
                    String dateKey = currentDate.toString();
                    dateDemandeCount.put(dateKey, dateDemandeCount.getOrDefault(dateKey, 0) + 1);

                    // Passer à la prochaine date
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                datesInvalides.add(new String[]{demande.getDate_debut().toString(), demande.getDate_fin().toString()});
            }
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // On ajoute les dates invalides à la liste si le nombre de demandes validées pour chaque date est égal à la quantité de ressources
        for (Map.Entry<String, Integer> entry : dateDemandeCount.entrySet()) {
            String date = entry.getKey();
            LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
            String formattedDate = dateTime.format(outputFormatter);
            int count = entry.getValue();
            if (count >= quantite) {
                datesInvalides.add(new String[]{formattedDate, formattedDate});
            }
        }


        return ResponseEntity.ok(datesInvalides);
    }

    @GetMapping("/ressource/delete/{id}")
    public String SupprimerRessource(HttpSession session, Model model, @PathVariable String id)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Ressource ressource = ressourceService.getRessourceById(Long.parseLong(id));
        ressourceService.deleteRessource(ressource);
        session.setAttribute("success", "La ressource a été supprimée avec succès.");
        return "redirect:/ressources";
    }
}
