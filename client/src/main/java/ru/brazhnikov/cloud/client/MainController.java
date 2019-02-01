package ru.brazhnikov.cloud.client;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.brazhnikov.cloud.common.*;
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

    private Stage savedStage;

    /**
     *  @access private
     *  @var String clientStorageDir - путь к папке с клиентскими файлами
     */
    private String clientStorageDir = "client_storage/";

    /**
     *  @access private
     *  @var String serverStorageDir - путь к папке с серверными файлами
     */
    private String serverStorageDir = "server_storage/";

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        try {
            this.initClienFilesTable();
            this.initServerFilesTable();
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
        this.sendFile( file );
        this.tfFileName.clear();
    }

    /**
     * pressOnMultiUploadBtn - перехватывает событие
     * нажатия на кнопку загрузки файлов на клиент
     */
    public void pressOnMultiUploadBtn () {
        System.out.println( "CLIENT MainController => pressOnMultiUploadBtn" );

        // готовим окно для выбора загружаемых файлов
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( "Выберите один или несколько файлов." );
        fileChooser.setInitialDirectory( new File( this.clientStorageDir ) );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog( this.savedStage );

        for ( File file : selectedFiles ) {
            System.out.println( "Load file : " + file.getName() );
            this.sendFile( this.clientStorageDir + file.getName() );
        }

        try {
            this.updateServerFilesTable( selectedFiles );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initClienFilesTable - инизализировать таблицу клиентских файлов
     * @throws IOException
     */
    private void initClienFilesTable () throws IOException {
        FilesTable filesTable = new FilesTable( this.clientFilesTable );
        filesTable.initTable( this.clientStorageDir );
    }

    /**
     * initServerFilesTable - инизализировать таблицу серверных файлов
     * @throws IOException
     */
    private void initServerFilesTable () throws IOException {
        FilesTable filesTable = new FilesTable( this.serverFilesTable );
        filesTable.initTable( this.serverStorageDir );
    }

    /**
     * updateServerFilesTable - обновить таблицу серверных файлов
     * @param selectedFiles - список загружаемых файлов
     * @throws IOException
     */
    private void updateServerFilesTable ( List<File> selectedFiles ) throws IOException {
        FilesTable filesTable = new FilesTable( this.serverFilesTable );
        filesTable.updateTable( selectedFiles );
    }

    /**
     * sendFile - отправить файл
     * @param file - путь к файлу + имя
     */
    private void sendFile ( String file ) {

        if ( Files.exists( Paths.get( file ) ) ) {
            try {
                FileMessage fileMessage = new FileMessage( Paths.get( file ) );
                Network.sendMsg( fileMessage );
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile () {

    }

    public void deleteAllFiles () {

    }
}
