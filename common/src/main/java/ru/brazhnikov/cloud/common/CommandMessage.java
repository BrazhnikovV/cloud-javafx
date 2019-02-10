package ru.brazhnikov.cloud.common;

/**
 * CommandMessage -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class CommandMessage extends AbstractMessage {

    private String[] commandList = { "CREATE_FOLDER", "DELETE_FILE", "DELETE_DIR", "DELETE_ALL", "AUTHORIZATION" };

    private String command;

    public CommandMessage( String command ) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
