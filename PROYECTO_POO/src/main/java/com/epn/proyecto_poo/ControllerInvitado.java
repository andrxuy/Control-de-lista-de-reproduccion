package com.epn.proyecto_poo;

import com.epn.proyecto_poo.api.DeezerAPIClient;
import com.epn.proyecto_poo.api.ReproductorServicio;
import com.epn.proyecto_poo.modelo.*;
import com.epn.proyecto_poo.servicio.CancionServicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;


public class ControllerInvitado {
    @FXML TableView<CancionTabla> tblCanciones;
    @FXML TableColumn<CancionTabla, Integer> colId;
    @FXML TableColumn<CancionTabla, String> colNombre;
    @FXML TableColumn<CancionTabla, String> colArtista;
    @FXML TableColumn<CancionTabla, String> colGenero;
    @FXML TableColumn<CancionTabla, String> colDuracion;
    @FXML TableColumn<CancionTabla, String> colOrigen;
    @FXML TableColumn<CancionTabla, String> colAcciones;

    @FXML TextField txtBusqueda;
    @FXML TextField txtBuscarId;
    @FXML TextField txtBuscarApi;
    @FXML ComboBox<String> cmbGenero;
    @FXML Label lblUsuario;
    @FXML Label lblEstado;

    @FXML Label lblCancionActual;
    @FXML Label lblTiempoActual;
    @FXML Label lblTiempoTotal;
    @FXML Slider sldProgreso;
    @FXML Slider sldVolumen;
    @FXML Button btnPlayPause;

    private CancionServicio cancionServicio;
    private ReproductorServicio reproductorServicio;
    private DeezerAPIClient deezerClient;
    private ObservableList<CancionTabla> listaCanciones;
    private boolean bloqueandoSlider = false;
    private boolean esAdmin = false;

