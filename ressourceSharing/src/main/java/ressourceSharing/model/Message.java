package ressourceSharing.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Messages")
@Table(
        name = "messages"
)
public class Message {

    @Id
    @SequenceGenerator(
            name = "messages_sequence",
            sequenceName = "messages_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "messages_sequence"
    )
    @Column(
            name = "id"
    )

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_emetteur_id", referencedColumnName = "id")
    private Utilisateur utilisateurEmetteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_recepteur_id", referencedColumnName = "id")
    private Utilisateur utilisateurRecepteur;

    @Column(
            name = "contenu",
            nullable = false,
            columnDefinition = "TEXT"
    )

    private String contenu;

    @Column(
            name = "date",
            nullable = false,
            columnDefinition = "DATETIME"
    )

    private LocalDateTime date;

    public Message() {
    }

    public Message(Utilisateur utilisateurEmetteur, Utilisateur utilisateurRecepteur, String contenu, LocalDateTime date) {
        this.utilisateurEmetteur = utilisateurEmetteur;
        this.utilisateurRecepteur = utilisateurRecepteur;
        this.contenu = contenu;
        this.date = date;
    }

    public Message(Long id, Utilisateur utilisateurEmetteur, Utilisateur utilisateurRecepteur, String contenu, LocalDateTime date) {
        this.id = id;
        this.utilisateurEmetteur = utilisateurEmetteur;
        this.utilisateurRecepteur = utilisateurRecepteur;
        this.contenu = contenu;
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Utilisateur getUtilisateurEmetteur() {
        return utilisateurEmetteur;
    }

    public Utilisateur getUtilisateurRecepteur() {
        return utilisateurRecepteur;
    }

    public String getContenu() {
        return contenu;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setUtilisateurEmetteur(Utilisateur utilisateurEmetteur) {
        this.utilisateurEmetteur = utilisateurEmetteur;
    }

    public void setUtilisateurRecepteur(Utilisateur utilisateurRecepteur) {
        this.utilisateurRecepteur = utilisateurRecepteur;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String formattedDate = date.format(formatter);

        return formattedDate;
    }
}
