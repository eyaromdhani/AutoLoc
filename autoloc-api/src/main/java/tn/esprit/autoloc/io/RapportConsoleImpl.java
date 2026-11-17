package tn.esprit.autoloc.io;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RapportConsoleImpl implements IRapport {

    @Override
    public void afficherEntete(String entete) {
        log.info("{}", entete);
    }

    @Override
    public double retournerTotal(double total) {
        return total;
    }
}