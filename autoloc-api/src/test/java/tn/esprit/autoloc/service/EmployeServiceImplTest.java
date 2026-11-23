package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceImplTest {
    @Mock IEmployeRepository repository;
    @InjectMocks EmployeServiceImpl service;
    private Employe valid() {
        Employe e = new Employe(); e.setNom("Trabelsi"); e.setPrenom("Ali"); e.setRole(RoleEmploye.AGENT);
        return e;
    }
    @Test void checkCreateNominal() {
        Employe e = valid(); assertTrue(service.check(e, true));
        when(repository.save(e)).thenReturn(e); assertSame(e, service.create(e));
    }
    @Test void createErrors() {
        Employe e = valid(); e.setIdEmploye(1L);
        assertThrows(IllegalArgumentException.class, () -> service.create(e));
        e.setIdEmploye(null); e.setRole(null);
        assertThrows(IllegalArgumentException.class, () -> service.create(e));
    }
    @Test void readNominalAndError() {
        Employe e = valid();
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(repository.findAll()).thenReturn(List.of(e));
        assertSame(e, service.findById(1L)); assertEquals(List.of(e), service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Employe e = valid(); e.setIdEmploye(1L);
        Employe u = valid(); u.setPrenom("Omar"); Agence a = new Agence(); u.setAgence(a);
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(repository.save(e)).thenReturn(e);
        assertSame(e, service.update(1L,u));
        assertEquals("Omar", e.getPrenom()); assertSame(a,e.getAgence());
        assertThrows(IllegalArgumentException.class, () -> service.update(1L,new Employe()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
