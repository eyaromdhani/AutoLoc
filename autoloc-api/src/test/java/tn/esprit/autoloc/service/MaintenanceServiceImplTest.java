package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTest {
    @Mock IMaintenanceRepository repository;
    @InjectMocks MaintenanceServiceImpl service;
    private Maintenance valid() { Maintenance m=new Maintenance(); m.setDateDebut(LocalDate.now()); return m; }
    @Test void checkCreateNominal() {
        Maintenance m=valid(); assertTrue(service.check(m,true));
        when(repository.save(m)).thenReturn(m); assertSame(m,service.create(m));
    }
    @Test void createErrors() {
        Maintenance m=valid(); m.setIdMaintenance(1L);
        assertThrows(IllegalArgumentException.class,()->service.create(m));
        m.setIdMaintenance(null); m.setDateDebut(null);
        assertThrows(IllegalArgumentException.class,()->service.create(m));
    }
    @Test void readNominalAndError() {
        Maintenance m=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(m)); when(repository.findAll()).thenReturn(List.of(m));
        assertSame(m,service.findById(1L)); assertEquals(List.of(m),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Maintenance m=valid(); m.setIdMaintenance(1L); Maintenance u=valid();
        u.setDateFin(LocalDate.now().plusDays(2)); u.setDescription("Révision"); u.setVehicule(new Vehicule());
        when(repository.findById(1L)).thenReturn(Optional.of(m)); when(repository.save(m)).thenReturn(m);
        assertSame(m,service.update(1L,u)); assertEquals("Révision",m.getDescription());
        assertThrows(IllegalArgumentException.class,()->service.update(1L,new Maintenance()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true); service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.deleteById(99L));
    }
}
