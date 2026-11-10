package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;

@Component
public class WeekendStrategy implements IOffreStrategy {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.WEEKEND;
    }

    @Override
    public double calculer(double tarifJournalier, int nbJours) {
        return tarifJournalier * 0.8 * nbJours;
    }
}