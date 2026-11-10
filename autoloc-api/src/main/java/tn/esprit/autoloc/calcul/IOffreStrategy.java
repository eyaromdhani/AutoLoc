package tn.esprit.autoloc.calcul;

public interface IOffreStrategy {
    VehiculeOffre getOffre();
    double calculer(double tarifJournalier, int nbJours);
}