package com.apr.Socio_Service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SocioDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]{1}$", message = "El RUT debe tener un formato válido (ej: 12345678-9)")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    private String direccion;
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El número de medidor es obligatorio")
    private String medidorNumero;

    @NotNull(message = "El ID de credencial es obligatorio")
    private Long credencialId;

    public SocioDTO() {
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedidorNumero() {
        return medidorNumero;
    }

    public void setMedidorNumero(String medidorNumero) {
        this.medidorNumero = medidorNumero;
    }

    public Long getCredencialId() {
        return credencialId;
    }

    public void setCredencialId(Long credencialId) {
        this.credencialId = credencialId;
    }
}
