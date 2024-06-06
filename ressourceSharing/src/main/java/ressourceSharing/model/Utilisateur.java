package ressourceSharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Utilisateurs")
@Table(
        name = "utilisateurs"
)
public class Utilisateur {
    @Id
    @SequenceGenerator(
            name = "utilisateurs_sequence",
            sequenceName = "utilisateurs_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "utilisateurs_sequence"
    )

    @Column(
            name = "id"
    )
    private long id;

    @Column(
            name = "nom",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String nom;

    @Column(
            name = "prenom",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String prenom;

    @Column(
            name = "pseudo",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String pseudo;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "utilisateur_ressource",
            joinColumns = { @JoinColumn(name = "proprietaire_id")},
            inverseJoinColumns = { @JoinColumn(name = "ressource_id")}
    )
    @JsonIgnore
    private Set<Ressource> ressources = new HashSet<>();

    @OneToMany(mappedBy = "demandeur", fetch = FetchType.EAGER)
    private List<DemandeEmprunt> demandesEmpruntEnvoyees;

    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.EAGER)
    private List<DemandeEmprunt> demandesEmpruntRecues;

    @Column(
            name = "role",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String role;

    @Column(
            name = "ville",
            columnDefinition = "TEXT"
    )
    private String ville;

    @OneToMany(mappedBy = "utilisateurEmetteur", fetch = FetchType.EAGER)
    private List<Message> messagesEnvoyes;

    @OneToMany(mappedBy = "utilisateurRecepteur", fetch = FetchType.EAGER)
    private List<Message> messagesRecus;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UtilisateurRessource> utilisateurRessources;

    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String pseudo, String email, String password, String role, String ville) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
        this.ville = ville;
    }

