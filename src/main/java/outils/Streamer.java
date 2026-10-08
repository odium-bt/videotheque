package outils;

import exceptions.StreamingException;
import modele.FichierVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Streamer {
    private final String urlServeur; // ex. "rtsp://192.168.1.50:8554"
    private volatile Process processus; // ffmpeg en cours (partagé entre threads)
    private String fluxEnCours; // Nom du chemin diffusé, ex. "film"
    boolean estEnPause;

    public Streamer(String urlServeur) {
        this.urlServeur = urlServeur;
    }

    /**
     * ffmpeg -re [-stream_loop -1] -i fichier <options du format> <sortie>
     */
    public void diffuserFichier(FichierVideo video, String nomFlux, boolean boucle) throws StreamingException, IOException {
        if (estEnCours()) {
            throw new StreamingException("Une diffusion est déjà en cours !");
        }
        this.fluxEnCours = nomFlux;

        List<String> commande = new ArrayList<>();

        commande.add("ffmpeg");
        commande.add("-re");
        if (boucle) {
            commande.add("-stream_loop");
            commande.add("-1");
        }
        commande.add("-i");
        commande.add(video.getFichier().getPath());//getAbsolutePath()
        commande.addAll(video.getOptionsStreaming());
        commande.addAll(optionsSortie(nomFlux));

        lancer(commande, nomFlux);
    }

    /**
     * ffmpeg <entrée caméra selon le système> <encodage direct> <sortie>
     */
    public void diffuserCamera(String nomFlux)
            throws StreamingException, IOException {
        if (estEnCours()) {
            throw new StreamingException("Une diffusion est déjà en cours !");
        }
        this.fluxEnCours = nomFlux;

        List<String> commande = new ArrayList<>();

        commande.add("ffmpeg");
        commande.addAll(optionsCamera());
        commande.addAll(List.of(
                "-video_size", "1280x720",
                "-framerate", "30",
                "-i", "video=HP TrueVision HD Camera:audio=Réseau de microphones (Technologie Intel® Smart Sound pour microphones numériques)",
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-tune", "zerolatency",
                "-pix_fmt", "yuv420p",
                "-g", "30",
                "-b:v", "2000k",
                "-maxrate", "2000k",
                "-bufsize", "2000k",
                "-c:a", "aac",
                "-b:a", "128k",
                "-ar", "44100"
        ));
        commande.addAll(optionsSortie(nomFlux));

        lancer(commande, nomFlux);
    }

    /**
     * Arrête proprement ffmpeg : envoie "q" sur son entrée standard,
     * attend 5 s au maximum, sinon destroy().
     */
    public void arreter() throws IOException, InterruptedException {
        if (estEnCours()) {
            processus.getOutputStream().write("q\n".getBytes());
            processus.getOutputStream().flush();
            boolean hasEnded = processus.waitFor(5, TimeUnit.SECONDS);
            if (!hasEnded) {
                processus.destroy();
            }
        }
    }

    public boolean estEnCours() {
        return processus != null && processus.isAlive();
    }

    /**
     * URL à donner aux spectateurs, ex. rtsp://.../film
     */
    public String getUrlLecture() {
        return urlServeur + "/" + fluxEnCours;
    }

    /**
     * -f rtsp -rtsp_transport tcp rtsp://serveur:8554/nomFlux
     */
    private List<String> optionsSortie(String nomFlux) {

        return List.of("-f", "rtsp",
                "-rtsp_transport", "tcp",
                this.urlServeur + "/" + nomFlux);

    }

    /**
     * Entrée caméra : dshow, v4l2 ou avfoundation selon os.name
     */
    private List<String> optionsCamera() {
        String os = System.getProperty("os.name");
        os = os.split(" ")[0].toLowerCase();
        return switch (os) {
            case "windows" -> List.of("-f", "dshow", "-rtbufsize", "100M");
            case "linux" -> List.of("-f", "v4l2", "-framerate", "30");
            case "mac" -> List.of("-f", "avfoundation", "-framerate", "30");
            default -> throw new UnsupportedOperationException();
        };
    }

    /**
     * Lance ffmpeg et lit sa sortie dans un thread daemon.
     */
    private void lancer(List<String> commande, String nomFlux)
            throws StreamingException, IOException {

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        this.processus = pb.start();

    }
}