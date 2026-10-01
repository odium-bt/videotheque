package modele;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public abstract class FichierVideo extends Video implements Convertible {
    private String chemin;
    private int taille;

    public FichierVideo(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin, int taille) {
        super(titre, realisateur, dateSortie, duree);
        this.chemin = chemin;
        this.taille = taille;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public File getFichier() {
        return new File(chemin);
    }

    public void lire() {
    }

    ;

    public void convertir() {
    }

    ;

    protected abstract List<String> optionsEncodage();
}