    public long getId() {
        return this.id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public Set<Ressource> getRessources() {
        return ressources;
    }

    public void setRessources(Set<Ressource> ressources) {
        this.ressources = ressources;
    }

    public void addRessource(Ressource ressource) {
        this.ressources.add(ressource);
        ressource.addUtilisateur(this);
    }

    public void removeRessource(Ressource ressource) {
        this.ressources.remove(ressource);
        ressource.removeUtilisateur(this);
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {

        return (this.role.equals("superadmin") || this.role.equals("admin"));
    }

    public boolean isUser() {
        return this.role.equals("user");
    }

    public boolean isSuperAdmin() {
        return this.role.equals("superadmin");
    }
    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getVille() {
        return this.ville;
    }

    public boolean hasRessource(int ressourceId) {
        for (Ressource ressource : this.ressources) {
            if (ressource.getId() == ressourceId) {
                return true;
            }
        }
        return false;
    }

    public List<DemandeEmprunt> getDemandesEmpruntEnvoyees() {
        return demandesEmpruntEnvoyees;
    }

    // Récupèration des demandes en attente encore valide
    public List<DemandeEmprunt> getDemandesEmpruntEnvoyeesEnAttente() {
        return demandesEmpruntEnvoyees.stream()
            .filter(demandeEmprunt -> demandeEmprunt.getStatut().equals("en attente") &&
                    demandeEmprunt.getDate_fin().after(new Date()))
            .collect(Collectors.toList());
    }

    public List<DemandeEmprunt> getDemandesEmpruntEnvoyeesRepondues() {
        return demandesEmpruntEnvoyees.stream()
                    .filter(demandeEmprunt -> !demandeEmprunt.getStatut().equals("en attente") &&
                    demandeEmprunt.getDate_fin().after(new Date()))
            .collect(Collectors.toList());
    }

    public void setDemandesEmpruntEnvoyees(List<DemandeEmprunt> demandesEmpruntEnvoyees) {
        this.demandesEmpruntEnvoyees = demandesEmpruntEnvoyees;
    }

    public void addDemandeEmpruntEnvoyee(DemandeEmprunt demandeEmprunt) {
        this.demandesEmpruntEnvoyees.add(demandeEmprunt);
    }

    public void removeDemandeEmpruntEnvoyee(DemandeEmprunt demandeEmprunt) {
        this.demandesEmpruntEnvoyees.remove(demandeEmprunt);
    }

    public List<DemandeEmprunt> getDemandesEmpruntRecues() {
        return demandesEmpruntRecues;
    }

    public List<DemandeEmprunt> getDemandesEmpruntRecuesEnAttente() {
        return demandesEmpruntRecues.stream()
            .filter(demandeEmprunt -> demandeEmprunt.getStatut().equals("en attente") &&
                    demandeEmprunt.getDate_fin().after(new Date()))
            .collect(Collectors.toList());
    }

    public List<DemandeEmprunt> getDemandesEmpruntRecuesRepondues() {
        return demandesEmpruntRecues.stream()
            .filter(demandeEmprunt -> !demandeEmprunt.getStatut().equals("en attente") &&
                    demandeEmprunt.getDate_fin().after(new Date()))
            .collect(Collectors.toList());
    }

    public void addDemandeEmpruntRecue(DemandeEmprunt demandeEmprunt) {
        this.demandesEmpruntRecues.add(demandeEmprunt);
    }

    public void removeDemandeEmpruntRecue(DemandeEmprunt demandeEmprunt) {
        this.demandesEmpruntRecues.remove(demandeEmprunt);
    }
    public void setDemandesEmpruntRecues(List<DemandeEmprunt> demandesEmpruntRecues) {
        this.demandesEmpruntRecues = demandesEmpruntRecues;
    }

    public List<Message> getMessagesEnvoyes() {
        return messagesEnvoyes;
    }

    public void setMessagesEnvoyes(List<Message> messagesEnvoyes) {
        this.messagesEnvoyes = messagesEnvoyes;
    }

    public void addMessageEnvoye(Message message) {
        this.messagesEnvoyes.add(message);
    }

    public void removeMessageEnvoye(Message message) {
        this.messagesEnvoyes.remove(message);
    }

    public List<Message> getMessagesRecus() {
        return messagesRecus;
    }

    public void setMessagesRecus(List<Message> messagesRecus) {
        this.messagesRecus = messagesRecus;
    }

    public void addMessageRecu(Message message) {
        this.messagesRecus.add(message);
    }

    public void removeMessageRecu(Message message) {
        this.messagesRecus.remove(message);
    }

    public List<Utilisateur> getContacts() {
        // Récupération des contacts
        List<Utilisateur> contacts = messagesEnvoyes.stream()
            .map(message -> message.getUtilisateurRecepteur())
            .collect(Collectors.toList());
        contacts.addAll(messagesRecus.stream()
            .map(message -> message.getUtilisateurEmetteur())
            .collect(Collectors.toList()));

        // Suppression des doublons
        contacts = contacts.stream().distinct().collect(Collectors.toList());

        return contacts;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom=" + nom +
                ", prenom='" + prenom + '\'' +
                ", pseudo=" + pseudo +
                "}\n";
    }

    public String nomPrenom() {
        return this.nom + " " + this.prenom;
    }
    public String prenomNom() {
        return this.prenom + " " + this.nom;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        Utilisateur utilisateur = (Utilisateur) o;
        return id == utilisateur.id && nom.equals(utilisateur.nom) && prenom.equals(utilisateur.prenom) && pseudo.equals(utilisateur.pseudo) && email.equals(utilisateur.email) && password.equals(utilisateur.password) && role.equals(utilisateur.role);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nom, prenom, pseudo, email, password, role);
    }

    public int getNbRessource(Ressource ressource) {
        for (UtilisateurRessource utilisateurRessource : this.getUtilisateurRessources()) {
            if (utilisateurRessource.getRessource().equals(ressource)) {
                return utilisateurRessource.getQuantite();
            }
        }
        return 0;
    }

    public Set<UtilisateurRessource> getUtilisateurRessources() {
        return utilisateurRessources;
    }

    public void setUtilisateurRessources(Set<UtilisateurRessource> utilisateurRessources) {
        this.utilisateurRessources = utilisateurRessources;
    }

    public void addUtilisateurRessource(UtilisateurRessource utilisateurRessource) {
        this.utilisateurRessources.add(utilisateurRessource);
    }

    public void removeUtilisateurRessource(UtilisateurRessource utilisateurRessource) {
        this.utilisateurRessources.remove(utilisateurRessource);
    }

    public Message getLastMessageWithUser(Utilisateur utilisateur) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(this.messagesEnvoyes);
        messages.addAll(this.messagesRecus);
        messages = messages.stream()
            .filter(message -> message.getUtilisateurEmetteur().equals(utilisateur) || message.getUtilisateurRecepteur().equals(utilisateur))
            .collect(Collectors.toList());
        if (messages.size() == 0) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    @PreRemove
    private void preRemove() {
        // Suppression des ressources
        for (Ressource ressource : this.ressources) {
            ressource.removeUtilisateur(this);
        }
        // Suppression des demandes
        for (DemandeEmprunt demandeEmprunt : this.demandesEmpruntEnvoyees) {
            demandeEmprunt.setDemandeur(null);
        }
        for (DemandeEmprunt demandeEmprunt : this.demandesEmpruntRecues) {
            demandeEmprunt.setProprietaire(null);
        }
        // Suppression des messages
        for (Message message : this.messagesEnvoyes) {
            message.setUtilisateurEmetteur(null);
        }
        for (Message message : this.messagesRecus) {
            message.setUtilisateurRecepteur(null);
        }
        // Suppression des utilisateurRessources
        for (UtilisateurRessource utilisateurRessource : this.utilisateurRessources) {
            utilisateurRessource.setUtilisateur(null);
            utilisateurRessource.setRessource(null);
        }
    }
}
