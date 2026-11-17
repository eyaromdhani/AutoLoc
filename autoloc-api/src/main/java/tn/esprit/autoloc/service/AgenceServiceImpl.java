
package tn.esprit.autoloc.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.io.RapportConsole;

import java.util.List;

@RapportConsole
@Primary
@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long>
        implements IAgenceService, ISimulationAgenceService {

    private final IVehiculeService vehiculeService;
    private final IEmployeService employeService;

    public AgenceServiceImpl(
            IAgenceRepository agenceRepository,
            IVehiculeService vehiculeService,
            IEmployeService employeService) {

        super(agenceRepository);
        this.vehiculeService = vehiculeService;
        this.employeService = employeService;
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence agence) {
        return agence.getIdAgence();
    }

    @Override
    protected void setFields(Agence source, Agence target) {
        target.setNom(source.getNom());
        target.setVille(source.getVille());
        target.setAdresse(source.getAdresse());
        target.setTelephone(source.getTelephone());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom())
                && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return !findById(id).getVehicules().isEmpty();
    }

    @Override
    @Transactional
    public void updateAgenceAvecEmployes(Agence agence, List<Employe> employes) {

        update(agence.getIdAgence(), agence);
        for (Employe e : employes) {
            agence.addEmploye(e);
            employeService.create(e);
        }

        agence.setEffectif(employes.size());

        update(agence.getIdAgence(), agence);    }

    public String simuler(Agence agence, int nbjours, VehiculeOffre offre) {
        StringBuilder rapport = new StringBuilder();
        rapport.append("Id : " + agence.getIdAgence()
                + "Nom : " + agence.getNom()
                + "Telephone : " + agence.getTelephone() + "\n");

        double total = 0;

        for (Vehicule vehicule : agence.getVehicules()) {
            total += vehiculeService.calculTarif(vehicule, nbjours, offre);
        }

        rapport.append("Total : " + total + "\n");
        return rapport.toString();
    }
}
