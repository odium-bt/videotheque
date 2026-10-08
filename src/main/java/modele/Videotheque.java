package modele;

import exceptions.*;

import java.io.IOException;
import java.util.ArrayList;

public class Videotheque implements GestionVideotheque {
    private final ArrayList<Video> videos = new ArrayList<>();

    public Videotheque() {
    }

    @Override
    public void ajouterVideo(Video v)
            throws VideoDejaExistanteException, SaisieInvalideException {
        for (Video video : videos) {
            if (video.getTitre().equals(v.getTitre())) {
                throw new VideoDejaExistanteException("Cette vidéo existe déjà");
            }
        }
        videos.add(v);
    }

    @Override
    public void listerVideos() throws VideothequeVideException {
        if (videos.isEmpty()) {
            throw new VideothequeVideException("La vidéothèque est vide");
        }
        int c = 1;
        for (Video video : videos) {
            System.out.println("=== Vidéo " + c + " ===");
            System.out.println("Titre : " + video.getTitre());
            System.out.println("Réalisateur : " + video.getRealisateur());
            System.out.println("Date de sortie : " + video.getDateSortie());
            System.out.println("Durée : " + video.getDuree());
            if (video instanceof Dvd) {
                System.out.println("Numéro : " + ((Dvd) video).getNumero());
                System.out.println("Zone : " + ((Dvd) video).getNumero());
            } else if (video instanceof FichierVideo) {
                System.out.println("Chemin : media/" + ((FichierVideo) video).getNomFichier());
                System.out.println("Taille : " + ((FichierVideo) video).getTaille());
            }
            System.out.println();
            c++;
        }
    }

    @Override
    public Video rechercherVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException {
        if (videos.isEmpty()) {
            throw new VideothequeVideException("La vidéothèque est vide");
        }
        for (Video video : videos) {
            if (video.getTitre().equals(titre)) {
                return video;
            }
        }
        throw new VideoIntrouvableException("Vidéo introuvable");
    }

    @Override
    public void supprimerVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException {
        Video video = rechercherVideo(titre);
        videos.remove(video);
    }

    @Override
    public void lireVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException,
            LectureImpossibleException {
        Video video = rechercherVideo(titre);
        if (video instanceof FichierVideo) {
            video.lire();
        } else {
            throw new LectureImpossibleException("Cette vidéo n'est pas un fichier");
        }
    }

    @Override
    public Video convertirVideo(String titre, String formatCible)
            throws VideoIntrouvableException, VideothequeVideException,
            ConversionImpossibleException, SaisieInvalideException, IOException, InterruptedException {
        Video v = rechercherVideo(titre);
        if (v == null) {
            throw new ConversionImpossibleException("Conversion impossible");
        }
        FichierVideo fv = v.convertir(formatCible);
        int index = videos.indexOf(v);
        videos.set(index, fv);
        return fv;
    }
}
