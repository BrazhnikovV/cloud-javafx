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

    public static final String CMD_MSG_AUTH_OK = "OK";

    private String type = "NONE AUTOHORIZED";

    public CommandMessage() {

    }

    public String getType() {
        return type;
    }
}
