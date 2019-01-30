package ru.brazhnikov.cloud.client;

import ru.brazhnikov.cloud.common.AbstractMessage;
import ru.brazhnikov.cloud.common.FileMessage;
import ru.brazhnikov.cloud.common.FileRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

/**
 * MainController - контроллер клиента ui
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class MainController implements Initializable {

    @FXML
    private TextField tfFileName;

    @FXML
    private ListView<String> filesList;

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        Network.start();
        Thread thread = new Thread(() -> {
            try {
                while ( true ) {
                    AbstractMessage am = Network.readObject();
                    if ( am instanceof FileMessage ) {

                        FileMessage fm = ( FileMessage ) am;
                        Files.write(
                            Paths.get("client_storage/" + fm.getFilename() ),
                            fm.getData(),
                            StandardOpenOption.CREATE
                        );
                        this.refreshLocalFilesList();
                    }
                }
            }
            catch ( ClassNotFoundException | IOException e ) {
                e.printStackTrace();
            }
            finally {
                Network.stop();
            }
        });

        thread.setDaemon( true );
        thread.start();

        this.filesList.setItems( FXCollections.observableArrayList() );
        this.refreshLocalFilesList();
    }

    /**
     * pressOnDownloadBtn - перехватить событие нажатия на кнопку
     * @param actionEvent - событие
     */
    public void pressOnDownloadBtn( ActionEvent actionEvent ) {

        if ( this.tfFileName.getLength() > 0 ) {
            Network.sendMsg( new FileRequest( this.tfFileName.getText() ) );
            this.tfFileName.clear();
        }
    }

    /**
     * refreshLocalFilesList - обновить список локальных файлов
     */
    private void refreshLocalFilesList() {

        if ( Platform.isFxApplicationThread() ) {
            try {
                this.filesList.getItems().clear();
                Files.list(
                    Paths.get("client_storage"))
                        .map( p -> p.getFileName().toString() )
                        .forEach( o -> this.filesList.getItems().add( o ) );
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        else {
            Platform.runLater(() -> {
                try {
                    this.filesList.getItems().clear();
                    Files.list(
                        Paths.get("client_storage" ) )
                            .map( p -> p.getFileName().toString() )
                            .forEach( o -> this.filesList.getItems().add( o ) );
                }
                catch ( IOException e ) {
                    e.printStackTrace();
                }
            });
        }
    }
}
