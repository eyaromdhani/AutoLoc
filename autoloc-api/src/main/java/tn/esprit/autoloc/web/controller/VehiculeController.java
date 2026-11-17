package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@Tag(name = "Vehicules", description = "Gestion des véhicules")
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Consulter un véhicule")
    @GetMapping("/{id}")
    public Vehicule findById(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Désaffecter un véhicule",
            description = "Retire l'agence du véhicule. Le véhicule reste en base.")

            @ApiResponse(responseCode = "200", description = "Véhicule désaffecté")
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")


    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @PutMapping("/desaffecter/{id}")
    public Vehicule desaffecter(
            @PathVariable Long id) {
        return vehiculeService.desaffecter(id);
    }

    @PutMapping("/{idVehicule}/affecter/{idAgence}")
    public Vehicule affecter(@PathVariable Long idVehicule,
                             @PathVariable Long idAgence) {
        return vehiculeService.affecter(idVehicule, idAgence);
    }

    @PutMapping("/{idVehicule}/equipements/{idEquipement}")
    public Vehicule affecterEquipement(@PathVariable Long idVehicule,
                                       @PathVariable Long idEquipement) {
        return vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        vehiculeService.deleteById(id);
    }
}