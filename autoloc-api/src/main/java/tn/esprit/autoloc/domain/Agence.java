package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 50)
    private String ville;

    @Column(nullable = false, length = 200)
    private String adresse;

    @Column(length = 20)
    private String telephone;

    //mappedby dans l'entité qui a cardinalité petite
    //nom de l'attribut dans la classe vehicule
    //fetch = FetchType.EAGER) :  Le chargement d’une agence implique le chargement des véhicules
    //cascade = CascadeType.ALL : L’ajout d’une véhicule implique la présence des ces agences

    @OneToMany(mappedBy = "agence", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Vehicule> vehicules = new HashSet<>();

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    private Set<Employe> employes = new HashSet<>();
}