package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@Tag(name = "Equipements", description = "Gestion des équipements")
@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;

    @Operation(summary = "Lister les équipements")
    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @Operation(summary = "Consulter un équipement")

            @ApiResponse(responseCode = "200", description = "Équipement trouvé")
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")

    @GetMapping("/{id}")
    public Equipement findById(
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable Long id) {
        return equipementService.findById(id);
    }

    @Operation(summary = "Créer un équipement",
            description = "Le corps ne doit pas contenir d'identifiant.")

            @ApiResponse(responseCode = "201", description = "Équipement créé")
            @ApiResponse(responseCode = "400", description = "Données invalides")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipement create(@RequestBody Equipement equipement) {
        return equipementService.create(equipement);
    }

    @Operation(summary = "Supprimer un équipement")

            @ApiResponse(responseCode = "204", description = "Équipement supprimé")
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable Long id) {
        equipementService.deleteById(id);
    }
}