package modele;

import java.util.List;

public class VideoMp4 extends FichierVideo {
    public String getSupport() {
        return "MP4";
    }

    public List<String> optionsEncodage() {
        return List.of("H.264", "AAC");
    }
}
