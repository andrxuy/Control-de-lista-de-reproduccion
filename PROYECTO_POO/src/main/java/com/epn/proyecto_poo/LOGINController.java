package com.epn.proyecto_poo;

import com.epn.proyecto_poo.modelo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LOGINController {
    @FXML TextField txtUser;
    @FXML TextField txtPassword;
    @FXML ComboBox<String> cmbRol;

    public void initialize(){
        cmbRol.getItems().addAll("Administrador", "Estándar", "Invitado");
    }
    //----------------
    EntityManagerFactory emf = JPAUtil.getEMF();
    EntityManager em = emf.createEntityManager();

    loginDAO loginDAO = new loginDAO(em);
    Seguridad seguridad = new Seguridad();

    public void iniciarSesion(){
        String user = txtUser.getText();
        String password_plana = txtPassword.getText();
        String rol = cmbRol.getValue();
        if (user.isEmpty() || password_plana.isEmpty() || rol == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor completa usuario, contraseña y selecciona un rol.");
            return;}
        try {
            Usuario usuarioEncontrado = loginDAO.buscarUsuario(user, rol);
            if (usuarioEncontrado == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario no encontrado",
                        "No existe un usuario con ese nombre y rol.");
                return;}
            boolean passwordCorrecta = seguridad.validarHash(password_plana, usuarioEncontrado.getPassword_hash());
            if (passwordCorrecta) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido",
                        "Inicio de sesión exitoso, " + usuarioEncontrado.getNombre_usuario());
                Stage stage = new Stage();
                irAprincipal(stage);
                Stage stageVieja = (Stage) txtUser.getScene().getWindow();
                stageVieja.close();
                loginDAO.InsertarSesion(new enSesion(user, rol));
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Contraseña incorrecta",
                        "La contraseña ingresada no es correcta.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al iniciar sesión",
                    "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }finally {
            em.close();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void irARegistro(Stage stageActual){
        try {
            FXMLLoader loader = new FXMLLoader(
                    REGISTERController.class.getResource("/com/epn/proyecto_poo/Register.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stageActual.setScene(scene);
            stageActual.setTitle("Iniciar Sesión");
            stageActual.show();
            Stage stageVieja = (Stage) txtUser.getScene().getWindow();
            stageVieja.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
    public void goRegister(){
        Stage stage = new Stage();
        irARegistro(stage);
    }
    public void irAprincipal(Stage stageActual){
        try {
            FXMLLoader loader = new FXMLLoader(
                    REGISTERController.class.getResource("/com/epn/proyecto_poo/vistaAlbum.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stageActual.setScene(scene);

            stageActual.setTitle("Iniciar Sesión");
            stageActual.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
