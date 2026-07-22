package com.epn.proyecto_poo.modelo;

public class CancionPreview {
    private String deezerId;
    private String titulo;
    private String artista;
    private String album;
    private String previewUrl;
    private String portadaUrl;
    private int duracionMs;

    public CancionPreview() {}

    public CancionPreview(String deezerId, String titulo, String artista, String album,
                          String previewUrl, String portadaUrl, int duracionMs) {
        this.deezerId = deezerId;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.previewUrl = previewUrl;
        this.portadaUrl = portadaUrl;
        this.duracionMs = duracionMs;
    }

    public String getDeezerId() { return deezerId; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getAlbum() { return album; }
    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }
    public String getPortadaUrl() { return portadaUrl; }
    public int getDuracionMs() { return duracionMs; }

    public String getDuracionFormateada() {
        int totalSeg = duracionMs / 1000;
        int min = totalSeg / 60;
        int seg = totalSeg % 60;
        return String.format("%d:%02d", min, seg);
    }

    public int getDuracionSegundos() { return duracionMs / 1000; }

    public Cancion aCancion() {
        Cancion c = new Cancion();
        c.setNombreCancion(this.titulo);
        c.setDuracion(Cancion.fromSegundos(this.getDuracionSegundos()));
        c.setGenero("Deezer");
        c.setArtista(this.artista);
        c.setPreviewUrl(this.previewUrl);
        return c;
    }
}
