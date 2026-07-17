package com.epn.proyecto_poo.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "ensesion")
public class enSesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sesion;
    @Column(name = "nombre_usuario", nullable = false, length = 255)
    private String nombre_usuario;
    @Column(name = "rol", nullable = false, length = 255)
    private String rol;

    public enSesion() {}

    public enSesion(String nombre_usuario, String rol) {
        this.nombre_usuario = nombre_usuario;
        this.rol = rol;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "enSesion{" +
                "nombre_usuario='" + nombre_usuario + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
