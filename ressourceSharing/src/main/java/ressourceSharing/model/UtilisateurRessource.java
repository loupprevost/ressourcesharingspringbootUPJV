package ressourceSharing.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "utilisateur_ressource")
public class UtilisateurRessource {
    @Id
    @SequenceGenerator(
            name = "utilisateurRessource_sequence",
            sequenceName = "utilisateurRessource_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "utilisateurRessource_sequence"
    )

    @Column(
            name = "id"
    )

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietaire_id", referencedColumnName = "id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ressource_id", referencedColumnName = "id")
    private Ressource ressource;

    @Column(
            name = "quantite",
            nullable = false,
            columnDefinition = "INTEGER DEFAULT '1'"
    )
    private int quantite;

    public UtilisateurRessource() {
    }

    public UtilisateurRessource(Utilisateur utilisateur, Ressource ressource) {
        this.utilisateur = utilisateur;
        this.ressource = ressource;
        this.quantite = 1;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public void addQuantite(Integer quantite) {
        this.quantite += quantite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }
}