    public void initialize() {
        cancionServicio = new CancionServicio();
        reproductorServicio = new ReproductorServicio();
        deezerClient = new DeezerAPIClient();
        esAdmin = "Administrador".equals(SesionActual.getRol());
        configurarTabla();
        configurarBusqueda();
        configurarReproductor();
        cargarGeneros();
        cargarCanciones();
        mostrarUsuario();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCancion"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionFormateada"));

        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colOrigen.setVisible(esAdmin);
        colOrigen.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                Label lbl = new Label(item);
                lbl.setStyle("Deezer".equals(item)
                        ? "-fx-text-fill: #ffaa00; -fx-font-weight: bold;"
                        : "-fx-text-fill: #1DB954; -fx-font-weight: bold;");
                setGraphic(lbl);
            }
        });

        colAcciones.setCellValueFactory(new PropertyValueFactory<>("nombreCancion"));
        colAcciones.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                CancionTabla cancion = getTableView().getItems().get(getIndex());
                javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(4);

                Button btnPlay = new Button("Play");
                btnPlay.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                btnPlay.setOnAction(e -> reproducirCancion(cancion));
                hbox.getChildren().add(btnPlay);

                if (esAdmin) {
                    if (cancion.esDeDeezer()) {
                        Button btnGuardar = new Button("Guardar");
                        btnGuardar.setStyle("-fx-background-color: #0077ff; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                        btnGuardar.setOnAction(e -> guardarDesdeDeezer(cancion));
                        hbox.getChildren().add(btnGuardar);
                    } else {
                        Button btnEditar = new Button("Editar");
                        btnEditar.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                        btnEditar.setOnAction(e -> editarCancion(cancion));
                        hbox.getChildren().add(btnEditar);

                        Button btnEliminar = new Button("Eliminar");
                        btnEliminar.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11;");
                        btnEliminar.setOnAction(e -> eliminarCancion(cancion));
                        hbox.getChildren().add(btnEliminar);
                    }
                }
                setGraphic(hbox);
            }
        });
    }

    private void configurarBusqueda() {
        txtBusqueda.setOnAction(e -> buscarCanciones());
        txtBuscarId.setOnAction(e -> buscarPorId());
        txtBuscarApi.setOnAction(e -> buscarEnDeezer());
        cmbGenero.setOnAction(e -> {
            String genero = cmbGenero.getValue();
            if (genero != null) {
                if ("Todos".equals(genero)) {
                    cargarCanciones();
                } else {
                    listaCanciones = FXCollections.observableArrayList();
                    for (Cancion c : cancionServicio.filtrarPorGenero(genero))
                        listaCanciones.add(CancionTabla.desdeCancion(c));
                    tblCanciones.setItems(listaCanciones);
                }
                actualizarEstado();
            }
        });
    }

    private void configurarReproductor() {
        sldVolumen.setValue(reproductorServicio.volumenProperty().get() * 100);
        sldVolumen.valueProperty().addListener((obs, o, n) ->
                reproductorServicio.volumenProperty().set(n.doubleValue() / 100.0));

        sldProgreso.setOnMousePressed(e -> bloqueandoSlider = true);
        sldProgreso.setOnMouseReleased(e -> {
            bloqueandoSlider = false;
            Duration total = reproductorServicio.duracionTotalProperty().get();
            if (total != null && total.toMillis() > 0) {
                Duration seekTo = Duration.millis(sldProgreso.getValue() * total.toMillis() / 100.0);
                reproductorServicio.buscar(seekTo);
            }
        });

        reproductorServicio.tiempoActualProperty().addListener((obs, o, n) -> {
            if (!bloqueandoSlider && n != null) {
                Duration total = reproductorServicio.duracionTotalProperty().get();
                if (total != null && total.toMillis() > 0)
                    sldProgreso.setValue((n.toMillis() / total.toMillis()) * 100.0);
            }
            Platform.runLater(() -> lblTiempoActual.setText(formatearDuration(n)));
        });

        reproductorServicio.duracionTotalProperty().addListener((obs, o, n) ->
                Platform.runLater(() -> lblTiempoTotal.setText(formatearDuration(n))));

        reproductorServicio.tituloActualProperty().addListener((obs, o, n) ->
                Platform.runLater(() -> {
                    String a = reproductorServicio.artistaActualProperty().get();
                    lblCancionActual.setText((n != null && !n.isEmpty())
                            ? n + (a != null && !a.isEmpty() ? " - " + a : "")
                            : "Ninguna cancion seleccionada");
                    btnPlayPause.setText(reproductorServicio.estaReproduciendo() ? "||" : ">");
                }));

        reproductorServicio.reproduciendoProperty().addListener((obs, o, n) ->
                Platform.runLater(() -> btnPlayPause.setText(n ? "||" : ">")));

        reproductorServicio.mensajeEstadoProperty().addListener((obs, o, n) ->
                Platform.runLater(() -> {
                    if (n != null && !n.isEmpty()) {
                        if (n.startsWith("Sin preview")) {
                            lblEstado.setStyle("-fx-text-fill: #ffaa00;");
                        } else if (n.startsWith("Error")) {
                            lblEstado.setStyle("-fx-text-fill: #ff4444;");
                        } else if (n.startsWith("Reproduciendo")) {
                            lblEstado.setStyle("-fx-text-fill: #1DB954;");
                        } else {
                            lblEstado.setStyle("-fx-text-fill: white;");
                        }
                        lblEstado.setText(n);
                    }
                }));
    }

    private String formatearDuration(Duration d) {
        if (d == null || d.isUnknown()) return "0:00";
        int t = (int) d.toSeconds();
        return String.format("%d:%02d", t / 60, t % 60);
    }

    private void cargarGeneros() {
        cmbGenero.getItems().clear();
        cmbGenero.getItems().add("Todos");
        cmbGenero.getItems().addAll(cancionServicio.obtenerGeneros());
        cmbGenero.getSelectionModel().selectFirst();
    }

    private void cargarCanciones() {
        listaCanciones = FXCollections.observableArrayList();
        for (Cancion c : cancionServicio.obtenerTodas())
            listaCanciones.add(CancionTabla.desdeCancion(c));
        tblCanciones.setItems(listaCanciones);
        actualizarEstado();
    }

    private void buscarCanciones() {
        String filtro = txtBusqueda.getText();
        if (filtro == null || filtro.trim().isEmpty()) { cargarCanciones(); return; }

        listaCanciones = FXCollections.observableArrayList();
        List<Cancion> locales = cancionServicio.buscar(filtro.trim());
        for (Cancion c : locales) listaCanciones.add(CancionTabla.desdeCancion(c));

        if (locales.isEmpty()) {
            List<CancionPreview> deezer = deezerClient.buscarCanciones(filtro.trim());
            for (CancionPreview p : deezer) listaCanciones.add(CancionTabla.desdePreview(p));
        }
        tblCanciones.setItems(listaCanciones);
        actualizarEstado();
    }

    @FXML
    public void buscarPorId() {
        String texto = txtBuscarId.getText();
        if (texto == null || texto.trim().isEmpty()) {
            cargarCanciones();
            return;
        }
        try {
            int id = Integer.parseInt(texto.trim());
            Cancion cancion = cancionServicio.obtenerPorId(id);
            listaCanciones = FXCollections.observableArrayList();
            if (cancion != null) {
                listaCanciones.add(CancionTabla.desdeCancion(cancion));
                lblEstado.setText("Encontrada: ID " + id);
            } else {
                cargarCanciones();
                mostrarAlerta(Alert.AlertType.INFORMATION, "No encontrado",
                        "No existe cancion con ID: " + id);
            }
            tblCanciones.setItems(listaCanciones);
            txtBuscarId.clear();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error",
                    "Ingrese un numero valido para el ID.");
        }
    }

    @FXML
    public void buscarEnDeezer() {
        String query = txtBuscarApi.getText();
        if (query == null || query.trim().isEmpty()) {
            cargarCanciones();
            return;
        }
        try {
            List<CancionPreview> resultados = deezerClient.buscarCanciones(query.trim());
            listaCanciones = FXCollections.observableArrayList();
            for (CancionPreview preview : resultados)
                listaCanciones.add(CancionTabla.desdePreview(preview));
            tblCanciones.setItems(listaCanciones);
            lblEstado.setText("Deezer: " + resultados.size() + " resultados");
            txtBuscarApi.clear();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "Error al buscar en Deezer: " + e.getMessage());
        }
    }

    @FXML
    public void mostrarTodos() {
        txtBuscarId.clear();
        txtBuscarApi.clear();
        txtBusqueda.clear();
        cargarCanciones();
    }

    private void mostrarUsuario() {
        String u = SesionActual.getUsuario();
        String r = SesionActual.getRol();
        lblUsuario.setText((u != null ? u : "Invitado") + " (" + (r != null ? r : "") + ")");
    }

    private void actualizarEstado() {
        long bd = 0, dz = 0;
        if (listaCanciones != null)
            for (CancionTabla c : listaCanciones) { if (c.esDeBD()) bd++; else dz++; }
        String e = "BD: " + bd;
        if (dz > 0) e += " | Deezer: " + dz;
        lblEstado.setText(e);
    }

    @FXML
    public void togglePlayPause() {
        if (reproductorServicio.estaReproduciendo()) reproductorServicio.pausar();
        else if (reproductorServicio.estaPausado()) reproductorServicio.reanudar();
        else {
            CancionTabla sel = tblCanciones.getSelectionModel().getSelectedItem();
            if (sel != null) reproducirCancion(sel);
        }
    }

    @FXML
    public void detenerReproduccion() {
        reproductorServicio.detener();
        lblCancionActual.setText("Ninguna cancion seleccionada");
        sldProgreso.setValue(0);
        lblTiempoActual.setText("0:00");
        lblTiempoTotal.setText("0:00");
        btnPlayPause.setText(">");
    }

    @FXML
    public void reproducirSeleccionada() {
        CancionTabla sel = tblCanciones.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin seleccion", "Selecciona una cancion.");
            return;
        }
        reproducirCancion(sel);
    }

    private void reproducirCancion(CancionTabla item) {
        lblEstado.setText("");
        if (item.esDeBD()) {
            reproductorServicio.reproducir(item.getCancionRef());
        } else if (item.getPreviewRef() != null) {
            reproductorServicio.reproducir(item.getPreviewRef());
        }
    }

    private void guardarDesdeDeezer(CancionTabla item) {
        try {
            Cancion cancion = item.getPreviewRef().aCancion();
            cancionServicio.guardarSiNoExiste(cancion);
            cargarCanciones();
            cargarGeneros();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Guardada",
                    "Cancion guardada en BD: " + item.getNombreCancion());
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Ya existe", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    private void editarCancion(CancionTabla item) {
        if (item.getCancionRef() == null) return;
        Cancion cancion = item.getCancionRef();

        TextInputDialog dlgNombre = new TextInputDialog(cancion.getNombreCancion());
        dlgNombre.setTitle("Editar Cancion");
        dlgNombre.setHeaderText(null);
        dlgNombre.setContentText("Nombre:");
        dlgNombre.showAndWait().ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) cancion.setNombreCancion(nombre.trim());
        });

        TextInputDialog dlgArtista = new TextInputDialog(cancion.getArtista());
        dlgArtista.setTitle("Editar Cancion");
        dlgArtista.setHeaderText(null);
        dlgArtista.setContentText("Artista:");
        dlgArtista.showAndWait().ifPresent(artista -> cancion.setArtista(artista.trim()));

        TextInputDialog dlgGenero = new TextInputDialog(cancion.getGenero());
        dlgGenero.setTitle("Editar Cancion");
        dlgGenero.setHeaderText(null);
        dlgGenero.setContentText("Genero:");
        dlgGenero.showAndWait().ifPresent(genero -> cancion.setGenero(genero.trim()));

        try {
            cancionServicio.guardar(cancion);
            cargarCanciones();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizada",
                    "Cancion actualizada: " + cancion.getNombreCancion());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar: " + e.getMessage());
        }
    }

    private void eliminarCancion(CancionTabla item) {
        if (item.getCancionRef() == null) return;
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Confirmar");
        conf.setHeaderText(null);
        conf.setContentText("Eliminar: " + item.getNombreCancion() + "?");
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                cancionServicio.eliminar(item.getCancionRef().getIdCancion());
                cargarCanciones();
                cargarGeneros();
            }
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    public void cerrarSesion() {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Cerrar Sesion");
        conf.setHeaderText(null);
        conf.setContentText("Estas seguro de cerrar sesion?");
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                reproductorServicio.detener();
                SesionActual.cerrarSesion();
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/com/epn/proyecto_poo/LOGIN.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) tblCanciones.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Iniciar Sesion");
                } catch (IOException e) {
                    System.out.println("Error abriendo login: " + e.getMessage());
                }
            }
        });
    }
}
