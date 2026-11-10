package tn.esprit.autoloc.io;

public class RapportSilencieux implements IRapport {
    @Override
    public void afficherEntete(String entete) {
        // rien : pas d'affichage console
    }

    @Override
    public double retournerTotal(double total) {
        return total;
    }
}