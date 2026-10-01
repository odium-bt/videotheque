package modele;

import exceptions.*;

import java.io.File;
import java.util.ArrayList;

public class Videothequee implements GestionVideotheque {
    private ArrayList<Video> videos = new ArrayList<>();

    public Videothequee(ArrayList<Video> videos) {
        this.videos = new ArrayList<>(videos);
    }

    @Override
    public void ajouterVideo(Video v)
            throws VideoDejaExistanteException, SaisieInvalideException {
        for (Video video : videos) {
            if (video.getTitre().equals(v.getTitre())) {
                throw new VideoDejaExistanteException("Cette vidéo existe déjà");
            }
        }
        if (v instanceof FichierVideo) {
            FichierVideo f = (FichierVideo) v;
            File fichier = new File(f.getChemin());
            if (!fichier.exists()) {
                throw new SaisieInvalideException("Le fichier n'existe pas");
            }
        }
        videos.add(v);
    }

    @Override
    public void listerVideos() throws VideothequeVideException {
        if (videos.isEmpty()) {
            throw new VideothequeVideException("La vidéothèque est vide");
        }
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    @Override
    public Video rechercherVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException {
        if (videos.isEmpty()) {
            throw new VideothequeVideException("La vidéothèque est vide");
        }
        for (Video video : videos) {
            if (video.getTitre().equals(titre)){
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
        video.lire();
    }

    @Override
    public Video convertirVideo(String titre, String formatCible)
            throws VideoIntrouvableException, VideothequeVideException,
            ConversionImpossibleException, SaisieInvalideException {
        Video v = rechercherVideo(titre);
        if (!(v instanceof Convertible)) {
            throw new ConversionImpossibleException("Conversion impossible");
        }
        Convertible c = (Convertible) v;
        FichierVideo fv = c.convertir(formatCible);
        int index = videos.indexOf(v);
        videos.set(index, fv);
        return fv;
    }
}
