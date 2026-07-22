package com.epn.proyecto_poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LOGINApplication extends Application {
    public void start(Stage stage)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LOGINApplication.class.getResource("LOGIN.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("INICIAR SESION - MI PLAYLIST");
        stage.setScene(scene);
        stage.show();
    }
}
