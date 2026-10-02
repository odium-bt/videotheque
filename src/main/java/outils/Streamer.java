package outils;

import exceptions.SaisieInvalideException;
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

    public Streamer(String urlServeur) {
        this.urlServeur = urlServeur;
    }

    /**
     * ffmpeg -re [-stream_loop -1] -i fichier <options du format> <sortie>
     */
    public void diffuserFichier(FichierVideo video, String nomFlux, boolean boucle)
            throws StreamingException, SaisieInvalideException, IOException {
        if (!nomFlux.matches("[A-Za-z0-9_-]+")) {
            throw new SaisieInvalideException("Le nom ne doit contenir que des lettres, des chiffres, - ou _");
        }
        if (estEnCours()) {
            throw new StreamingException("Une diffusion est déjà en cours !");
        }
        List<String> commande = new ArrayList<>();

        commande.add("ffmpeg");
        commande.add("-re");
        if (boucle) {
            commande.add("-stream_loop");
            commande.add("-1");
        }
        commande.add("-i");
        commande.add(video.getFichier().getAbsolutePath());
        commande.addAll(video.getOptionsStreaming());
        commande.add("-f");
        commande.add("rtsp");
        commande.add("-rtsp_transport");
        commande.add("tcp");
        commande.add(urlServeur + "/" + nomFlux);

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        this.processus = pb.start();
    }

    /**
     * ffmpeg <entrée caméra selon le système> <encodage direct> <sortie>
     */
    public void diffuserCamera(String nomFlux)
            throws StreamingException, SaisieInvalideException { /* TODO */ }

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
    public String getUrlLecture() { /* TODO */
        return "";
    }

    /**
     * -f rtsp -rtsp_transport tcp rtsp://serveur:8554/nomFlux
     */
    private List<String> optionsSortie(String nomFlux) { /* TODO */
        return List.of();
    }

    /**
     * Entrée caméra : dshow, v4l2 ou avfoundation selon os.name
     */
    private List<String> optionsCamera() { /* TODO */
        return List.of();
    }

    /**
     * Lance ffmpeg et lit sa sortie dans un thread daemon.
     */
    private void lancer(List<String> commande, String nomFlux)
            throws StreamingException {
        Thread thread = new Thread();

    }
}