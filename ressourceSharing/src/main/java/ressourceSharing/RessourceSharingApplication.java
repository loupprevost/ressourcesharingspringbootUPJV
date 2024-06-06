package ressourceSharing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ressourceSharing.model.*;
import ressourceSharing.repository.RessourceRepository;
import ressourceSharing.repository.UtilisateurRepository;
import ressourceSharing.repository.UtilisateurRessourceRepository;

@SpringBootApplication
public class RessourceSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RessourceSharingApplication.class, args);
    }

    @Bean
    CommandLineRunner AjoutDonneesDatabase(
            RessourceRepository ressourceRepository,
            UtilisateurRepository utilisateurRepository,
            UtilisateurRessourceRepository utilisateurRessourceRepository
    ) {
        return args -> {
            if (utilisateurRepository.findByEmail("fabriceCabrol@mail.com").isPresent()) {
                return;
            }
            Utilisateur user1 = new Utilisateur("Dupont", "Jean", "JD", "jeanDupont@mail.com", "jdmail", "user", "Amiens");
            Utilisateur admin = new Utilisateur("Admin", "Admin", "Admin", "admin@mail.com", "admin", "superadmin", "Paris");
            Utilisateur user2 = new Utilisateur("Cabrol", "Fabrice", "FC", "fabriceCabrol@mail.com", "fcmail", "user", "Lille");

            utilisateurRepository.save(user1);
            utilisateurRepository.save(admin);
            utilisateurRepository.save(user2);

            Ressource ressource1 = new Ressource("Marteau", "Pour taper des choses", "Matériel");
            Ressource ressource2 = new Ressource("Tournevis", "Pour visser des choses", "Matériel");
            Ressource ressource3 = new Ressource("Pelle", "Pour creuser des choses", "Matériel");

            Ressource ressource4 = new Ressource("Java", "Pour coder des choses", "Savoir faire");
            Ressource ressource5 = new Ressource("Bricolage", "Pour réparer des choses", "Savoir faire");
            Ressource ressource6 = new Ressource("Cuisine", "Pour cuisiner des choses", "Savoir faire");

            ressourceRepository.save(ressource1);
            ressourceRepository.save(ressource2);
            ressourceRepository.save(ressource3);
            ressourceRepository.save(ressource4);
            ressourceRepository.save(ressource5);
            ressourceRepository.save(ressource6);

            user1 = utilisateurRepository.findByEmail("jeanDupont@mail.com").get();
            user2 = utilisateurRepository.findByEmail("fabriceCabrol@mail.com").get();

            UtilisateurRessource utilisateurRessource1 = new UtilisateurRessource(user1, ressource1);
            UtilisateurRessource utilisateurRessource2 = new UtilisateurRessource(user1, ressource2);
            UtilisateurRessource utilisateurRessource3 = new UtilisateurRessource(user1, ressource3);
            UtilisateurRessource utilisateurRessource5 = new UtilisateurRessource(user1, ressource5);
            UtilisateurRessource utilisateurRessource6 = new UtilisateurRessource(user2, ressource6);
            UtilisateurRessource utilisateurRessource4 = new UtilisateurRessource(user2, ressource4);

            utilisateurRessourceRepository.save(utilisateurRessource1);
            utilisateurRessourceRepository.save(utilisateurRessource2);
            utilisateurRessourceRepository.save(utilisateurRessource3);
            utilisateurRessourceRepository.save(utilisateurRessource5);
            utilisateurRessourceRepository.save(utilisateurRessource6);
            utilisateurRessourceRepository.save(utilisateurRessource4);

            utilisateurRessource1 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user1.getId(), ressource1.getId()).get(0);
            utilisateurRessource2 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user1.getId(), ressource2.getId()).get(0);
            utilisateurRessource3 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user1.getId(), ressource3.getId()).get(0);
            utilisateurRessource5 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user1.getId(), ressource5.getId()).get(0);
            utilisateurRessource6 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user2.getId(), ressource6.getId()).get(0);
            utilisateurRessource4 = utilisateurRessourceRepository.findByUtilisateurIdAndRessourceId(user2.getId(), ressource4.getId()).get(0);

            user1.addRessource(ressource1);
            user1.addUtilisateurRessource(utilisateurRessource1);
            user1.addRessource(ressource2);
            user1.addUtilisateurRessource(utilisateurRessource2);
            user1.addRessource(ressource3);
            user1.addUtilisateurRessource(utilisateurRessource3);
            user1.addRessource(ressource5);
            user1.addUtilisateurRessource(utilisateurRessource5);
            user2.addRessource(ressource6);
            user2.addUtilisateurRessource(utilisateurRessource6);
            user2.addRessource(ressource4);
            user2.addUtilisateurRessource(utilisateurRessource4);


            utilisateurRessourceRepository.save(utilisateurRessource1);
            utilisateurRessourceRepository.save(utilisateurRessource2);
            utilisateurRessourceRepository.save(utilisateurRessource3);
            utilisateurRessourceRepository.save(utilisateurRessource5);
            utilisateurRessourceRepository.save(utilisateurRessource6);
            utilisateurRessourceRepository.save(utilisateurRessource4);
        };
    }
}
