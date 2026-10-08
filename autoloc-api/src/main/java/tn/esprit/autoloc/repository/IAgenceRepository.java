package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Agence;

//How to read JpaRepository<Agence, Long>:
//Agence is the entity this repository manages.
//Long is the type of its id (idAgence is a Long).
public interface IAgenceRepository extends JpaRepository<Agence, Long> {
}