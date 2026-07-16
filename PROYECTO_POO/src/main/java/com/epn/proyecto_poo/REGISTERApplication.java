package com.epn.proyecto_poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class REGISTERApplication extends Application {
    public void start(Stage stage)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LOGINApplication.class.getResource("Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("REGISTRO - MI PLAYLIST");
        stage.setScene(scene);
        stage.show();
    }
}
