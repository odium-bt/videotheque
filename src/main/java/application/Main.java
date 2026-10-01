package application;

import exceptions.SaisieInvalideException;
import modele.GestionVideotheque;
import modele.Videotheque;

import java.io.IOException;

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
                    case 0:
                        System.out.println("Fin du programme. Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            } catch (SaisieInvalideException e) {
                System.err.println(e.getMessage());
            }
        } while (choix != 0);
        scan.close();
    }
}
