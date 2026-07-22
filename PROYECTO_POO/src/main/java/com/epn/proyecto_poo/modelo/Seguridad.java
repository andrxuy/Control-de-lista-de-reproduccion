package com.epn.proyecto_poo.modelo;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {
    public String generarHash(String password_plana){
        String cifrado = BCrypt.hashpw(password_plana, BCrypt.gensalt());
        return cifrado;
    }

    public boolean validarHash(String password_plana, String password_phash){
        return BCrypt.checkpw(password_plana, password_phash);
    }
    public static void main(String[] args) {
        Seguridad seguridad = new Seguridad();
        System.out.println(seguridad.generarHash("123456"));

    }
}
