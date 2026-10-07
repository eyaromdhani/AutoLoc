package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.domain.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AgenceTest {

    @Autowired
    private AgenceRepositoryMock agenceRepository;

    @Test
    public void addAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("123 TUN 4567");
        v1.setMarque("Renault");
        v1.setModele("Clio");
        v1.setCategorie(CategorieVehicule.ECONOMIQUE);
        v1.setTarifJournalier(new BigDecimal("80"));
        v1.setStatut(StatutVehicule.DISPONIBLE);
        v1.setAgence(agence);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("234 TUN 8910");
        v2.setMarque("Peugeot");
        v2.setModele("3008");
        v2.setCategorie(CategorieVehicule.SUV);
        v2.setTarifJournalier(new BigDecimal("150"));
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setAgence(agence);

        agence.getVehicules().add(v1);
        agence.getVehicules().add(v2);

        agenceRepository.save(agence);
    }

    @Test
    public void loadAgence() {
        //question:12
        List<Agence> agences = (List<Agence>) agenceRepository.findAll();

        //StringBuilder : qui sert à construire une chaîne de caractères morceau par morceau.
        // Question :13
        StringBuilder sb = new StringBuilder();
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

            // Question 14 :
            Assertions.fail(sb.toString());
        }
    }
}
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}