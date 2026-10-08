package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Employe;

public interface IClientRepository extends JpaRepository<Client, Long> {
}