package com.epn.proyecto_poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InvitadoApplication extends Application {
    public void start(Stage stage)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(InvitadoApplication.class.getResource("playlist1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MI PLAYLIST INVITADO");
        stage.setScene(scene);
        stage.show();
    }
}
