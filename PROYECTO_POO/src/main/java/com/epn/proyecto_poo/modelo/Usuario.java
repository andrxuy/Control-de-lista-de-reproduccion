package com.epn.proyecto_poo.modelo;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_usuario;
    @Column(nullable = false, length = 255)
    private String nombre_usuario   ;
    @Column(name = "password_hash", nullable = false)
    private String password_hash;
    @Column(nullable = false, length = 255)
    private String rol;

    public Usuario() {}

    public Usuario(Integer ID_usuario, String nombre_usuario, String password_hash, String rol) {
        this.ID_usuario = ID_usuario;
        this.nombre_usuario = nombre_usuario;
        this.password_hash = password_hash;
        this.rol = rol;
    }

    public Usuario(String nombre_usuario, String password_hash, String rol) {
        this.nombre_usuario = nombre_usuario;
        this.password_hash = password_hash;
        this.rol = rol;
    }

    public Integer getID_usuario() {
        return ID_usuario;
    }

    public void setID_usuario(Integer ID_usuario) {
        this.ID_usuario = ID_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getPassword_hash() {return password_hash;}

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID_usuario=" + ID_usuario +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
