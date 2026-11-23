package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock IReservationRepository repository;
    @InjectMocks ReservationServiceImpl service;
    private Reservation valid() {
        Reservation r=new Reservation(); r.setDateDebut(LocalDate.now());
        r.setDateFin(LocalDate.now().plusDays(3)); r.setStatut(StatutReservation.CONFIRMEE); return r;
    }
    @Test void checkCreateNominal() {
        Reservation r=valid(); assertTrue(service.check(r,true));
        when(repository.save(r)).thenReturn(r); assertSame(r,service.create(r));
    }
    @Test void createErrors() {
        Reservation r=valid(); r.setIdReservation(1L);
        assertThrows(IllegalArgumentException.class,()->service.create(r));
        r.setIdReservation(null); r.setDateFin(null);
        assertThrows(IllegalArgumentException.class,()->service.create(r));
    }
    @Test void readNominalAndError() {
        Reservation r=valid();
        when(repository.findById(1L)).thenReturn(Optional.of(r)); when(repository.findAll()).thenReturn(List.of(r));
        assertSame(r,service.findById(1L)); assertEquals(List.of(r),service.findAll());
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findById(99L));
    }
    @Test void updateNominalAndError() {
        Reservation r=valid(); r.setIdReservation(1L); Reservation u=valid(); u.setStatut(StatutReservation.EN_COURS);
        when(repository.findById(1L)).thenReturn(Optional.of(r)); when(repository.save(r)).thenReturn(r);
        assertSame(r,service.update(1L,u)); assertEquals(StatutReservation.EN_COURS,r.getStatut());
        assertThrows(IllegalArgumentException.class,()->service.update(1L,new Reservation()));
    }
    @Test void deleteNominalAndError() {
        when(repository.existsById(1L)).thenReturn(true); service.deleteById(1L); verify(repository).deleteById(1L);
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.deleteById(99L));
    }
}
