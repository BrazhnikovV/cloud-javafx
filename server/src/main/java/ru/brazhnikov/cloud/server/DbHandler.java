package ru.brazhnikov.cloud.server;

import java.sql.*;
import java.util.*;
import org.sqlite.JDBC;

/**
 * DbHandler - класс для создания соединения с базой данных и работы с ней
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.server
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class DbHandler {

    /**
     *  @access private
     *  @var String dbName - имя базы данных
     */
    private final String dbName = "cloud-server";

    /**
     *  @access private
     *  @var String path - путь к файлу базы данных
     */
    private final String path = System.getProperty( "user.dir" );

    /**
     *  @access private
     *  @var String connectionString - путь к базе данных
     */
    private final String connectionString = "jdbc:sqlite:" + this.path + "/db/" + this.dbName;

    /**
     *  @access private
     *  @var DbHandler instance - одиночка, чтобы не плодить множество
     *  экземпляров класса DbHandler
     */
    private static DbHandler instance = null;

    /**
     * getInstance -
     * @return synchronized DbHandler
     * @throws SQLException
     */
    public static synchronized DbHandler getInstance() throws SQLException {
        if ( instance == null ) {
            instance = new DbHandler();
        }
        return instance;
    }

    /**
     *  @access private
     *  @var Connection connection - Объект, в котором будет храниться соединение с БД
     */
    private static Connection connection;

    /**
     * DbHandler - конструктор
     * @throws SQLException
     */
    private DbHandler() throws SQLException {
        DriverManager.registerDriver( new JDBC() );
        this.connection = DriverManager.getConnection( this.connectionString );
    }

    /**
     * getUserById - получить пользователя по id
     * @param id - идентификатор пользователя
     */
    public User getUserById( int id ) {
        User user = null;
        try ( PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setObject(1, id );
            statement.execute();

            ResultSet resultSet = statement.executeQuery();
            while ( resultSet.next() ) {
                user = new User(
                    resultSet.getInt("id"), resultSet.getString("name"),
                    resultSet.getString("pass"), resultSet.getInt("created")
                );
            }
            return user;
        }
        catch ( SQLException e ) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустой объект
            return user;
        }
    }

    /**
     * getUserByName - получить пользователя по id
     * @param name - имя пользователя
     */
    public static User getUserByName(String name) {
        User user = null;
        try ( PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE name = ?" ) ) {
            statement.setObject(1, name );
            statement.execute();

            ResultSet resultSet = statement.executeQuery();
            while ( resultSet.next() ) {
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("pass"),
                    resultSet.getInt("created")
                );
            }
            return user;
        }
        catch ( SQLException e ) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустой объект
            return user;
        }
    }

    /**
     * getAllUsers - получить список всех пользователей
     * @return List<User>
     */
    public List<User> getAllUsers() {

        // Statement используется для того, чтобы выполнить sql-запрос
        try ( Statement statement = this.connection.createStatement() ) {

            // В данный список будем загружать наши продукты, полученные из БД
            List<User> users = new ArrayList<User>();

            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT id, name, pass, created FROM users" );

            // Проходимся по нашему resultSet и заносим данные в users
            while ( resultSet.next() ) {
                users.add(
                    new User(
                        resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("pass"), resultSet.getInt("created")
                    )
                );
            }

            return users;

        }
        catch ( SQLException e ) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }

    /**
     * addUser - добавить пользователя
     * @param user - объект пользователя
     */
    public void addUser( User user ) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO Users( `name`, `pass`, `created` ) " + "VALUES( ?, ?, ? )" ) ) {
            statement.setObject(1, user.name );
            statement.setObject(2, user.pass );
            statement.setObject(3, user.created );
            statement.execute();
        }
        catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * deleteUser - Удаление продукта по id
     * @param id - идентификатор пользователя
     */
    public void deleteUser( int id ) {

        try ( PreparedStatement statement = this.connection.prepareStatement("DELETE FROM Users WHERE id = ?" ) ) {
            statement.setObject(1, id );
            statement.execute();
        }
        catch ( SQLException e ) {
            e.printStackTrace();
        }
    }
}