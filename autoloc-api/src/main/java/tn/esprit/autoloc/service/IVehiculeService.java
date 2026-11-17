package tn.esprit.autoloc.service;

import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService extends IGenericService<Vehicule, Long>{
    double calculTarif(Vehicule v , int nbjours, VehiculeOffre offre);
    Vehicule desaffecter(long id);
    Vehicule affecter(long id, long idAgence);
    Vehicule affecterEquipement(long id, long idEquipement);

}
