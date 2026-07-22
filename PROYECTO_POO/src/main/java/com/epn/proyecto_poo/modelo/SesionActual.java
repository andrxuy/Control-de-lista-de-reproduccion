package com.epn.proyecto_poo.modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class SesionActual {
    private static String usuarioActual = null;
    private static String rolActual = null;

    public static void setUsuario(String usuario, String rol) {
        usuarioActual = usuario;
        rolActual = rol;
    }

    public static String getUsuario() {
        if (usuarioActual == null) {
            cargarDesdeBD();
        }
        return usuarioActual;
    }

    public static String getRol() {
        if (rolActual == null) {
            cargarDesdeBD();
        }
        return rolActual;
    }

    private static void cargarDesdeBD() {
        try {
            EntityManagerFactory emf = JPAUtil.getEMF();
            EntityManager em = emf.createEntityManager();
            String jpql = "SELECT e FROM enSesion e ORDER BY e.id_sesion DESC";
            TypedQuery<enSesion> query = em.createQuery(jpql, enSesion.class);
            query.setMaxResults(1);
            enSesion ultimaSesion = query.getSingleResult();
            if (ultimaSesion != null) {
                usuarioActual = ultimaSesion.getNombre_usuario();
                rolActual = ultimaSesion.getRol();
            }
            em.close();
        } catch (Exception e) {
            System.out.println("Error al cargar sesion desde BD: " + e.getMessage());
            usuarioActual = "Invitado";
            rolActual = "Invitado";
        }
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        rolActual = null;
    }
}
