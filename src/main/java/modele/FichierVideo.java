package modele;

import exceptions.ConversionImpossibleException;
import exceptions.LectureImpossibleException;
import exceptions.SaisieInvalideException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import outils.Ffmpeg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public abstract class FichierVideo extends Video implements Convertible, Runnable {
    private String chemin;
    private int taille;

    public FichierVideo(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin, int taille) {
        super(titre, realisateur, dateSortie, duree);
        this.chemin = chemin;
        this.taille = taille;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public String getFormat() {
        String cheminMinuscule = chemin.toLowerCase();
        return cheminMinuscule.substring(cheminMinuscule.lastIndexOf(".") + 1);
    }

    public File getFichier() {
        return new File(chemin);
    }

    public void lire() {
        File file = new File(chemin);
        if (!file.exists()) {
            throw new LectureImpossibleException("Le fichier spécifié est introuvable.");
        }

        Thread thread = new Thread(this, "Lecteur-" + titre);

        thread.setDaemon(true);

        System.out.println("Début de lecture : " + thread.getName());
        thread.start();
    }

    public void run() {
        try (FileInputStream flux = new FileInputStream(this.getFichier())) {
            Player player = new Player(flux);
            player.play();
        } catch (IOException | JavaLayerException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        System.out.println("Fin de lecture : " + Thread.currentThread().getName());
    }

    public FichierVideo convertir(String formatCible) throws SaisieInvalideException, IOException, InterruptedException {
        if (formatCible.equals(this.getFormat())) {
            throw new ConversionImpossibleException("Le fichier est déjà au format " + formatCible + ".");
        }

        File original = this.getFichier();

        String nomSansExtension = original.getName().substring(0, original.getName().lastIndexOf("."));
        String nouveauChemin = nomSansExtension + "." + formatCible;

        File converti = new File(original.getParent(), nouveauChemin);

        List<String> options = switch (formatCible) {
            case "mp4" -> List.of(
                    "-c:v", "libx264",
                    "-preset", "fast",
                    "-crf", "23",
                    "-c:a", "aac",
                    "-b:a", "160k"
            );
            case "avi" -> List.of(
                    "-c:v", "mpeg4",
                    "-q:v", "5",
                    "-c:a", "libmp3lame",
                    "-b:a", "192k"
            );
            default -> throw new SaisieInvalideException("Format non supporté : " + formatCible);
        };

        System.out.println("Conversion en cours...");

        if (0 != Ffmpeg.convertir(original, converti, options)) {
            throw new IOException("FFmpeg a échoué");
        }

        this.setChemin(nouveauChemin);

        System.out.println("Conversion terminée : " + converti.getPath());

        return switch (formatCible) {
            case "mp4" ->
                    new VideoMp4(this.getTitre(), this.getRealisateur(), this.getDateSortie(), this.getDuree(), nouveauChemin, this.getTaille());
            case "avi" ->
                    new VideoAvi(this.getTitre(), this.getRealisateur(), this.getDateSortie(), this.getDuree(), nouveauChemin, this.getTaille());
            default -> null;
        };
    }

    protected abstract List<String> optionsEncodage();
}


