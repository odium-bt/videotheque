package modele;

import java.time.LocalDate;
import java.util.List;

public class VideoAvi extends FichierVideo {
    public VideoAvi(String titre, String realisateur, LocalDate dateSortie, int duree, String nomFichier, double taille) {
        super(titre, realisateur, dateSortie, duree, nomFichier, taille);
    }

    public String getSupport() {
        return "AVI";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("MPEG-4", "MP3");
    }

    @Override
    protected List<String> optionsStreaming() {
        return List.of("-c:v", "libx264", "-preset veryfast", "-tune zerolatency", "-pix_fmt",
                "yuv420p", "-g 50", "-b:v 2500k", "-maxrate 2500k", "-bufsize 5000k", "-c:a aac", "-b:a 128k", "-ar 44100");
    }
}
