package ru.brazhnikov.cloud.client;

/**
 * FileInfo - класс информация о файле
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class FileInfo {

    /**
     *  @access private
     *  @var String name - имя файла
     */
    private String name;

    /**
     *  @access private
     *  @var long length - длинна файла
     */
    private long length;

    /**
     * getLength - получить длинну файла
     * @return long
     */
    public long getLength() {
        return length;
    }

    /**
     * setLength - установить длинну файла
     * @param length - длинна файла
     */
    public void setLength( long length ) {
        this.length = length;
    }

    /**
     * getName - получить имя файла
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * setName - установить имя файла
     * @param name - имя файла
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * FileInfo - конструктор
     * @param name - имя файла
     * @param length - длинна файла
     */
    public FileInfo( String name, long length ) {
        this.name = name;
        this.length = length;
    }
}
