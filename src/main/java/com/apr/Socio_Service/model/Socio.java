package com.apr.Socio_Service.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    private String direccion;
    private String telefono;

    @Column(unique = true)
    private String email;

    @Column(name = "medidor_numero", unique = true, nullable = false)
    private String medidorNumero;

    private Boolean activo;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "credencial_id", nullable = false)
    private Long credencialId;

    public Socio() {
    }

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getCredencialId() {
        return credencialId;
    }

    public void setCredencialId(Long credencialId) {
        this.credencialId = credencialId;
    }
}
