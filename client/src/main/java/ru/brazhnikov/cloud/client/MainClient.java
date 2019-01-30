package ru.brazhnikov.cloud.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainClient -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class MainClient extends Application {

    @Override
    public void start( Stage primaryStage ) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource("/main.fxml" ) );
        Parent root = fxmlLoader.load();
        primaryStage.setTitle( "Box Client" );
        Scene scene = new Scene( root );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch( args );
    }
}
