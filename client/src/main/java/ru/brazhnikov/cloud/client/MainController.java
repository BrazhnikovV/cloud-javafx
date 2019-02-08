package ru.brazhnikov.cloud.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
import java.util.ArrayList;
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

    TreeItem<String> root;

    @FXML
    private TreeView<String> serverTreeView;

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        try {
            this.showAuthModal();
            this.initTreeItemDir();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        Network.start();
        Thread thread = new Thread(() -> {
            try {
                while ( true ) {
                    System.out.println( "### MainController => thread = new Thread(() ->" );
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

                    if ( am instanceof CommandMessage ) {
                        System.out.println( "### MainController => instanceof CommandMessage" );
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
            this.root.getChildren().add(new TreeItem<String>(file.getName()));
        }
    }

    /**
     * deleteAllFiles - удалить все файлы из целевой директории
     */
    public void deleteAllFiles () {
        System.out.println( "CLIENT MainController => deleteAllFiles" );

        FileSystem.deleteAllFiles( this.clientStorageDir );
    }

    /**
     * showAuthModal - показать окно авторизации
     */
    public void showAuthModal() throws IOException {
        System.out.println( "CLIENT MainController => showAuthModal" );

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

    /**
     * initTreeItemDir - инициализирует дерево файлов серверного хранилища
     */
    private void initTreeItemDir () {

        // получаем элементы для дерева файлов
        this.root = TreeItemDir.getItems( this.serverStorageDir );

        // убираем рамку у поля при фокусе
        this.serverTreeView.setStyle( "-fx-focus-traversable: false" );
        this.serverTreeView.setRoot( this.root );
    }

    /**
     * updateTreeItemDir -
     */
    private void updateTreeItemDir () {
        System.out.println( "CLIENT MainController => updateTreeItemDir" );

        // получаем элементы для дерева файлов

        List<File> lst = FileSystem.getFilesFromDirectory( serverStorageDir );
        for ( File file : lst ) {
            this.root.getChildren().add(new TreeItem<String>(file.getName()));
        }

        //this.serverTreeView.setRoot( this.root );
    }
}
