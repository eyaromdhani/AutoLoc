package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaiementServiceImplTest {
    @Mock IPaiementRepository repository;
    @InjectMocks PaiementServiceImpl service;
    private Paiement valid() {
        Paiement p=new Paiement(); p.setMontant(new BigDecimal("250"));
        p.setDatePaiement(LocalDate.now()); p.setModePaiement(ModePaiement.CARTE_BANCAIRE); return p;
    }
    @Test void checkNominalAndError() {
        assertTrue(service.check(valid(),true));
        assertFalse(service.check(new Paiement(),true));
    }
    @Test void readNominalAndError() {
        Paiement p=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(p)); when(repository.findAll()).thenReturn(List.of(p));
        assertSame(p,service.findById(1L)); assertEquals(List.of(p),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void createIsUnsupported() {
        assertThrows(UnsupportedOperationException.class,()->service.create(valid()));
        verifyNoInteractions(repository);
    }
    @Test void updateIsUnsupported() {
        assertThrows(UnsupportedOperationException.class,()->service.update(1L,valid()));
        verifyNoInteractions(repository);
    }
    @Test void deleteIsUnsupported() {
        assertThrows(UnsupportedOperationException.class,()->service.deleteById(1L));
        verifyNoInteractions(repository);
    }
}
