package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;

@Component
public class StandardStrategy implements IOffreStrategy {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.STANDARD;
    }

    @Override
    public double calculer(double tarifJournalier, int nbJours) {
        return tarifJournalier * nbJours;
    }
}