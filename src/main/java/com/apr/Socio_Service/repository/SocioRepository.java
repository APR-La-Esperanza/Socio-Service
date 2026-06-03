package com.apr.Socio_Service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apr.Socio_Service.model.Socio;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByRut(String rut);
    
    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    boolean existsByMedidorNumero(String medidorNumero);

    boolean existsByCredencialId(Long credencialId);
}
