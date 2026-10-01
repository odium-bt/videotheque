package application;

import exceptions.SaisieInvalideException;

import java.io.IOException;

import static application.Controller.scan;

public class Main {
    private static final GestionVideotheque videotheque = new Videotheque();
    private static Controller c = new Controller();

    static void main() {
        int choix = 0;
        do {
            try {
                choix = c.afficherMenu();
                scan.nextLine();
                switch (choix) {
                    case 0:
                        System.out.println("Fin du programme. Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            } catch () {
            }
        } while (choix != 0);
        scan.close();
    }
}
