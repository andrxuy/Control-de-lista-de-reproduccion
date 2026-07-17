package com.epn.proyecto_poo.modelo;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("aplicacion");

    public static EntityManagerFactory getEMF() {
        return emf;
    }
}
