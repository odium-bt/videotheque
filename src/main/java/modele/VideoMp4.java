package modele;

import exceptions.ConversionImpossibleException;
import exceptions.SaisieInvalideException;

import java.time.LocalDate;
import java.util.List;

public class VideoMp4 extends FichierVideo {
    public VideoMp4(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin, int taille) {
        super(titre, realisateur, dateSortie, duree, chemin, taille);
    }

    public String getSupport() {
        return "MP4";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("H.264", "AAC");
    }
}
