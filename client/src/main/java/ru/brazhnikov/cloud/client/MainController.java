package ru.brazhnikov.cloud.client;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.brazhnikov.cloud.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import ru.brazhnikov.cloud.common.FileSystem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
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
    private TableView<FileInfo> clientFilesTable;

    @FXML
    private TableView<FileInfo> serverFilesTable;

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

    /**
     *  @access private
     *  @var FilesTable delegatClientFilesTable -
     */
    private FilesTable delegatClientFilesTable;

    /**
     *  @access private
     *  @var FilesTable delegatServerFilesTable -
     */
    private FilesTable delegatServerFilesTable;

    /**
     *  @access private
     *  @var boolean isGuest -
     */
    private boolean isGuest = true;

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        try {
            if ( this.isGuest ) {
                this.showAuthModal();
            }
            else {
                this.initClienFilesTable();
                this.initServerFilesTable();
            }
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
                    // если клиенту приходит файл меcадж, то он сохраняет его к себе в хранилище
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

        List<File> selectedFiles = FileSystem.multiUploadFiles( this.clientStorageDir );
        for ( File file : selectedFiles ) {
            this.sendFile( this.clientStorageDir + file.getName() );
        }

        try {
            this.updateServerFilesTable( selectedFiles );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * deleteAllFiles - удалить все файлы из целевой директории
     */
    public void deleteAllFiles () {
        System.out.println( "CLIENT MainController => deleteAllFiles" );

        FileSystem.deleteAllFiles( this.clientStorageDir );
        try {
            this.updateClientFilesTable();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * showAuthModal - показать окно авторизации
     */
    public void showAuthModal() throws IOException {

        Stage stage       = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml" ) );
        Parent root       = loader.load();

        LoginController lc = ( LoginController ) loader.getController();
        lc.backController  = this;

        // изменяем стандартный обработчик закрытия окна ( крестик справа в верху )
        stage.setOnCloseRequest( new EventHandler<WindowEvent>() {
            @Override
            public void handle( WindowEvent event ) {
                System.exit(0 );
            }
        });

        stage.setTitle( "Cloud Server :: Autorization" );
        stage.setScene( new Scene(root, 400, 200 ) );
        stage.initModality( Modality.APPLICATION_MODAL );
        stage.showAndWait();
    }

    /**
     * initClienFilesTable - инизализировать таблицу клиентских файлов
     * @throws IOException
     */
    private void initClienFilesTable () throws IOException {
        this.delegatClientFilesTable = new FilesTable( this.clientFilesTable );
        this.delegatClientFilesTable.initTable( this.clientStorageDir );
    }

    /**
     * initServerFilesTable - инизализировать таблицу серверных файлов
     * @throws IOException
     */
    private void initServerFilesTable () throws IOException {
        this.delegatServerFilesTable = new FilesTable( this.serverFilesTable );
        this.delegatServerFilesTable.initTable( this.serverStorageDir );
    }

    /**
     * updateServerFilesTable - обновить таблицу серверных файлов
     * @param selectedFiles - список загружаемых файлов
     * @throws IOException
     */
    private void updateServerFilesTable ( List<File> selectedFiles ) throws IOException {
        this.delegatServerFilesTable.updateTable( selectedFiles );
    }

    /**
     * updateClientFilesTable - обновить таблицу клиентских файлов
     * @throws IOException
     */
    private void updateClientFilesTable () throws IOException {
        System.out.println( "CLIENT MainController => updateClientFilesTable" );
        this.delegatClientFilesTable.updateTable( this.clientStorageDir );
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
}
