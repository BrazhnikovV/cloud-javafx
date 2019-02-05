package ru.brazhnikov.cloud.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import ru.brazhnikov.cloud.common.AbstractMessage;
import ru.brazhnikov.cloud.common.AuthMessage;
import ru.brazhnikov.cloud.common.CommandMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * LoginController - класс контроллер авторизации пользователя в хранилище
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class LoginController implements Initializable {

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private VBox globParent;

    /**
     *  @access private
     *  @var int id - идентификатор
     */
    public int id;

    /**
     *  @access private
     *  @var MainController backController - объект для хранения основного контроллера
     */
    public MainController backController;

    /**
     * auth - авторизовация
     * @param actionEvent - событие
     */
    public void auth( ActionEvent actionEvent ) {
        Network.sendMsg( new AuthMessage( this.login.getText(), this.password.getText() ) );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread thread = new Thread(() -> {
            try {
                while ( true ) {
                    // клиент слушает файл месаджи
                    AbstractMessage am = Network.readObject();

                    if ( am instanceof CommandMessage) {
                        System.out.println( "### LoginController => instanceof CommandMessage" );
                        // эта штука позволяет работать с интерфейсом, без нее ошибка
                        Platform.runLater(() -> {
                            this.globParent.getScene().getWindow().hide();
                        });
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
}
