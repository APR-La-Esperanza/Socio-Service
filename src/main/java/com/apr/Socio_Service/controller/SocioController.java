package com.apr.Socio_Service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.apr.Socio_Service.dto.SocioDTO;
import com.apr.Socio_Service.dto.SocioResponseDTO;
import com.apr.Socio_Service.service.SocioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/socios")
@Tag(name = "Socios", description = "Endpoints para la gestión y consulta del padrón de socios de la APR.")
@SecurityRequirement(name = "bearerAuth")
public class SocioController {

    private final SocioService service;

    public SocioController(SocioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los socios", description = "Retorna una lista completa de los socios inscritos en el sistema APR.")
    @ApiResponse(responseCode = "200", description = "Socios obtenidos correctamente.")
    public ResponseEntity<List<SocioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar socio por ID", description = "Obtiene los detalles del socio correspondiente al identificador numérico.")
    @ApiResponse(responseCode = "200", description = "Socio encontrado y devuelto.")
    @ApiResponse(responseCode = "404", description = "El socio con el ID provisto no existe.")
    public ResponseEntity<SocioResponseDTO> buscarPorId(
            @Parameter(description = "Identificador del socio", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo socio", description = "Agrega un socio al padrón. Valida que el identificador de credencial exista en Auth-Service.")
    @ApiResponse(responseCode = "201", description = "Socio registrado exitosamente.")
    @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o problemas de validación de credencial.")
    public ResponseEntity<SocioResponseDTO> guardar(
            @RequestBody(description = "Datos del nuevo socio a registrar", required = true,
                         content = @Content(schema = @Schema(implementation = SocioDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"rut\": \"12345678-9\",\n  \"nombre\": \"Carmen\",\n  \"apellido\": \"Perez\",\n  \"direccion\": \"Camino Central 45, Sector La Esperanza\",\n  \"telefono\": \"+56987654321\",\n  \"email\": \"carmen@apr.cl\",\n  \"medidorNumero\": \"MED-10045\",\n  \"activo\": true,\n  \"credencialId\": 1\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody SocioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", description = "Modifica los datos del socio identificado por el ID.")
    @ApiResponse(responseCode = "200", description = "Socio actualizado exitosamente.")
    @ApiResponse(responseCode = "400", description = "Cuerpo de la petición inválido o inconsistente.")
    @ApiResponse(responseCode = "404", description = "El socio indicado no existe.")
    public ResponseEntity<SocioResponseDTO> actualizar(
            @Parameter(description = "ID del socio a actualizar", required = true) @PathVariable Long id,
            @RequestBody(description = "Nuevos datos del socio", required = true,
                         content = @Content(schema = @Schema(implementation = SocioDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"rut\": \"12345678-9\",\n  \"nombre\": \"Carmen\",\n  \"apellido\": \"Perez Gomez\",\n  \"direccion\": \"Camino Central 45, Sector La Esperanza\",\n  \"telefono\": \"+56987654321\",\n  \"email\": \"carmen@apr.cl\",\n  \"medidorNumero\": \"MED-10045\",\n  \"activo\": true,\n  \"credencialId\": 1\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody SocioDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar socio", description = "Da de baja permanentemente al socio del sistema.")
    @ApiResponse(responseCode = "204", description = "Socio eliminado con éxito.")
    @ApiResponse(responseCode = "404", description = "El socio no existe.")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del socio a eliminar", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
