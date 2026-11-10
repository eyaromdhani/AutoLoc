package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.IOffreStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {


    private final Map<VehiculeOffre, IOffreStrategy> strategies = new EnumMap<>(VehiculeOffre.class);
    public VehiculeServiceImpl(IVehiculeRepository repository, List<IOffreStrategy> offreStrategies) {
        super(repository);
        for (IOffreStrategy strategy : offreStrategies) {
            strategies.put(strategy.getOffre(), strategy);
        }
    }


    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule v) {
        return v.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule source, Vehicule target) {
        target.setImmatriculation(source.getImmatriculation());
        target.setMarque(source.getMarque());
        target.setModele(source.getModele());
        target.setCategorie(source.getCategorie());
        target.setTarifJournalier(source.getTarifJournalier());
        target.setStatut(source.getStatut());
        target.setAgence(source.getAgence());
        target.setEquipements(source.getEquipements());
    }

    @Override
    public boolean check(Vehicule v, boolean isNew) {
        return v != null
                && Utils.isNotEmpty(v.getImmatriculation())
                && Utils.isNotEmpty(v.getMarque())
                && Utils.isNotEmpty(v.getModele())
                && v.getCategorie() != null
                && v.getTarifJournalier() != null
                && v.getStatut() != null;
    }

    @Override
    public double calculTarif(Vehicule v, int nbjours, VehiculeOffre offre) {
        IOffreStrategy strategy = strategies.get(offre);
        if (strategy == null) {
            throw new IllegalArgumentException("Offre non supportée : " + offre);
        }
        return strategy.calculer(v.getTarifJournalier().doubleValue(), nbjours);
    }
}