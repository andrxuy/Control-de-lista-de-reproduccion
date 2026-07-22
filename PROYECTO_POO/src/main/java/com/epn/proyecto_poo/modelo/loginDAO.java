package com.epn.proyecto_poo.modelo;

import jakarta.persistence.*;

public class loginDAO implements LOGIN{
    private EntityManager em;
    public loginDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Usuario buscarUsuario(String nombreUsuario, String rol) {
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.nombre_usuario = :nombreUsuario AND u.rol = :rol";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario);
            query.setParameter("rol", rol);
            Usuario resultado = query.getSingleResult();
            System.out.println("buscarUsuario OK => " + resultado);
            return resultado;
        } catch (NoResultException e) {
            System.out.println("buscarUsuario => No se encontro usuario con nombre='" + nombreUsuario + "' y rol='" + rol + "'");
            return null;
        }
    }

    Seguridad seguridad = new Seguridad();

    @Override
    public void Insertar(Usuario u) {
        try {
            System.out.println("Insertar => nombre: '" + u.getNombre_usuario() + "', rol: '" + u.getRol() + "'");
            u.setPassword_hash(seguridad.generarHash(u.getPassword_hash()));
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            System.out.println("Usuario insertado! ID=" + u.getID_usuario() + ", nombre=" + u.getNombre_usuario() + ", rol=" + u.getRol());
            InsertarSesion(new enSesion(u.getNombre_usuario(), u.getRol()));
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al insertar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void InsertarSesion(enSesion u) {
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            System.out.println("Sesion insertada!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }
}
