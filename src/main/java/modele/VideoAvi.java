package modele;

import exceptions.ConversionImpossibleException;
import exceptions.SaisieInvalideException;

import java.time.LocalDate;
import java.util.List;

public class VideoAvi extends FichierVideo {
    public VideoAvi(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin, int taille) {
        super(titre, realisateur, dateSortie, duree, chemin, taille);
    }

    public String getSupport() {
        return "AVI";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("MPEG-4", "MP3");
    }

    @Override
    public FichierVideo convertir(String formatCible) throws ConversionImpossibleException, SaisieInvalideException {
        return null;
    }


}
