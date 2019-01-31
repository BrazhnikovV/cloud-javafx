package ru.brazhnikov.cloud.common;

/**
 * FileRequest -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.common
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class FileRequest extends AbstractMessage {

    /**
     *  @access private
     *  @var String filename - имя файла
     */
    private String filename;

    /**
     * getFilename - получить имя файла
     * @return String
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * FileRequest - конструктор
     * @param filename - имя файла
     */
    public FileRequest( String filename ) {
        this.filename = filename;
    }
}
