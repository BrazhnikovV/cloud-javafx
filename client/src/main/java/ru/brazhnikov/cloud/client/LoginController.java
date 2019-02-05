package ru.brazhnikov.cloud.client;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

/**
 * LoginController - класс контроллер авторизации пользователя в хранилище
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class LoginController {

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
        System.out.println( this.login.getText() + " " + this.password.getText());
        System.out.println( "id = " + this.id );
        this.globParent.getScene().getWindow().hide();
    }
}
