package ru.brazhnikov.cloud.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.brazhnikov.cloud.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    /**
     *  @access private
     *  @var String serverStorageDir - путь к папке с серверными файлами
     */
    private static String serverStorageDir = "server_storage/";

    /**
     *  @access private
     *  @var ArrayList<String> selectedPath - путь к директории согласно выбора
     *  пользователя в дереве папок и файлов
     */
    private static ArrayList<String> selectedPath = new ArrayList<>();

    /**
     *  @access private
     *  @var TreeItem<String> - корнеь дерева
     */
    private TreeItem<String> root;

    @FXML
    private TreeView<String> serverTreeView;

    @FXML
    private MenuBar menuBar;

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
                    // клиент слушает файл месаджи
                    AbstractMessage am = Network.readObject();

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
     * pressOnMultiUploadBtn - перехватывает событие
     * нажатия на кнопку загрузки файлов на клиент
     */
    public void pressOnMultiUploadBtn () {
        System.out.println( "CLIENT MainController => pressOnMultiUploadBtn" );

        List<File> selectedFiles = FileSystem.multiUploadFiles();

        if ( selectedFiles == null ) {
            return;
        }

        for ( File file : selectedFiles ) {
            this.sendFile( file.getName() );
            this.root.getChildren().add( new TreeItem<String>( file.getName() ) );
        }
    }

    /**
     * exit - перехватить клик по пункту меню выход из приложения
     * @param actionEvent
     */
    public void pressMenuExit ( ActionEvent actionEvent )  {
        System.exit(0 );
    }

    /**
     * deleteAllFiles - удалить все файлы из целевой директории
     */
    public void deleteAllFiles () {
        System.out.println( "CLIENT MainController => deleteAllFiles" );

        // !Fixme - отправить командМесадж на сервер для очиски хранилища
        //FileSystem.deleteAllFiles( this.clientStorageDir );
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
        this.serverTreeView.getSelectionModel().selectedItemProperty()
            .addListener( new ChangeListener<TreeItem<String>>() {
                @Override
                public void changed( ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldVal, TreeItem<String> newVal) {
                    TreeItem<String> selectedItem = newVal;
                    System.out.println("Selected Text : " + selectedItem.getValue() );

                    selectedPath.clear();
                    selectedPath = getParents( selectedItem );
                    System.out.println( selectedPath );
                }
            });
    }

    /**
     * getParents - получить массив строк названий каталогов присутствующих в выборе
     * пользователя относительно дерева каталогов и папок
     * @param selectedItem - элемент дерева по которому кликнул пользователь
     * @return ArrayList<String>
     */
    private ArrayList<String> getParents ( TreeItem<String> selectedItem ) {

        if ( selectedItem.getParent() != null ) {
            selectedPath.add( selectedItem.getParent().getValue() );
            this.getParents( selectedItem.getParent() );
        }
        return selectedPath;
    }
}
