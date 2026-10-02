package modele;

import exceptions.ConversionImpossibleException;
import exceptions.LectureImpossibleException;
import exceptions.SaisieInvalideException;

import java.time.LocalDate;
import java.util.List;

public class VideoAvi extends FichierVideo {
    public VideoAvi(String titre, String realisateur, LocalDate dateSortie, int duree, String nomFichier, int taille) {
        super(titre, realisateur, dateSortie, duree, nomFichier, taille);
    }

    public String getSupport() {
        return "AVI";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("MPEG-4", "MP3");
    }
}
