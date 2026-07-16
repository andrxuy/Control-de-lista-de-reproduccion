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
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void Insertar(Usuario u) {
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            System.out.println("Usuario insertado!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }
}
