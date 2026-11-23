package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock IClientRepository repository;
    @InjectMocks ClientServiceImpl service;

    private Client valid() {
        Client c = new Client();
        c.setNom("Ben Ali"); c.setPrenom("Sara"); c.setEmail("sara@test.tn");
        c.setNumPermis("P123"); c.setDateInscription(LocalDate.now());
        return c;
    }

    @Test void checkAndCreateNominal() {
        Client c = valid();
        assertTrue(service.check(c, true));
        when(repository.save(c)).thenReturn(c);
        assertSame(c, service.create(c));
    }
    @Test void createErrors() {
        Client c = valid(); c.setIdClient(1L);
        assertThrows(IllegalArgumentException.class, () -> service.create(c));
        c.setIdClient(null); c.setEmail("");
        assertThrows(IllegalArgumentException.class, () -> service.create(c));
    }
    @Test void findAllAndFindByIdNominal() {
        Client c = valid();
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.findAll()).thenReturn(List.of(c));
        assertSame(c, service.findById(1L));
        assertEquals(List.of(c), service.findAll());
    }
    @Test void findByIdError() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Client c = valid(); c.setIdClient(1L);
        Client updated = valid(); updated.setPrenom("Nadia");
        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.save(c)).thenReturn(c);
        assertSame(c, service.update(1L, updated));
        assertEquals("Nadia", c.getPrenom());
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, new Client()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteById(1L);
        verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
