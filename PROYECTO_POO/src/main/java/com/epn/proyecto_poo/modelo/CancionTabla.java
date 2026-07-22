package com.epn.proyecto_poo.modelo;

public class CancionTabla {
    private Integer id;
    private String nombreCancion;
    private String artista;
    private String genero;
    private String duracionFormateada;
    private String origen;
    private Cancion cancionRef;
    private CancionPreview previewRef;

    public CancionTabla() {}

    public static CancionTabla desdeCancion(Cancion c) {
        CancionTabla t = new CancionTabla();
        t.id = c.getIdCancion();
        t.nombreCancion = c.getNombreCancion();
        t.artista = c.getArtista();
        t.genero = c.getGenero();
        t.duracionFormateada = c.getDuracionFormateada();
        t.origen = "BD";
        t.cancionRef = c;
        return t;
    }

    public static CancionTabla desdePreview(CancionPreview p) {
        CancionTabla t = new CancionTabla();
        t.id = null;
        t.nombreCancion = p.getTitulo();
        t.artista = p.getArtista();
        t.genero = p.getAlbum() != null ? p.getAlbum() : "";
        t.duracionFormateada = p.getDuracionFormateada();
        t.origen = "Deezer";
        t.previewRef = p;
        return t;
    }

    public Integer getId() { return id; }
    public String getNombreCancion() { return nombreCancion; }
    public String getArtista() { return artista; }
    public String getGenero() { return genero; }
    public String getDuracionFormateada() { return duracionFormateada; }
    public String getOrigen() { return origen; }
    public Cancion getCancionRef() { return cancionRef; }
    public CancionPreview getPreviewRef() { return previewRef; }

    public boolean esDeDeezer() { return "Deezer".equals(origen); }
    public boolean esDeBD() { return "BD".equals(origen); }
}
