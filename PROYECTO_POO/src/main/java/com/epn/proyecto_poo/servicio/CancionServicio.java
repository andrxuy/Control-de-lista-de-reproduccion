package com.epn.proyecto_poo.servicio;

import com.epn.proyecto_poo.modelo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class CancionServicio {
    private CancionDAO cancionDAO;

    public CancionServicio() {
        EntityManagerFactory emf = JPAUtil.getEMF();
        EntityManager em = emf.createEntityManager();
        this.cancionDAO = new CancionDAO(em);
    }

    public List<Cancion> obtenerTodas() { return cancionDAO.obtenerTodas(); }

    public Cancion obtenerPorId(Integer id) { return cancionDAO.obtenerPorId(id); }

    public boolean existeCancion(String nombre, String artista) {
        return cancionDAO.buscarPorNombreYArtista(nombre, artista) != null;
    }

    public void guardarSiNoExiste(Cancion cancion) {
        if (existeCancion(cancion.getNombreCancion(), cancion.getArtista())) {
            throw new IllegalArgumentException(
                    "La cancion '" + cancion.getNombreCancion() + "' de '" + cancion.getArtista() + "' ya existe en la BD");
        }
        cancionDAO.insertar(cancion);
    }

    public List<Cancion> buscar(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) return cancionDAO.obtenerTodas();
        return cancionDAO.buscar(filtro.trim());
    }

    public List<Cancion> filtrarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) return cancionDAO.obtenerTodas();
        return cancionDAO.filtrarPorGenero(genero);
    }

    public void guardar(Cancion cancion) throws IllegalArgumentException {
        validar(cancion);
        if (cancion.getIdCancion() == null) {
            cancionDAO.insertar(cancion);
        } else {
            cancionDAO.actualizar(cancion);
        }
    }

    public void eliminar(Integer id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");
        cancionDAO.eliminar(id);
    }

    public List<String> obtenerGeneros() { return cancionDAO.obtenerGeneros(); }

    private void validar(Cancion cancion) {
        if (cancion.getNombreCancion() == null || cancion.getNombreCancion().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la cancion es obligatorio");
        }
    }
}
