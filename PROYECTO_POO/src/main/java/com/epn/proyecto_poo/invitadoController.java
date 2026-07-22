package com.epn.proyecto_poo;
import com.epn.proyecto_poo.modelo.*;
import com.epn.proyecto_poo.servicio.CancionServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class invitadoController {
    @FXML private TableView<CancionTabla> tblCanciones;
    @FXML private TableColumn<CancionTabla, Integer> colId;
    @FXML private TableColumn<CancionTabla, String> colNombre;
    @FXML private TableColumn<CancionTabla, String> colArtista;
    @FXML private TableColumn<CancionTabla, String> colGenero;
    @FXML private TableColumn<CancionTabla, String> colDuracion;
    @FXML private TableColumn<CancionTabla, String> colOrigen;
    @FXML private TableColumn<CancionTabla, String> colAcciones;

    @FXML private TextField txtBusqueda;
    @FXML private TextField txtBuscarId;
    @FXML private TextField txtBuscarApi;
    @FXML private ComboBox<String> cmbGenero;
    @FXML private Label lblUsuario;
    @FXML private Label lblEstado;

    @FXML private Label lblCancionActual;
    @FXML private Label lblTiempoActual;
    @FXML private Label lblTiempoTotal;
    @FXML private Slider sldProgreso;
    @FXML private Slider sldVolumen;
    @FXML private Button btnPlayPause;
    @FXML private Button btnReproducir;
    @FXML private Button btnMostrarTodos;

    private CancionServicio cancionServicio;
    private ObservableList<CancionTabla> listaCanciones;
    @FXML
    public void initialize() {
        cancionServicio = new CancionServicio();
        configurarTablaSoloLectura();
        deshabilitarInteraccion();
        cargarGeneros();
        cargarCanciones();
        mostrarUsuarioInvitado();
    }


    private void configurarTablaSoloLectura() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCancion"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionFormateada"));

        colOrigen.setVisible(false);
        colAcciones.setVisible(false);
    }

    private void deshabilitarInteraccion() {
        txtBusqueda.setDisable(true);
        txtBuscarId.setDisable(true);
        txtBuscarApi.setDisable(true);
        cmbGenero.setDisable(true);
        btnReproducir.setDisable(true);
        btnMostrarTodos.setDisable(true);
        btnPlayPause.setDisable(true);
        sldProgreso.setDisable(true);
        sldVolumen.setDisable(true);

        tblCanciones.setEditable(false);
        tblCanciones.setMouseTransparent(true);
        tblCanciones.setFocusTraversable(false);
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

    private void actualizarEstado() {
        int total = listaCanciones != null ? listaCanciones.size() : 0;
        lblEstado.setText("Modo invitado (solo lectura): " + total + " canciones cargadas");
    }

    private void mostrarUsuarioInvitado() {
        lblUsuario.setText("Invitado");
        lblCancionActual.setText("Ninguna cancion seleccionada");
        lblTiempoActual.setText("0:00");
        lblTiempoTotal.setText("0:00");
    }

    @FXML
    public void cerrarSesion() {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Salir del modo invitado");
        conf.setHeaderText(null);
        conf.setContentText("Deseas volver a la pantalla de inicio de sesion?");
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
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
