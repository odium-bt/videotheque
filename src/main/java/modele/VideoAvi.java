package modele;

import java.util.List;

public class VideoAvi extends FichierVideo {
    public String getSupport() {
        return "AVI";
    }

    public List<String> optionsEncodage() {
        return List.of("MPEG-4", "MP3");
    }
}
