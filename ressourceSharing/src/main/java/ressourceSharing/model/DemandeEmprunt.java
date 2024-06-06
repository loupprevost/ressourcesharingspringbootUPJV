package ressourceSharing.model;

import jakarta.persistence.*;

import java.util.Date;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "DemandeEmprunts")
@Table(
        name = "demande_emprunts"
)
public class DemandeEmprunt {
    @Id
    @SequenceGenerator(
            name = "demandeEmprunts_sequence",
            sequenceName = "demandeEmprunts_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "demandeEmprunts_sequence"
    )
    @Column(
            name = "id"
    )

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demandeur_id")
    private Utilisateur demandeur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ressource_id", referencedColumnName = "id")
    private Ressource ressource;

    @Column(
            name = "date_debut",
            nullable = false,
            columnDefinition = "DATE"
    )

    private Date date_debut;

    @Column(
            name = "date_fin",
            nullable = false,
            columnDefinition = "DATE"
    )

    private Date date_fin;

    @Column(
            name = "statut",
            nullable = false,
            columnDefinition = "TEXT"
    )

    private String statut;

    public DemandeEmprunt() {
    }

    public DemandeEmprunt(Utilisateur demandeur, Utilisateur proprietaire, Ressource ressource, Date date_debut, Date date_fin, String statut) {
        this.demandeur = demandeur;
        this.proprietaire = proprietaire;
        this.ressource = ressource;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur utilisateur) {
        this.demandeur = utilisateur;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur utilisateur) {
        this.proprietaire = utilisateur;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @PreRemove
    private void preRemove()
    {
        if (this.demandeur != null) {
            this.demandeur.getDemandesEmpruntEnvoyees().remove(this);
            this.demandeur = null;
        }
        if (this.proprietaire != null) {
            this.proprietaire.getDemandesEmpruntRecues().remove(this);
            this.proprietaire = null;
        }
    }

    @Override
    public String toString() {
        return "DemandeEmprunt{" +
                "id=" + id +
                ", demandeur=" + demandeur +
                ", proprietaire=" + proprietaire +
                ", ressource=" + ressource +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", statut='" + statut + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandeEmprunt)) return false;
        DemandeEmprunt that = (DemandeEmprunt) o;
        return getId().equals(that.getId()) && getDemandeur().equals(that.getDemandeur()) && getProprietaire().equals(that.getProprietaire()) && getRessource().equals(that.getRessource()) && getDate_debut().equals(that.getDate_debut()) && getDate_fin().equals(that.getDate_fin()) && getStatut().equals(that.getStatut());
    }
}
