package com.epn.proyecto_poo.modelo;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "canciones")
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancion")
    private Integer idCancion;

    @Column(name = "nombre_cancion", length = 255)
    private String nombreCancion;

    @Column(name = "duracion")
    private LocalTime duracion;

    @Column(name = "genero", length = 255)
    private String genero;

    @Column(name = "artista", length = 255)
    private String artista;

    @Transient private String previewUrl;

    public Cancion() {}

    public Cancion(Integer idCancion, String nombreCancion, LocalTime duracion, String genero, String artista) {
        this.idCancion = idCancion;
        this.nombreCancion = nombreCancion;
        this.duracion = duracion;
        this.genero = genero;
        this.artista = artista;
    }

    public Cancion(String nombreCancion, LocalTime duracion, String genero, String artista) {
        this.nombreCancion = nombreCancion;
        this.duracion = duracion;
        this.genero = genero;
        this.artista = artista;
    }

    public Integer getIdCancion() { return idCancion; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    public String getNombreCancion() { return nombreCancion; }
    public void setNombreCancion(String nombreCancion) { this.nombreCancion = nombreCancion; }
    public LocalTime getDuracion() { return duracion; }
    public void setDuracion(LocalTime duracion) { this.duracion = duracion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }
    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }

    public String getDuracionFormateada() {
        if (duracion == null) return "0:00";
        int totalMin = duracion.getHour() * 60 + duracion.getMinute();
        return String.format("%d:%02d", totalMin, duracion.getSecond());
    }

    public static LocalTime fromSegundos(int totalSeg) {
        int h = totalSeg / 3600;
        int m = (totalSeg % 3600) / 60;
        int s = totalSeg % 60;
        return LocalTime.of(h, m, s);
    }
}
