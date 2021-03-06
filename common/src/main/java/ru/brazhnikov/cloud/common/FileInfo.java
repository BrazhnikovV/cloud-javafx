package ru.brazhnikov.cloud.common;

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
     *  @access private
     *  @var long lastModified - дата последнего изменения файла
     */
    private long lastModified;

    /**
     * getLength - получить длинну файла
     * @return long
     */
    public long getLength() {
        return this.length;
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
        return this.name;
    }

    /**
     * setName - установить имя файла
     * @param name - имя файла
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * getLastModified - получить дату последнего изменения файла
     * @return long
     */
    public long getLastModified() {
        return this.lastModified;
    }

    /**
     * setLastModified - установить дату последнего изменения файла
     * @param lastModified
     */
    public void setLastModified( long lastModified ) {
        this.lastModified = lastModified;
    }

    /**
     * FileInfo - конструктор
     * @param name - имя файла
     * @param length - длинна файла
     * @param lastModified - дата последней модификации файла
     */
    public FileInfo( String name, long length, long lastModified ) {
        this.name = name;
        this.length = length;
        this.lastModified = lastModified;
    }
}
