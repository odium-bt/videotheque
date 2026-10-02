package application;

import exceptions.*;
import modele.*;
import modele.FichierVideo;

import modele.Videotheque;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    static Scanner scan = new Scanner(System.in);
    private final Videotheque videotheque = new Videotheque();

    /**
     * Affiche le menu principal
     */
    public int afficherMenu() {
        System.out.println("========== VIDÉOTHÈQUE ==========");
        System.out.println("1. Ajouter une vidéo");
        System.out.println("2. Lister toutes les vidéos");
        System.out.println("3. Rechercher une vidéo");
        System.out.println("4. Supprimer une vidéo");
        System.out.println("5. Lire une vidéo");
        System.out.println("6. Convertir une vidéo");
        System.out.println("0. Quitter");
        System.out.println("=================================");
        System.out.print("Votre choix : ");
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

    public LocalDate saisieDate() throws SaisieInvalideException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date;
        String dateD = saisieStr("Date de sortie (jj/mm/aaaa) : ");
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

    public String saisieNum() throws SaisieInvalideException {
        return saisieStr("Numéro de la vidéo : ");
    }

    public int saisieDuree() throws SaisieInvalideException {
        return saisieInt("Durée (minutes) : ");
    }


    public int saisieSupport() throws SaisieInvalideException {
        int s = saisieInt("Support (1 = DVD, 2 = fichier MP4, 3 = fichier AVI) : ");
        if (s >= 1 && s <= 3) {
            return s;
        } else {
            throw new SaisieInvalideException("Veuillez choisir une des options proposées.");
        }
    }

    public String saisieFormat(String msg) throws SaisieInvalideException {
        String f = saisieStr(msg).toLowerCase();
        if (!f.equals("mp4") && !f.equals("avi")) {
            System.out.println("Format entré invalide.");
            f = saisieFormat(msg);
        }
        return f;
    }

    public void ajouterVideo() throws SaisieInvalideException, VideoDejaExistanteException {
        int support = saisieSupport();
        String titre = saisieStr("Titre : ");
        String auteur = saisieStr("Réalisateur : ");
        LocalDate date = saisieDate();
        int duree = saisieDuree();

        // Spécifique aux fichiers
        String chemin;
        int taille;

        Video v;
        boolean valid = false;

        switch (support) {
            case 1:
                String numero = saisieStr("Numéro : ");
                int zone = saisieInt("Zone : ");
                v = new Dvd(titre, auteur, date, duree, numero, zone);
                break;
            case 2:
                do {
                    chemin = saisieStr("Nom du fichier (ex : video.mp4) : ");
                    if (new File("media/" + chemin).exists()) {
                        valid = true;
                    } else {
                        System.out.println("Fichier introuvable, veuillez ré-essayer");
                    }
                } while (!valid);
                taille = saisieInt("Taille (Mb) : ");
                v = new VideoMp4(titre, auteur, date, duree, chemin, taille);
                break;
            case 3:
                do {
                    chemin = saisieStr("Nom du fichier (ex : video.aav) : ");
                    if (new File("media/" + chemin).exists()) {
                        valid = true;
                    } else {
                        System.out.println("Fichier introuvable, veuillez ré-essayer");
                    }
                } while (!valid);
                taille = saisieInt("Taille (Mb) : ");
                v = new VideoAvi(titre, auteur, date, duree, chemin, taille);
                break;
            default:
                throw new SaisieInvalideException("Veuillez choisir une des options proposées.");
        }

        videotheque.ajouterVideo(v);
    }

    public void listerVideos() throws VideothequeVideException {
        try {
            videotheque.listerVideos();
        } catch (VideothequeVideException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rechercherVideo() throws VideoIntrouvableException, VideothequeVideException, SaisieInvalideException {
        String titre = saisieStr("Titre de la vidéo : ");
        Video v = videotheque.rechercherVideo(titre);
        System.out.println(v.toString());
    }

    public void convertirVideo() throws SaisieInvalideException, VideothequeVideException, VideoIntrouvableException, IOException, InterruptedException {
        String titre = saisieStr("Titre de la vidéo à convertir : ");
        String format = saisieFormat("Format cible (MP4, AVI) : ");

        Video v = videotheque.rechercherVideo(titre);
        if (v instanceof FichierVideo) {
            v.convertir(format);
        } else {
            throw new ConversionImpossibleException("La vidéo choisie n'est pas dans un format convertible");
        }
    }

    public void lireVideo() throws SaisieInvalideException, VideothequeVideException, VideoIntrouvableException {
        videotheque.lireVideo(saisieStr("Titre : "));
    }

    public void supprimerVideo() throws VideoIntrouvableException, VideothequeVideException, SaisieInvalideException {
        String titre = saisieStr("Titre de la vidéo : ");
        videotheque.supprimerVideo(titre);
    }
}
