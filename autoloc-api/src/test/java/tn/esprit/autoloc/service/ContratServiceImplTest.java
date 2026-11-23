package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTest {
    @Mock IContratRepository repository;
    @InjectMocks ContratServiceImpl service;

    private Contrat valid() {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now()); c.setMontantTotal(new BigDecimal("500"));
        return c;
    }
    @Test void checkCreateNominal() {
        Contrat c = valid();
        assertTrue(service.check(c, true));
        when(repository.save(c)).thenReturn(c);
        assertSame(c, service.create(c));
    }
    @Test void createErrors() {
        Contrat c = valid(); c.setIdContrat(1L);
        assertThrows(IllegalArgumentException.class, () -> service.create(c));
        c.setIdContrat(null); c.setMontantTotal(null);
        assertThrows(IllegalArgumentException.class, () -> service.create(c));
    }
    @Test void findNominal() {
        Contrat c = valid();
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.findAll()).thenReturn(List.of(c));
        assertSame(c, service.findById(1L));
        assertEquals(List.of(c), service.findAll());
    }
    @Test void findError() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Contrat c = valid(); c.setIdContrat(1L);
        Contrat u = valid(); u.setMontantTotal(new BigDecimal("700"));
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.save(c)).thenReturn(c);
        assertSame(c, service.update(1L, u));
        assertEquals(new BigDecimal("700"), c.getMontantTotal());
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, new Contrat()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
