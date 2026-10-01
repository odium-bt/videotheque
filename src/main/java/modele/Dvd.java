package modele;

import exceptions.ConversionImpossibleException;
import exceptions.SaisieInvalideException;

import java.time.LocalDate;
import java.util.List;

public class Dvd extends Video {
    private String numero;
    private int zone;

    public Dvd(String titre, String realisateur, LocalDate dateSortie, int duree, String numero, int zone) {
        super(titre, realisateur, dateSortie, duree);
        this.numero = numero;
        this.zone = zone;
    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getSupport() {
        return "DVD";
    }

    @Override
    public void lire() {
        System.out.println("Prenez le DVD DVD-001 \"Amélie\" et insérez-le dans un lecteur zone 2.");
    }

    @Override
    protected List<String> optionsEncodage() {
        return List.of();
    }

    @Override
    public FichierVideo convertir(String formatCible) throws ConversionImpossibleException, SaisieInvalideException {
        return null;
    }


}
