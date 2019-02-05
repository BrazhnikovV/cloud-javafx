package ru.brazhnikov.cloud.common;

/**
 * AuthMessage - класс для передачи сообщения о необходимости
 * пройти авторизацию, содержит данные для авторизации
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.common
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class AuthMessage extends AbstractMessage {

    /**
     *  @access private
     *  @var String name - имя пользователя
     */
    private String login;

    /**
     *  @access private
     *  @var String pass - пароль пользователя
     */
    private String password;

    /**
     * AuthMessage - конструктор
     * @param login - имя пользователя
     * @param password - пароль пользователя
     */
    public AuthMessage ( String login, String password ) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }
}
