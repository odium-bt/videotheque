package application;

import exceptions.SaisieInvalideException;
import modele.FichierVideo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    static Scanner scan = new Scanner(System.in);

    /**
     * Affiche le menu principal
     */
    public int afficherMenu() {
        System.out.println("===== GESTION DE LA VIDEOTHEQUE =====");
        System.out.println("1. Ajouter une vidéo");
        System.out.println("2. Supprimer une vidéo");
        System.out.println("3. Afficher le contenu de la vidéothèque");
        System.out.println("4. Vider la vidéothèque");
        System.out.println("5. Rechercher une vidéo");
        System.out.println("6. Écouter une vidéo");
        System.out.println("7. Arrêter la vidéo");
        System.out.println("0. Quitter");
        System.out.print("Choix:");
        try {
            return Controller.scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer le numéro d'une des options données.");
            scan.nextLine();
            return afficherMenu();
        }
    }

    public String saisieStr(String msg) throws SaisieInvalideException {
        String s;
        System.out.print(msg);
        s = scan.nextLine();
        if (s.isEmpty()) {
            throw new SaisieInvalideException("La donnée entrée est invalide !");
        }
        return s;
    }

    private int saisieInt(String msg) throws SaisieInvalideException {
        int s;
        System.out.print(msg);
        try {
            s = scan.nextInt();
            scan.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Donnée invalide, veuillez entrer un chiffre.");
            scan.nextLine();
            return saisieInt(msg);
        }
        if (s < 0) {
            throw new SaisieInvalideException("La donnée entrée est invalide !");
        }
        return s;
    }

    private double saisieTailleD() throws SaisieInvalideException {
        double s;
        System.out.print("Taille du fichier (en Mo) : ");
        try {
            s = scan.nextDouble();
            scan.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Merci d'entrer une taille valide (exemple : 34.5)");
            scan.nextLine();
            return saisieTailleD();
        }
        if (s <= 0) {
            throw new SaisieInvalideException("La taille du fichier doit être supérieure à 0.");
        }
        return s;
    }

    public String saisieAuteur() throws SaisieInvalideException {
        return (saisieStr("Nom de l'auteur : "));
    }


    public String saisieNomA() throws SaisieInvalideException {
        return (saisieStr("Nom de la vidéo : "));
    }

    public LocalDate saisieDate() throws SaisieInvalideException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date;
        String dateD = saisieStr("Date de parution (jj/mm/aaaa) : ");
        try {
            date = LocalDate.parse(dateD, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Format invalide. Exemple : 22/09/2026");
            return saisieDate();
        }
        if (date.isBefore(LocalDate.of(1946, 1, 1))) {
            System.out.println("La date entrée ne peut pas être trop vieille (entrez une date après 1946).");
            return saisieDate();
        }
        if (date.isAfter(LocalDate.now())) {
            System.out.println("La date entrée ne peut pas être plus tard que la date actuelle.");
            return saisieDate();
        }
        return date;
    }

    public int saisieQuantite() throws SaisieInvalideException {
        return saisieInt("Nombre d'exemplaires : ");

    }

    public String saisieNum() throws SaisieInvalideException {
        return saisieStr("Numéro de la vidéo : ");
    }

    public String saisieType() throws SaisieInvalideException {
        return saisieStr("Type de disque (simple/double) : ");
    }

    public String saisieFormat() throws SaisieInvalideException {
        String ext = saisieStr("Format du fichier (ex: mp3) : ").toLowerCase();
        if (switch (ext) {
            case "mp3", "flac", "wav", "aac" -> true;
            default -> false;
        }) {
            return ext;
        } else {
            throw new SaisieInvalideException("Format de fichier invalide");
        }
    }

    public int saisieDuree() throws SaisieInvalideException {
        return saisieInt("Durée de la vidéo (en minutes) : ");
    }

    public int saisieTaille() throws SaisieInvalideException {
        return saisieInt("Taille du vinyle (en cm) : ");
    }

    public void saisieVideo() throws SaisieInvalideException {
        String nomVideo = saisieNomA();
        String auteur = saisieAuteur();
        LocalDate date = saisieDate();
        int quantite = saisieQuantite();

        String num;

        FichierVideo v = null;

        // GestionVideotheque.creerVideo(v);
    }
}
