package tn.esprit.autoloc.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;

import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAgenceServiceImpl implements IAgenceService  {
    private final IAgenceRepository agenceRepository;

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) &&
                Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if(agence.getIdAgence() !=  null)
        {
            throw new IllegalArgumentException("L'agence ne doit pas avoir d'identifiant lors de la création.");
        }

        if (!check(agence, true)) {
            throw new IllegalArgumentException("L'agence n'est pas valide.");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        //return agenceRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Agence", id.toString()));
        return agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec l'id : " + id));
    }

    @Override
    public List<Agence> findAll() {
        return List.of();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);

        if (!check(agence, false)) {
            throw new IllegalArgumentException("Les données de l'agence sont invalides");
        }

        existing.setNom(agence.getNom());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        existing.setVehicules(agence.getVehicules());

        return agenceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new EntityNotFoundException("Agence introuvable avec l'id : " + id);
        }
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }
}