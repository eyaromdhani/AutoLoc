package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipementServiceImplTest {
    @Mock IEquipementRepository repository;
    @InjectMocks EquipementServiceImpl service;
    private Equipement valid() { Equipement e=new Equipement(); e.setLibelle("GPS"); return e; }
    @Test void checkCreateNominal() {
        Equipement e=valid(); assertTrue(service.check(e,true));
        when(repository.save(e)).thenReturn(e); assertSame(e,service.create(e));
    }
    @Test void createErrors() {
        Equipement e=valid(); e.setIdEquipement(1L);
        assertThrows(IllegalArgumentException.class,()->service.create(e));
        e.setIdEquipement(null); e.setLibelle("");
        assertThrows(IllegalArgumentException.class,()->service.create(e));
    }
    @Test void readNominalAndError() {
        Equipement e=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(e)); when(repository.findAll()).thenReturn(List.of(e));
        assertSame(e,service.findById(1L)); assertEquals(List.of(e),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Equipement e=valid(); e.setIdEquipement(1L); Equipement u=valid(); u.setLibelle("Siège enfant");
        when(repository.findById(1L)).thenReturn(Optional.of(e)); when(repository.save(e)).thenReturn(e);
        assertSame(e,service.update(1L,u)); assertEquals("Siège enfant",e.getLibelle());
        assertThrows(IllegalArgumentException.class,()->service.update(1L,new Equipement()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true); service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.deleteById(99L));
    }
}
