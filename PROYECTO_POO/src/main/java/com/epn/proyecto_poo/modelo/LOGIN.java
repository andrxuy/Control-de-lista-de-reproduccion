package com.epn.proyecto_poo.modelo;

public interface LOGIN {
    public Usuario buscarUsuario(String nombreUsuario, String rol);
    public void Insertar(Usuario u);
}
