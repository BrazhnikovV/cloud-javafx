package ru.brazhnikov.cloud.client;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.brazhnikov.cloud.common.AbstractMessage;
import ru.brazhnikov.cloud.common.FileInfo;
import ru.brazhnikov.cloud.common.FileMessage;
import ru.brazhnikov.cloud.common.FileRequest;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
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
    TableView<FileInfo> clientFilesTable;

    @FXML
    TableView<FileInfo> serverFilesTable;

    /**
     *  @access private
     *  @var String clientStorageDir - путь к папке с клиентскими файлами
     */
    private String clientStorageDir = "client_storage/";

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        try {
            this.initClienFilesTable();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        Network.start();
        Thread thread = new Thread(() -> {
            try {
                while ( true ) {
                    // клиент слушает файл месаджи
                    AbstractMessage am = Network.readObject();
                    // если клиенту приходит файл меадж, то он сохраняет его к себе в хранилище
                    if ( am instanceof FileMessage ) {
                        FileMessage fm = ( FileMessage ) am;
                        Files.write(
                            Paths.get(this.clientStorageDir + fm.getFilename() ),
                            fm.getData(),
                            StandardOpenOption.CREATE
                        );
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
    }

    /**
     * initClienFilesTable - инизализировать таблицу клиентских файлов
     * @throws IOException
     */
    public void initClienFilesTable () throws IOException {

        // Получаем файлы из клиентской папки
        File dir        = new File( this.clientStorageDir );
        File[] arrFiles = dir.listFiles();
        List<File> lst  = Arrays.asList( arrFiles );

        ObservableList<FileInfo> personsList = FXCollections.observableArrayList();

        TableColumn<FileInfo, String> tcName = new TableColumn<>("Name" );
        tcName.setCellValueFactory(new PropertyValueFactory<>( "name" ) );

        TableColumn<FileInfo, String> tcLength = new TableColumn<>("Length" );
        tcLength.setCellValueFactory(new PropertyValueFactory<>( "length" ) );

        TableColumn<FileInfo, String> tcLastMod = new TableColumn<>("LastModified" );
        tcLastMod.setCellValueFactory(new PropertyValueFactory<>( "lastModified" ) );

        for ( File file : lst ) {
            personsList.add( new FileInfo( file.getName(), file.length(), file.lastModified() ) );
        }

        this.clientFilesTable.getColumns().addAll( tcName, tcLength, tcLastMod );
        this.clientFilesTable.setItems( personsList );
    }

    /**
     * pressOnDownloadBtn - перехватить событие нажатия на кнопку
     * @param actionEvent - событие
     */
    public void pressOnDownloadBtn( ActionEvent actionEvent ) {
        System.out.println( "CLIENT MainController => pressOnDownloadBtn" );

        if ( this.tfFileName.getLength() > 0 ) {
            Network.sendMsg( new FileRequest( this.tfFileName.getText() ) );
            this.tfFileName.clear();
        }
    }

    /**
     * pressOnUploadBtn - перехватить событие нажатия
     * на кнопку для загрузки файла на сервер
     * @param actionEvent - событие
     */
    public void pressOnUploadBtn( ActionEvent actionEvent ) throws IOException {
        System.out.println( "CLIENT MainController => pressOnUploadBtn" );

        String file = this.clientStorageDir + this.tfFileName.getText().trim();

        if ( Files.exists( Paths.get( file ) ) ) {
            try {
                FileMessage fileMessage = new FileMessage( Paths.get( file ) );
                Network.sendMsg( fileMessage );
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }

        this.tfFileName.clear();
    }
}
