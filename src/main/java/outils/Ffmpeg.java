package outils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Ffmpeg {
    private Ffmpeg() {
    } // classe utilitaire : aucune instance

    public static int lire(File fichier, String titreFenetre) throws IOException, InterruptedException {
        List<String> commande = new ArrayList<>();

        commande.add("ffplay");
        commande.add("-autoexit");
        commande.add("-window_title");
        commande.add(titreFenetre);
        commande.add(fichier.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        Process processus = pb.start();

        return processus.waitFor(); // attend la fin de ffmplay
    }

    /**
     * ffmpeg -y -i entree [options] sortie
     * Renvoie le code de retour de ffmpeg (0 = succès).
     */
    public static int convertir(File entree, File sortie, List<String> options)
            throws IOException, InterruptedException {
        List<String> commande = new ArrayList<>();

        commande.add("ffmpeg");
        commande.add("-y");
        commande.add("-loglevel");
        commande.add("error"); // n'affiche que les erreurs
        commande.add("-i");
        commande.add(entree.getAbsolutePath());
        commande.addAll(options);
        commande.add(sortie.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(commande);
        pb.redirectErrorStream(true); // stderr fusionné dans stdout
        Process processus = pb.start();
        // TODO : lire la sortie du processus ligne par ligne
        // (BufferedReader) et l'afficher
        return processus.waitFor(); // attend la fin de ffmpeg
    }
}