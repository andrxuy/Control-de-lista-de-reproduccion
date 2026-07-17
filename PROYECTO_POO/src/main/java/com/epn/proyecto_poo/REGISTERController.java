package com.epn.proyecto_poo;

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
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("aplicacion");
    EntityManager em = emf.createEntityManager();
    loginDAO loginDAO = new loginDAO(em);

    @FXML
    public void initialize(){
        cmbRol.getItems().addAll("Administrador", "Estándar", "Invitado");
    }

    public void Registrar(){
        String user  = txtUser.getText();
        String password = txtPassword.getText();
        String rol = cmbRol.getValue();
        System.out.println("antes try");
        try{
            Usuario usuario = loginDAO.buscarUsuario(user, rol);
            if (usuario == null){
                loginDAO.Insertar(new Usuario(user, password, rol));
                System.out.println("REGISTRO EXITOSO!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("REGISTRO EXITOSO!");
                alert.setContentText("BIENVENIDO "+ user);
                alert.show();
                txtPassword.clear();
                txtUser.clear();
                cmbRol.getSelectionModel().clearSelection();
                //---------------
                txtUser.clear();
                txtPassword.clear();
                cmbRol.getSelectionModel().clearSelection();
                Stage stage = new Stage();
                irALogin(stage);

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("El usuario ya existe ingrese otro nuevamente!");
                alert.setContentText("Ingrese un usuario que no tenga el mismo nombre y rol");
                alert.show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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


}
