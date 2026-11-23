package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.IOffreStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {
    @Mock IVehiculeRepository repository;
    @Mock IAgenceRepository agenceRepository;
    @Mock IEquipementRepository equipementRepository;
    @Mock IOffreStrategy strategy;
    @InjectMocks VehiculeServiceImpl service;

    private Vehicule valid() {
        Vehicule v=new Vehicule(); v.setImmatriculation("123TUN"); v.setMarque("Renault");
        v.setModele("Clio"); v.setCategorie(CategorieVehicule.ECONOMIQUE);
        v.setTarifJournalier(new BigDecimal("80")); v.setStatut(StatutVehicule.DISPONIBLE); return v;
    }
    @BeforeEach void setupStrategy() {
        when(strategy.getOffre()).thenReturn(VehiculeOffre.STANDARD);
        service = new VehiculeServiceImpl(repository,List.of(strategy),agenceRepository,equipementRepository);
    }
    @Test void checkCreateNominal() {
        Vehicule v=valid(); assertTrue(service.check(v,true));
        when(repository.save(v)).thenReturn(v); assertSame(v,service.create(v));
    }
    @Test void createErrors() {
        Vehicule v=valid(); v.setIdVehicule(1L);
        assertThrows(IllegalArgumentException.class,()->service.create(v));
        v.setIdVehicule(null); v.setMarque("");
        assertThrows(IllegalArgumentException.class,()->service.create(v));
    }
    @Test void readNominalAndError() {
        Vehicule v=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(v)); when(repository.findAll()).thenReturn(List.of(v));
        assertSame(v,service.findById(1L)); assertEquals(List.of(v),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Vehicule v=valid(); v.setIdVehicule(1L); Vehicule u=valid(); u.setMarque("Peugeot");
        when(repository.findById(1L)).thenReturn(Optional.of(v)); when(repository.save(v)).thenReturn(v);
        assertSame(v,service.update(1L,u)); assertEquals("Peugeot",v.getMarque());
        Vehicule invalid = new Vehicule();
        assertThrows(IllegalArgumentException.class,()->service.update(1L,invalid));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true); service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.deleteById(99L));
    }
    @Test void calculTarifNominalAndUnsupportedOffer() {
        Vehicule v=valid(); when(strategy.calculer(80.0,5)).thenReturn(400.0);
        assertEquals(400.0,service.calculTarif(v,5,VehiculeOffre.STANDARD));
        assertThrows(IllegalArgumentException.class,()->service.calculTarif(v,5,VehiculeOffre.WEEKEND));
    }
    @Test void desaffecterNominalAndError() {
        Vehicule v=valid(); v.setAgence(new Agence());
        when(repository.findById(1L)).thenReturn(Optional.of(v)); when(repository.save(v)).thenReturn(v);
        assertSame(v,service.desaffecter(1L)); assertNull(v.getAgence());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.desaffecter(99L));
    }
    @Test void affecterNominalAndError() {
        Vehicule v=valid(); Agence a=new Agence();
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(agenceRepository.findById(2L)).thenReturn(Optional.of(a)); when(repository.save(v)).thenReturn(v);
        assertSame(v,service.affecter(1L,2L)); assertSame(a,v.getAgence());
        when(agenceRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,()->service.affecter(1L,99L));
    }
    @Test void affecterEquipementNominalAndError() {
        Vehicule v=valid(); Equipement e=new Equipement(); e.setLibelle("GPS");
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(equipementRepository.findById(2L)).thenReturn(Optional.of(e)); when(repository.save(v)).thenReturn(v);
        assertSame(v,service.affecterEquipement(1L,2L)); assertTrue(v.getEquipements().contains(e));
        when(equipementRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,()->service.affecterEquipement(1L,99L));
    }
}
