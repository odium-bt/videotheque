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

import static util.Utils.streamer;


public class Controller {
    static Scanner scan = new Scanner(System.in);
    private final Videotheque videotheque = new Videotheque();

    /**
     * Affiche le menu principal
     */
    public int afficherMenu() {
        while (true) {
            System.out.println("========== VIDÉOTHÈQUE ==========");
            System.out.println("1. Ajouter une vidéo");
            System.out.println("2. Lister toutes les vidéos");
            System.out.println("3. Rechercher une vidéo");
            System.out.println("4. Supprimer une vidéo");
            System.out.println("5. Lire une vidéo");
            System.out.println("6. Convertir une vidéo");
            System.out.println("7. Diffuser une vidéo en streaming");
            System.out.println("8. Diffuser la caméra du laptop");
            System.out.println("9. Arrêter la diffusion");
            System.out.println("0. Quitter");
            System.out.println("=================================");
            System.out.print("Votre choix : ");
            try {
                int choix = scan.nextInt();
                scan.nextLine(); // Vidage du buffer
                return choix;
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer le numéro d'une des options données.");
                scan.nextLine();
            }
        }
    }

    /**
     * Demande à l'utilisateur de saisir une chaine de caractère
     *
     * @param msg le message affiché à l'utilisateur
     * @return String s la saisie de l'utilisateur
     * @throws SaisieInvalideException Jette une exception si l'utilisateur ne rentre rien
     */
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
        while (true) {
            System.out.print(msg);
            try {
                try {
                    s = scan.nextInt();
                    scan.nextLine();
                } catch (InputMismatchException e) {
                    scan.nextLine();
                    throw new SaisieInvalideException("Donnée invalide, veuillez entrer un chiffre.");
                }
                if (s < 0) {
                    throw new SaisieInvalideException("La donnée entrée est invalide !");
                }
                return s;
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private double saisieTailleD() throws SaisieInvalideException {
        double s;
        while (true) {
            System.out.print("Taille du fichier (en Mo) : ");
            try {
                try {
                    s = scan.nextDouble();
                    scan.nextLine();
                } catch (InputMismatchException e) {
                    scan.nextLine();
                    throw new SaisieInvalideException("Merci d'entrer une taille valide (exemple : 34.5)");
                }
                if (s <= 0) {
                    throw new SaisieInvalideException("La taille du fichier doit être supérieure à 0.");
                }
                return s;
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public LocalDate saisieDate() throws SaisieInvalideException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date;
        while (true) {
            try {
                String dateD = saisieStr("Date de sortie (jj/mm/aaaa) : ");
                try {
                    date = LocalDate.parse(dateD, formatter);
                } catch (DateTimeParseException e) {
                    throw new SaisieInvalideException("Format invalide. Exemple : 22/09/2026");
                }
                if (date.isBefore(LocalDate.of(1946, 1, 1))) {
                    throw new SaisieInvalideException("La date entrée ne peut pas être trop vieille (entrez une date après 1946).");
                }
                if (date.isAfter(LocalDate.now())) {
                    throw new SaisieInvalideException("La date entrée ne peut pas être plus tard que la date actuelle.");
                }
                return date;
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int saisieDuree() throws SaisieInvalideException {
        return saisieInt("Durée (minutes) : ");
    }


    public int saisieSupport() throws SaisieInvalideException {
        while (true) {
            int s = saisieInt("Support (1 = DVD, 2 = fichier MP4, 3 = fichier AVI) : ");

            if (s >= 1 && s <= 3) {
                return s;
            }

            System.out.println("Veuillez choisir une des options proposées.");
        }
    }

    public String saisieFormat(String msg) {
        while (true) {
            try {
                String f = saisieStr(msg).toLowerCase();
                if (!f.equals("mp4") && !f.equals("avi")) {
                    throw new SaisieInvalideException("Format entré invalide.");
                }
                return f;
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String saisieFlux() {
        while (true) {
            try {
                String n = saisieStr("Nom du flux : ");

                if (!n.matches("[A-Za-z0-9_-]+")) {
                    throw new SaisieInvalideException("Le nom ne doit contenir que des lettres, des chiffres, '-' ou '_'.");
                }

                return n;
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void ajouterVideo() throws SaisieInvalideException, VideoDejaExistanteException {
        int support = saisieSupport();
        String titre = saisieStr("Titre : ");
        String auteur = saisieStr("Réalisateur : ");
        LocalDate date = saisieDate();
        int duree = saisieDuree();

        // Spécifique aux fichiers
        String chemin;
        double taille;

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
                taille = saisieTailleD();
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

    public void rechercherVideo() throws
            VideoIntrouvableException, VideothequeVideException, SaisieInvalideException {
        String titre = saisieStr("Titre de la vidéo : ");
        Video v = videotheque.rechercherVideo(titre);
        System.out.println(v.toString());
    }

    public void convertirVideo() throws
            SaisieInvalideException, VideothequeVideException, VideoIntrouvableException, IOException, InterruptedException {
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

    public void supprimerVideo() throws
            VideoIntrouvableException, VideothequeVideException, SaisieInvalideException {
        String titre = saisieStr("Titre de la vidéo : ");
        videotheque.supprimerVideo(titre);
    }

    public void diffuserVideo() throws
            VideothequeVideException, VideoIntrouvableException, SaisieInvalideException, IOException, StreamingException {

        String n = saisieStr("Titre de la vidéo : ");
        Video v = videotheque.rechercherVideo(n);
        String nomFlux;
        boolean boucle;

        if (!(v instanceof FichierVideo)) System.out.println("La vidéo doit-être un fichier numérique !");
        else {
            nomFlux = saisieFlux();
            boucle = saisieStr("Activer le bouclage ? (y/n) : ").equals("y");
            streamer.diffuserFichier((FichierVideo) v, nomFlux, boucle);
            System.out.println(">> Diffusion lancée à : " + streamer.getUrlLecture() + " <<");
        }
    }

    public void diffuserCamera() throws SaisieInvalideException, IOException {
        String nomFlux = saisieFlux();
        streamer.diffuserCamera(nomFlux);
        System.out.println(">> Diffusion de la webcam lancée à : " + streamer.getUrlLecture() + " <<");
    }

    public void arreterDiffusion() throws IOException, InterruptedException {
        if (streamer.estEnCours()) streamer.arreter();
        else System.out.println("Aucun flux en cours de diffusion");
    }
}
