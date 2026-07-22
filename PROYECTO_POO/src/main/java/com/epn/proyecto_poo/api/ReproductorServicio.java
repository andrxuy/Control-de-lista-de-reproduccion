package com.epn.proyecto_poo.api;

import com.epn.proyecto_poo.modelo.Cancion;
import com.epn.proyecto_poo.modelo.CancionPreview;
import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class ReproductorServicio {
    private MediaPlayer mediaPlayer;

    private static final String[] FALLBACK_URLS = {
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-12.mp3"
    };
    private int fallbackIndex = 0;

    private final StringProperty tituloActual = new SimpleStringProperty("");
    private final StringProperty artistaActual = new SimpleStringProperty("");
    private final StringProperty mensajeEstado = new SimpleStringProperty("");
    private final ObjectProperty<Duration> tiempoActual = new SimpleObjectProperty<>(Duration.ZERO);
    private final ObjectProperty<Duration> duracionTotal = new SimpleObjectProperty<>(Duration.ZERO);
    private final DoubleProperty volumen = new SimpleDoubleProperty(0.7);
    private final BooleanProperty reproduciendo = new SimpleBooleanProperty(false);
    private final BooleanProperty pausado = new SimpleBooleanProperty(false);

    public void reproducir(CancionPreview cancion) {
        if (cancion == null) return;
        detener();

        tituloActual.set(cancion.getTitulo());
        artistaActual.set(cancion.getArtista());

        String url = cancion.getPreviewUrl();
        boolean usandoFallback = false;

        if (url == null || url.isEmpty()) {
            url = FALLBACK_URLS[fallbackIndex % FALLBACK_URLS.length];
            fallbackIndex++;
            usandoFallback = true;
        }

        String nombre = cancion.getTitulo() + " - " + cancion.getArtista();
        mensajeEstado.set(usandoFallback
                ? "Sin preview: " + nombre
                : "Reproduciendo: " + nombre);

        iniciarReproduccion(url);
    }

    public void reproducir(Cancion cancion) {
        if (cancion == null) return;
        detener();

        tituloActual.set(cancion.getNombreCancion());
        artistaActual.set(cancion.getArtista() != null ? cancion.getArtista() : "");

        String url = cancion.getPreviewUrl();
        boolean usandoFallback = false;

        if (url == null || url.isEmpty()) {
            url = FALLBACK_URLS[fallbackIndex % FALLBACK_URLS.length];
            fallbackIndex++;
            usandoFallback = true;
        }

        String nombre = cancion.getNombreCancion();
        mensajeEstado.set(usandoFallback
                ? "Sin preview: " + nombre
                : "Reproduciendo: " + nombre);

        iniciarReproduccion(url);
    }

    private void iniciarReproduccion(String url) {
        try {
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnError(() -> {
                String err = mediaPlayer.getError() != null
                        ? mediaPlayer.getError().getMessage() : "desconocido";
                System.err.println("[Player] Error: " + err);
                mensajeEstado.set("Error: " + err);
                reproduciendo.set(false);
                pausado.set(false);
            });

            mediaPlayer.currentTimeProperty().addListener((obs, o, n) -> tiempoActual.set(n));
            mediaPlayer.setOnReady(() -> duracionTotal.set(mediaPlayer.getTotalDuration()));
            mediaPlayer.setOnEndOfMedia(() -> {
                mensajeEstado.set("Finalizado");
                detener();
            });
            mediaPlayer.volumeProperty().bind(volumen);
            mediaPlayer.play();
            reproduciendo.set(true);
            pausado.set(false);
        } catch (Exception e) {
            System.err.println("[Player] Error: " + e.getMessage());
            mensajeEstado.set("Error: " + e.getMessage());
            reproduciendo.set(false);
        }
    }

    public void pausar() {
        if (mediaPlayer != null && reproduciendo.get()) {
            mediaPlayer.pause();
            reproduciendo.set(false);
            pausado.set(true);
            mensajeEstado.set("Pausado: " + tituloActual.get());
        }
    }

    public void reanudar() {
        if (mediaPlayer != null && pausado.get()) {
            mediaPlayer.play();
            reproduciendo.set(true);
            pausado.set(false);
            mensajeEstado.set("Reproduciendo: " + tituloActual.get());
        }
    }

    public void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        reproduciendo.set(false);
        pausado.set(false);
        tiempoActual.set(Duration.ZERO);
        duracionTotal.set(Duration.ZERO);
    }

    public void buscar(Duration tiempo) {
        if (mediaPlayer != null) mediaPlayer.seek(tiempo);
    }

    public StringProperty tituloActualProperty() { return tituloActual; }
    public StringProperty artistaActualProperty() { return artistaActual; }
    public StringProperty mensajeEstadoProperty() { return mensajeEstado; }
    public ObjectProperty<Duration> tiempoActualProperty() { return tiempoActual; }
    public ObjectProperty<Duration> duracionTotalProperty() { return duracionTotal; }
    public DoubleProperty volumenProperty() { return volumen; }
    public BooleanProperty reproduciendoProperty() { return reproduciendo; }
    public BooleanProperty pausadoProperty() { return pausado; }
    public boolean estaReproduciendo() { return reproduciendo.get(); }
    public boolean estaPausado() { return pausado.get(); }
}
