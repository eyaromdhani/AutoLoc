package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    @ManyToOne
    private Vehicule vehicule;

    @ManyToOne
    private Client client;

    @OneToOne
    @JoinColumn(name = "id_contrat")
    private Contrat contrat;
    //@JoinColumn(name = "id_contrat") sets the name of the foreign key column in reservation.
    // Without it, Hibernate would name it contrat_id_contrat.
}