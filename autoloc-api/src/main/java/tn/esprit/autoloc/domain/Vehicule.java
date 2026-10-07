package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @Column(nullable = false, length = 50)
    private String marque;

    @Column(nullable = false, length = 50)
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategorieVehicule categorie;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVehicule statut;

    @ManyToOne
    private Agence agence;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Reservation> reservations = new HashSet<>();
    //mappedBy = "vehicule" is the name of the attribute you just wrote in Reservation (A1).
    // It tells Hibernate that Reservation owns the foreign key, so don't create another one.

    @ManyToMany
    @JoinTable(name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "id_vehicule"),
            inverseJoinColumns = @JoinColumn(name = "id_equipement"))
    private Set<Equipement> equipements = new HashSet<>();
    //name = "vehicule_equipement" is the name of the join table.
    //joinColumns is the column pointing to this entity (Vehicule).
    //inverseJoinColumns is the column pointing to the other entity (Equipement).

}