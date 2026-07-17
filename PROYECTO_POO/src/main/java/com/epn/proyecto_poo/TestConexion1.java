package com.epn.proyecto_poo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestConexion1 {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("aplicacion");
            EntityManager em = emf.createEntityManager();
            System.out.println("✅ Conexión exitosa a la base de datos");
            em.close();
            emf.close();
        } catch (Exception e) {
            System.out.println("❌ Error al conectar:");
            e.printStackTrace();
        }
    }
}