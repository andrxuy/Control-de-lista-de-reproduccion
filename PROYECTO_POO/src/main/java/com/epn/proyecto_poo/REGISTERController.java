package com.epn.proyecto_poo;

import com.epn.proyecto_poo.modelo.JPAUtil;
import com.epn.proyecto_poo.modelo.Usuario;
import com.epn.proyecto_poo.modelo.loginDAO;
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

public class REGISTERController {
    @FXML
    TextField txtUser;
    @FXML
    TextField txtPassword;
    @FXML
    ComboBox<String>cmbRol;

    @FXML
    public void initialize(){
        cmbRol.getItems().addAll("Administrador", "Estándar");
    }

    public void Registrar(){
        String user  = txtUser.getText();
        String password = txtPassword.getText();
        String rol = cmbRol.getValue();
        System.out.println("=== REGISTRO ===");
        System.out.println("Usuario: " + user);
        System.out.println("Rol seleccionado: " + rol);

        if (user == null || user.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacio", "Ingrese un nombre de usuario.");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacio", "Ingrese una contrasena.");
            return;
        }
        if (rol == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin rol", "Seleccione un rol (Administrador o Estandar).");
            return;
        }

        EntityManagerFactory emf = null;
        EntityManager em = null;
        try{
            emf = JPAUtil.getEMF();
            em = emf.createEntityManager();
            loginDAO loginDAO = new loginDAO(em);
            Usuario usuario = loginDAO.buscarUsuario(user.trim(), rol);
            System.out.println("Busqueda => nombre: '" + user.trim() + "', rol: '" + rol + "' => resultado: " + usuario);
            if (usuario == null){
                Usuario nuevo = new Usuario(user.trim(), password, rol);
                System.out.println("Creando usuario: " + nuevo);
                loginDAO.Insertar(nuevo);
                System.out.println("REGISTRO EXITOSO!");
                mostrarAlerta(Alert.AlertType.CONFIRMATION, "REGISTRO EXITOSO!", "BIENVENIDO " + user);
                txtPassword.clear();
                txtUser.clear();
                cmbRol.getSelectionModel().clearSelection();

                Stage stage = new Stage();
                irALogin(stage);
                Stage actual = (Stage) txtPassword.getScene().getWindow();
                actual.close();

            }else {
                mostrarAlerta(Alert.AlertType.ERROR, "El usuario ya existe",
                        "Ya existe un usuario '" + user + "' con rol '" + rol + "'.");
            }
        } catch (Exception e) {
            System.out.println("Error en registro: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void irALogin(Stage stageActual) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    REGISTERController.class.getResource("/com/epn/proyecto_poo/LOGIN.fxml")
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
    public void irLOGIN(){
        Stage stage= new Stage();
        irALogin(stage);
    }
}
