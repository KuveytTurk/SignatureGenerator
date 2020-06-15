/*
 * Copyright (c) 2020
 * KUVEYT TÜRK PARTICIPATION BANK INC.
 *
 * Author: Fikri Aydemir
 *
 * Project: API Request SignatureGenerator
 */

package tr.com.kuveytturk.api.signature;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SignatureGeneratorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Request Signature Generator for KT API v0.1");

        Parent root = FXMLLoader.load(getClass().getResource("/SignatureGenerator.fxml"));

        //prefHeight="666.0" prefWidth="802.0"
        Scene scene = new Scene(root, 1010, 435);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/apiImage.png")));
        primaryStage.show();
    }
}
