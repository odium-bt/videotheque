package modele;

import exceptions.ConversionImpossibleException;
import exceptions.SaisieInvalideException;

public interface Convertible {
    /**
     * Convertit au format "MP4" ou "AVI" et renvoie le nouvel objet.
     * Le fichier d'origine est conservé.
     */
    FichierVideo convertir(String formatCible)
            throws ConversionImpossibleException, SaisieInvalideException;
}
