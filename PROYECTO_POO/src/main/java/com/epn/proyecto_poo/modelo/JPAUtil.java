package com.epn.proyecto_poo.modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class JPAUtil {
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEMF() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("aplicacion");
            System.out.println("[JPAUtil] Conectando a: " + emf.getProperties().get("jakarta.persistence.jdbc.url"));
            System.out.println("[JPAUtil] Usuario: " + emf.getProperties().get("jakarta.persistence.jdbc.user"));
            verificarEsquema();
        }
        return emf;
    }

    public static synchronized void resetEMF() {
        if (emf != null && emf.isOpen()) {
            emf.getCache().evictAll();
            emf.close();
        }
        emf = null;
        System.out.println("[JPAUtil] EntityManagerFactory reseteado");
        getEMF();
    }

    @SuppressWarnings("unchecked")
    private static void verificarEsquema() {
        EntityManager em = emf.createEntityManager();
        try {
            String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'canciones'";
            List<String> columnas = em.createNativeQuery(sql).getResultList();
            System.out.println("[JPAUtil] Columnas en 'canciones': " + columnas);
            System.out.println("[JPAUtil] Total columnas: " + columnas.size());
        } catch (Exception e) {
            System.err.println("[JPAUtil] No se pudo verificar esquema: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
