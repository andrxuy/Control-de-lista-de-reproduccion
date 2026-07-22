package com.epn.proyecto_poo.modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.Time;
import java.util.List;

public class CancionDAO {
    private EntityManager em;

    public CancionDAO(EntityManager em) {
        this.em = em;
    }

    public List<Cancion> obtenerTodas() {
        try {
            String jpql = "SELECT c FROM Cancion c ORDER BY c.idCancion ASC";
            TypedQuery<Cancion> query = em.createQuery(jpql, Cancion.class);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al obtener canciones: " + e.getMessage());
            return List.of();
        }
    }

    public Cancion obtenerPorId(Integer id) {
        try {
            return em.find(Cancion.class, id);
        } catch (Exception e) {
            System.out.println("Error al obtener cancion por ID: " + e.getMessage());
            return null;
        }
    }

    public Cancion buscarPorNombreYArtista(String nombre, String artista) {
        try {
            String jpql = "SELECT c FROM Cancion c WHERE LOWER(c.nombreCancion) = :nombre " +
                    "AND LOWER(c.artista) = :artista";
            TypedQuery<Cancion> query = em.createQuery(jpql, Cancion.class);
            query.setParameter("nombre", nombre.toLowerCase());
            query.setParameter("artista", artista != null ? artista.toLowerCase() : "");
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Cancion> buscar(String filtro) {
        try {
            String jpql = "SELECT c FROM Cancion c WHERE " +
                    "LOWER(c.nombreCancion) LIKE :filtro OR " +
                    "LOWER(c.genero) LIKE :filtro OR " +
                    "LOWER(c.artista) LIKE :filtro ORDER BY c.idCancion ASC";
            TypedQuery<Cancion> query = em.createQuery(jpql, Cancion.class);
            query.setParameter("filtro", "%" + filtro.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al buscar canciones: " + e.getMessage());
            return List.of();
        }
    }

    public List<Cancion> filtrarPorGenero(String genero) {
        try {
            String jpql = "SELECT c FROM Cancion c WHERE c.genero = :genero ORDER BY c.idCancion ASC";
            TypedQuery<Cancion> query = em.createQuery(jpql, Cancion.class);
            query.setParameter("genero", genero);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al filtrar por genero: " + e.getMessage());
            return List.of();
        }
    }

    public void insertar(Cancion cancion) {
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO canciones (nombre_cancion, duracion, genero, artista) VALUES (?, ?, ?, ?)";
            em.createNativeQuery(sql)
                    .setParameter(1, cancion.getNombreCancion())
                    .setParameter(2, cancion.getDuracion() != null ? Time.valueOf(cancion.getDuracion()) : null)
                    .setParameter(3, cancion.getGenero())
                    .setParameter(4, cancion.getArtista())
                    .executeUpdate();
            em.getTransaction().commit();
            System.out.println("Cancion insertada!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al insertar cancion: " + e.getMessage());
            throw e;
        }
    }

    public void actualizar(Cancion cancion) {
        try {
            em.getTransaction().begin();
            String sql = "UPDATE canciones SET nombre_cancion = ?, duracion = ?, genero = ?, artista = ? WHERE id_cancion = ?";
            em.createNativeQuery(sql)
                    .setParameter(1, cancion.getNombreCancion())
                    .setParameter(2, cancion.getDuracion() != null ? Time.valueOf(cancion.getDuracion()) : null)
                    .setParameter(3, cancion.getGenero())
                    .setParameter(4, cancion.getArtista())
                    .setParameter(5, cancion.getIdCancion())
                    .executeUpdate();
            em.getTransaction().commit();
            System.out.println("Cancion actualizada!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al actualizar cancion: " + e.getMessage());
            throw e;
        }
    }

    public void eliminar(Integer id) {
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM canciones WHERE id_cancion = ?";
            em.createNativeQuery(sql).setParameter(1, id).executeUpdate();
            em.getTransaction().commit();
            System.out.println("Cancion eliminada!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al eliminar cancion: " + e.getMessage());
            throw e;
        }
    }

    public List<String> obtenerGeneros() {
        try {
            String jpql = "SELECT DISTINCT c.genero FROM Cancion c WHERE c.genero IS NOT NULL ORDER BY c.genero";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al obtener generos: " + e.getMessage());
            return List.of();
        }
    }
}
