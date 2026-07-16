package com.epn.proyecto_poo;

import com.epn.proyecto_poo.modelo.Seguridad;
import com.epn.proyecto_poo.modelo.Usuario;
import com.epn.proyecto_poo.modelo.loginDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class LOGINController {
    @FXML TextField txtUser;
    @FXML TextField txtPassword;
    @FXML ComboBox<String> cmbRol;

    public void initialize(){
        cmbRol.getItems().addAll("Administrador", "Estándar", "Invitado");
    }

    @FXML
    public void iniciarSesion(){
        Seguridad seguridad = new Seguridad();
        String user = txtUser.getText();
        String password_plana = txtPassword.getText();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aplicacion");
        EntityManager em = emf.createEntityManager();

        try {
            loginDAO loginDAO = new loginDAO(em);
            Usuario encontrado = loginDAO.buscarUsuario(user, cmbRol.getValue());

            boolean loginValido = encontrado != null
                    && seguridad.validarHash(password_plana, encontrado.getPassword_hash());

            if (!loginValido) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR NO SE PUDO INICIAR SESION");
                alert.setContentText("Credenciales incorrectas!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("SE PUDO INICIAR SESION");
                alert.setContentText("Bienvenido a Mi Playlist, " + encontrado.getRol() + "!");
                alert.show();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}