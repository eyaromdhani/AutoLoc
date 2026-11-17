package tn.esprit.autoloc;

public final class Utils {

    private Utils() {
        // classe utilitaire : ne pas instancier
    }

    // static permet de l'appeler directement
    // non vide = non null et contient autre chose que des espaces
    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}