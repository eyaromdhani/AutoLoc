package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.domain.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import tn.esprit.autoloc.repository.IAgenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AgenceTest {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    private void addAgence(CrudRepository<Agence, Long> repository) {

        int suffix = (int) System.currentTimeMillis();

        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("123 TUN " + suffix);
        v1.setMarque("Renault");
        v1.setModele("Clio");
        v1.setCategorie(CategorieVehicule.ECONOMIQUE);
        v1.setTarifJournalier(new BigDecimal("80"));
        v1.setStatut(StatutVehicule.DISPONIBLE);
        v1.setAgence(agence);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("234 TUN " + suffix);
        v2.setMarque("Peugeot");
        v2.setModele("3008");
        v2.setCategorie(CategorieVehicule.SUV);
        v2.setTarifJournalier(new BigDecimal("150"));
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setAgence(agence);

        agence.getVehicules().add(v1);
        agence.getVehicules().add(v2);

        repository.save(agence);
    }

    private void loadAgence(CrudRepository<Agence, Long> repository, String type) {

        Iterable<Agence> agences = repository.findAll();
        //question:12
        //List<Agence> agences = (List<Agence>) basicAgenceRepository.findAll();

        //StringBuilder : qui sert à construire une chaîne de caractères morceau par morceau.
        // Question :13
        StringBuilder sb = new StringBuilder();
        sb.append("\nRepository type : ").append(type);

        for (Agence agence : agences) {
            sb.append("\n")
                    .append(agence.getIdAgence())
                    .append(" | ")
                    .append(agence.getNom())
                    .append("\nVehicules Count : ")
                    .append(agence.getVehicules().size());

            for (Vehicule v : agence.getVehicules()) {
                sb.append("\n")
                        .append(v.getIdVehicule())
                        .append("|")
                        .append(v.getImmatriculation());
            }
            sb.append("\n");


        }
        // Question 14 :
        Assertions.fail(sb.toString());
    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }
    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "Basic (CrudRepository)");
    }
    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "Full (JpaRepository)");
    }

    @Test
    public void loadSortedAgences() {
        List<Agence> agences = fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "idAgence"));

        StringBuilder sb = new StringBuilder();
        sb.append("\nSorted by id DESC");

        for (Agence agence : agences) {
            sb.append("\n")
                    .append(agence.getIdAgence())
                    .append(" | ")
                    .append(agence.getNom())
                    .append(" | ")
                    .append(agence.getVille());
        }

        Assertions.fail(sb.toString());
    }

    @Test
    public void loadPagedAgences() {
        int taille = 2;
        Sort tri = Sort.by(Sort.Direction.DESC, "idAgence");

        Page<Agence> premiere = fullAgenceRepository.findAll(PageRequest.of(0, taille, tri));

        StringBuilder sb = new StringBuilder();
        sb.append("Agences triées par id décroissant, paginées par ").append(taille).append("\n");
        sb.append("Nombre total de pages : ").append(premiere.getTotalPages()).append("\n\n");

        for (int i = 0; i < premiere.getTotalPages(); i++) {
            Page<Agence> page = fullAgenceRepository.findAll(PageRequest.of(i, taille, tri));

            sb.append("--- Page en cours : ").append(page.getNumber()).append(" ---\n");
            for (Agence a : page.getContent()) {
                sb.append("Agence ID : ").append(a.getIdAgence())
                        .append(" | Nom : ").append(a.getNom())
                        .append(" | Ville : ").append(a.getVille())
                        .append("\n");
            }
            sb.append("\n");
        }
        Assertions.fail(sb.toString());
    }
}

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}