package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenceServiceImplTest {
    @Mock IAgenceRepository repository;
    @Mock IVehiculeService vehiculeService;
    @Mock IEmployeService employeService;
    @InjectMocks AgenceServiceImpl service;

    private Agence valid() {
        Agence a=new Agence(); a.setNom("Agence Tunis"); a.setTelephone("71111111");
        a.setVille("Tunis"); a.setAdresse("Rue Centrale"); return a;
    }
    @Test void checkCreateNominal() {
        Agence a=valid(); assertTrue(service.check(a,true));
        when(repository.save(a)).thenReturn(a); assertSame(a,service.create(a));
    }
    @Test void createErrors() {
        Agence a=valid(); a.setIdAgence(1L);
        assertThrows(IllegalArgumentException.class,()->service.create(a));
        a.setIdAgence(null); a.setNom("");
        assertThrows(IllegalArgumentException.class,()->service.create(a));
    }
    @Test void readNominalAndError() {
        Agence a=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(a)); when(repository.findAll()).thenReturn(List.of(a));
        assertSame(a,service.findById(1L)); assertEquals(List.of(a),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Agence a=valid(); a.setIdAgence(1L); Agence u=valid(); u.setNom("Nouvelle agence");
        when(repository.findById(1L)).thenReturn(Optional.of(a)); when(repository.save(a)).thenReturn(a);
        assertSame(a,service.update(1L,u)); assertEquals("Nouvelle agence",a.getNom());
        Agence invalid = new Agence();
        assertThrows(IllegalArgumentException.class,()->service.update(1L,invalid));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true); service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.deleteById(99L));
    }
    @Test void hasVehiculesNominalAndError() {
        Agence a=valid(); a.getVehicules().add(new Vehicule());
        when(repository.findById(1L)).thenReturn(Optional.of(a));
        assertTrue(service.hasVehicules(1L));
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.hasVehicules(99L));
    }
    @Test void updateAgenceAvecEmployesNominal() {
        Agence a=valid(); a.setIdAgence(1L); Employe e=new Employe(); e.setNom("Ali");
        when(repository.findById(1L)).thenReturn(Optional.of(a));
        when(repository.save(a)).thenReturn(a);
        when(employeService.create(e)).thenReturn(e);
        service.updateAgenceAvecEmployes(a,List.of(e));
        assertEquals(1,a.getEffectif());
        assertSame(a,e.getAgence());
        verify(employeService).create(e);
        verify(repository,times(2)).save(a);
    }
    @Test void simulerNominal() {
        Agence a=valid(); a.setIdAgence(1L); Vehicule v=new Vehicule();
        a.getVehicules().add(v);
        when(vehiculeService.calculTarif(v,3,VehiculeOffre.STANDARD)).thenReturn(240.0);
        String rapport=service.simuler(a,3,VehiculeOffre.STANDARD);
        assertTrue(rapport.contains("Total : 240.0"));
    }
}
