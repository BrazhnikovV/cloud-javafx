package ru.brazhnikov.cloud.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FileMessage - класс сущность представления файла
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
     *  @var byte[] data - байтовый массив данных файла
     */
    private byte[] data;

    /**
     *  @access private
     *  @var String pathToSave - путь к месту сохранения в файловом хранилище
     */
    private String pathToSave;

    /**
     * getFilename - получить имя файла
     * @return String
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * getData - получить массив байт данных файла
     * @return byte[]
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * getPathToSave - получить путь к месту сохранения файла в файловом хранилище
     * @return String
     */
    public String getPathToSave() { return this.pathToSave; }

    /**
     * FileMessage - конструктор
     * @param path - объект содержащий данные о файле
     * @throws IOException
     */
    public FileMessage( Path path, String pathToSave ) throws IOException {

        this.filename   = path.getFileName().toString();
        this.data       = Files.readAllBytes( path );
        this.pathToSave = pathToSave;
    }
}
