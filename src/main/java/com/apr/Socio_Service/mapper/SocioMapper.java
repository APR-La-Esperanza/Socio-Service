package com.apr.Socio_Service.mapper;

import com.apr.Socio_Service.dto.SocioDTO;
import com.apr.Socio_Service.dto.SocioResponseDTO;
import com.apr.Socio_Service.model.Socio;

public class SocioMapper {

    public static Socio toEntity(SocioDTO dto) {
        if (dto == null) {
            return null;
        }
        Socio socio = new Socio();
        socio.setRut(dto.getRut());
        socio.setNombre(dto.getNombre());
        socio.setApellido(dto.getApellido());
        socio.setDireccion(dto.getDireccion());
        socio.setTelefono(dto.getTelefono());
        socio.setEmail(dto.getEmail());
        socio.setMedidorNumero(dto.getMedidorNumero());
        socio.setCredencialId(dto.getCredencialId());
        return socio;
    }

    public static SocioResponseDTO toResponseDTO(Socio socio) {
        if (socio == null) {
            return null;
        }
        SocioResponseDTO dto = new SocioResponseDTO();
        dto.setId(socio.getId());
        dto.setRut(socio.getRut());
        dto.setNombre(socio.getNombre());
        dto.setApellido(socio.getApellido());
        dto.setDireccion(socio.getDireccion());
        dto.setTelefono(socio.getTelefono());
        dto.setEmail(socio.getEmail());
        dto.setMedidorNumero(socio.getMedidorNumero());
        dto.setActivo(socio.getActivo());
        dto.setFechaRegistro(socio.getFechaRegistro());
        dto.setCredencialId(socio.getCredencialId());
        return dto;
    }
}
