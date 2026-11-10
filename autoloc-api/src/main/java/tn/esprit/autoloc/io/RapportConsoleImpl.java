package tn.esprit.autoloc.io;

public class RapportConsoleImpl implements IRapport {
    @Override
    public void afficherEntete(String entete) {
        System.out.println(entete);
    }

    @Override
    public double retournerTotal(double total) {
        return total;
    }
}