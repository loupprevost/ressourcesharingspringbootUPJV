package ressourceSharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Ressources")
@Table(
        name = "ressources"
)
public class Ressource {
    @Id
    @SequenceGenerator(
            name = "ressources_sequence",
            sequenceName = "ressources_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "ressources_sequence"
    )
    @Column(
            name = "id"
    )

    private Long id;

    @Column(
            name = "nom",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String nom;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )

    private String description;

    @Column(
            name = "type",
            nullable = false,
            columnDefinition = "TEXT"
    )

    private String type;

    @ManyToMany (fetch = FetchType.EAGER,cascade = {CascadeType.ALL} , mappedBy = "ressources")
    @JsonIgnore
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    @OneToMany(mappedBy = "ressource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UtilisateurRessource> utilisateurRessources;

    public Ressource() {
    }

    public Ressource(String nom, String description, String type) {
        this.nom = nom;
        this.description = description;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
    }

    public void removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur);
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
    public String getBackground() {
        switch (this.type) {
            case "Matériel":
                return "#e6e6ff;";
            case "Savoir faire":
                return "#e6ffe9;";
        }
        return "#ffffff;";
    }

    @Override
    public String toString() {
        return "Ressource: " + this.nom + " " + this.description + " " + this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Ressource)) {
            return false;
        }
        Ressource ressource = (Ressource) o;
        return id == ressource.id && nom.equals(ressource.nom) && description.equals(ressource.description) && type.equals(ressource.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, type);
    }
}
