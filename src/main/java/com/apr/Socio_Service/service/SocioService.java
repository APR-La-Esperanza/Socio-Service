package com.apr.Socio_Service.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.apr.Socio_Service.dto.SocioDTO;
import com.apr.Socio_Service.dto.SocioResponseDTO;
import com.apr.Socio_Service.exception.ResourceNotFoundException;
import com.apr.Socio_Service.mapper.SocioMapper;
import com.apr.Socio_Service.model.Socio;
import com.apr.Socio_Service.repository.SocioRepository;

@Service
public class SocioService {

    private final SocioRepository repository;
    private final WebClient webClient;

    public SocioService(SocioRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public List<SocioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(SocioMapper::toResponseDTO)
                .toList();
    }

    public SocioResponseDTO buscarPorId(Long id) {
        Socio socio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        return SocioMapper.toResponseDTO(socio);
    }

    public SocioResponseDTO guardar(SocioDTO dto) {
        if (repository.existsByRut(dto.getRut())) {
            throw new IllegalArgumentException("Ya existe un socio registrado con el RUT: " + dto.getRut());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un socio registrado con el email: " + dto.getEmail());
        }
        if (repository.existsByMedidorNumero(dto.getMedidorNumero())) {
            throw new IllegalArgumentException("Ya existe un socio registrado con el número de medidor: " + dto.getMedidorNumero());
        }
        if (repository.existsByCredencialId(dto.getCredencialId())) {
            throw new IllegalArgumentException("La credencial ID ya está asignada a otro socio");
        }

        // Validar que la credencialId exista en Auth_Service usando WebClient
        validarCredencialEnAuthService(dto.getCredencialId());

        Socio socio = SocioMapper.toEntity(dto);
        Socio guardado = repository.save(socio);
        return SocioMapper.toResponseDTO(guardado);
    }

    public SocioResponseDTO actualizar(Long id, SocioDTO dto) {
        Socio socio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        // Validar duplicados exceptuando al socio actual
        repository.findByRut(dto.getRut()).ifPresent(s -> {
            if (!s.getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe otro socio con el RUT: " + dto.getRut());
            }
        });

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            repository.findAll().stream()
                .filter(s -> dto.getEmail().equalsIgnoreCase(s.getEmail()) && !s.getId().equals(id))
                .findAny()
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya existe otro socio con el email: " + dto.getEmail());
                });
        }

        repository.findAll().stream()
            .filter(s -> dto.getMedidorNumero().equals(s.getMedidorNumero()) && !s.getId().equals(id))
            .findAny()
            .ifPresent(s -> {
                throw new IllegalArgumentException("Ya existe otro socio con el número de medidor: " + dto.getMedidorNumero());
            });

        repository.findAll().stream()
            .filter(s -> dto.getCredencialId().equals(s.getCredencialId()) && !s.getId().equals(id))
            .findAny()
            .ifPresent(s -> {
                throw new IllegalArgumentException("La credencial ID ya está asignada a otro socio");
            });

        // Validar que la credencialId exista en Auth_Service
        if (!socio.getCredencialId().equals(dto.getCredencialId())) {
            validarCredencialEnAuthService(dto.getCredencialId());
        }

        socio.setRut(dto.getRut());
        socio.setNombre(dto.getNombre());
        socio.setApellido(dto.getApellido());
        socio.setDireccion(dto.getDireccion());
        socio.setTelefono(dto.getTelefono());
        socio.setEmail(dto.getEmail());
        socio.setMedidorNumero(dto.getMedidorNumero());
        socio.setCredencialId(dto.getCredencialId());

        Socio actualizado = repository.save(socio);
        return SocioMapper.toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        Socio socio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        repository.delete(socio);
    }

    private void validarCredencialEnAuthService(Long credencialId) {
        try {
            // Obtenemos el request actual para extraer la cabecera Authorization (JWT)
            String authHeader = null;
            org.springframework.web.context.request.RequestAttributes attributes = 
                    org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (attributes instanceof org.springframework.web.context.request.ServletRequestAttributes) {
                jakarta.servlet.http.HttpServletRequest currentRequest = 
                        ((org.springframework.web.context.request.ServletRequestAttributes) attributes).getRequest();
                authHeader = currentRequest.getHeader("Authorization");
            }

            final String tokenHeader = authHeader;

            Boolean existe = webClient.get()
                    .uri("/auth/" + credencialId)
                    .headers(headers -> {
                        if (tokenHeader != null && !tokenHeader.isBlank()) {
                            headers.set("Authorization", tokenHeader);
                        }
                    })
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().is2xxSuccessful())
                    .onErrorReturn(false)
                    .block();

            if (existe == null || !existe) {
                throw new IllegalArgumentException("La credencial ID " + credencialId + " no existe en el sistema de autenticación.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al comunicarse con Auth-Service: " + e.getMessage());
        }
    }
}
