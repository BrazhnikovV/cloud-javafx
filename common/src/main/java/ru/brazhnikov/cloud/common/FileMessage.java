package ru.brazhnikov.cloud.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FileMessage -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.common
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class FileMessage extends AbstractMessage {

    /**
     *  @access private
     *  @var String filename - имя файла
     */
    private String filename;

    /**
     *  @access private
     *  @var byte[] data -
     */
    private byte[] data;

    /**
     * getFilename - получить имя файла
     * @return String
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * getData - получить
     * @return byte[]
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * FileMessage - конструктор
     * @param path - объект содержащий данные о файле
     * @throws IOException
     */
    public FileMessage( Path path ) throws IOException {

        this.filename = path.getFileName().toString();
        this.data     = Files.readAllBytes( path );
    }
}
