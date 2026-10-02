package application;

import exceptions.SaisieInvalideException;
import exceptions.VideoDejaExistanteException;
import exceptions.VideoIntrouvableException;
import exceptions.VideothequeVideException;
import modele.GestionVideotheque;
import modele.Videotheque;

import static application.Controller.scan;

public class Main {
    private static final GestionVideotheque videotheque = new Videotheque();
    private static final Controller c = new Controller();

    static void main() {
        int choix = 0;
        do {
            try {
                choix = c.afficherMenu();
                scan.nextLine();
                switch (choix) {
                    case 1:
                        c.ajouterVideo();
                        break;
                    case 2:
                        c.listerVideos();
                        break;
                    case 3:
                        c.rechercherVideo();
                        break;
                    case 4:
                        c.supprimerVideo();
                        break;
                    case 5:
                        c.lireVideo();
                        break;
                    case 0:
                        System.out.println("Fin du programme. Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            } catch (SaisieInvalideException | VideoDejaExistanteException | VideothequeVideException |
                     VideoIntrouvableException e) {
                System.err.println(e.getMessage());
            }
        } while (choix != 0);

        scan.close();
    }
}
