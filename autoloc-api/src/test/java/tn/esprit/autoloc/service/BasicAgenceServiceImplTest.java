package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAgenceServiceImplTest {
    @Mock IAgenceRepository repository;
    @InjectMocks BasicAgenceServiceImpl service;
    private Agence valid() {
        Agence a=new Agence(); a.setNom("Centre"); a.setTelephone("71123456"); a.setVille("Tunis"); a.setAdresse("Rue 1"); return a;
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
        assertThrows(EntityNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Agence a=valid(); a.setIdAgence(1L); Agence u=valid(); u.setNom("Nouveau");
        when(repository.findById(1L)).thenReturn(Optional.of(a)); when(repository.save(a)).thenReturn(a);
        assertSame(a,service.update(1L,u)); assertEquals("Nouveau",a.getNom());
        Agence invalid = new Agence();
        assertThrows(IllegalArgumentException.class,()->service.update(1L,invalid));
    }
    @Test void hasVehiculesAndDelete() {
        Agence a=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(a));
        assertFalse(service.hasVehicules(1L));
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class,()->service.deleteById(99L));
    }
    @Test void updateWithEmployeesIsUnsupported() {
        Agence agence = valid();
        List<Employe> employes = List.of(new Employe());
        assertThrows(UnsupportedOperationException.class,
            ()->service.updateAgenceAvecEmployes(agence,employes));
    }
}
