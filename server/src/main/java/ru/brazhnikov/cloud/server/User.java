package ru.brazhnikov.cloud.server;

/**
 * User - класс для хранения информации о пользователя
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.server
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class User {

    /**
     *  @access public
     *  @var int id - идентификатор пользователя
     */
    public int id;

    /**
     *  @access public
     *  @var String name - имя пользователя
     */
    public String name;

    /**
     *  @access public
     *  @var String pass - пароль пользователя
     */
    public String pass;

    /**
     *  @access public
     *  @var int created - дата создания пользователя
     */
    public int created;

    /**
     * User - конструктор
     * @param name - имя пользователя
     * @param pass - пароль пользователя
     * @param created - дата создания пользователя
     */
    public User( String name, String pass, int created ) {
        this.name = name;
        this.pass = pass;
        this.created = created;
    }

    /**
     * User - конструктор
     * @param id   - идентификатор пользователя
     * @param name - имя пользователя
     * @param pass - пароль пользователя
     * @param created - дата создания пользователя
     */
    public User( int id, String name, String pass, int created ) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.created = created;
    }

    // Выводим информацию по пользователю
    @Override
    public String toString() {
        return String.format(
            "ID: %s | Имя: %s | Пароль: %s | Создан: %s", this.id, this.name, this.pass, this.created
        );
    }
}
