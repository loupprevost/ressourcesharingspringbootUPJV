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
import ressourceSharing.model.Message;
import ressourceSharing.model.Utilisateur;
import ressourceSharing.service.DemandeEmpruntService;
import ressourceSharing.service.MessageService;
import ressourceSharing.service.UtilisateurService;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MessageController {
    MessageService messageService;
    UtilisateurService utilisateurService;

    @Autowired
    public MessageController(MessageService messageService, UtilisateurService utilisateurService, DemandeEmpruntService demandeEmpruntService)
    {
        this.messageService = messageService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/messages")
    public String AfficherMessages(HttpSession session, Model model)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        List<Message> messages = messageService.getMessageByRecepteurIdOrDestinataireId(utilisateur.getId(), utilisateur.getId());
        messages.sort(Collections.reverseOrder(Comparator.comparing(Message::getDate)));
        Map<Utilisateur, Message> lastMessages = new HashMap<>();
        for(Message message : messages)
        {
            if (message.getUtilisateurEmetteur() == null || message.getUtilisateurRecepteur() == null)
            {
                continue;
            }
            if(message.getUtilisateurEmetteur().getId() != utilisateur.getId() && !lastMessages.containsKey(message.getUtilisateurEmetteur()))
            {
                lastMessages.put(message.getUtilisateurEmetteur(), message);
            }
            else if(message.getUtilisateurRecepteur().getId() != utilisateur.getId() && !lastMessages.containsKey(message.getUtilisateurRecepteur()))
            {
                lastMessages.put(message.getUtilisateurRecepteur(), message);
            }
        }
        model.addAttribute("lastMessages", lastMessages);
        return "Messages/index";
    }

    @GetMapping("/message/{destinataireId}")
    public String GetConversation(HttpSession session, Model model, @PathVariable String destinataireId)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Utilisateur destinataire = utilisateurService.getUtilisateurById(Long.parseLong(destinataireId));
        List<Message> conversation = messageService.getMessageByRecepteurIdAndDestinataireId(destinataire.getId(), utilisateur.getId());
        conversation.addAll(messageService.getMessageByRecepteurIdAndDestinataireId(utilisateur.getId(), destinataire.getId()));
        conversation.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
        model.addAttribute("messages", conversation);
        model.addAttribute("destinataire", destinataire);
        Message newMessage = new Message();
        newMessage.setUtilisateurEmetteur(utilisateur);
        newMessage.setUtilisateurRecepteur(destinataire);
        model.addAttribute("newMessage", newMessage);
        return "Messages/message";
    }

    @PostMapping("/message/{destinataireId}")
    public String SendMessage(HttpSession session, @PathVariable String destinataireId, Message message)
    {
        if(session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Utilisateur destinataire = utilisateurService.getUtilisateurById(Long.parseLong(destinataireId));
        message.setUtilisateurEmetteur(utilisateur);
        message.setUtilisateurRecepteur(destinataire);
        message.setDate(LocalDateTime.now());
        messageService.persistMessage(message);
        return "redirect:/message/" + destinataireId;
    }
}
