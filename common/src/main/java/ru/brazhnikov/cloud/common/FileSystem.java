package ru.brazhnikov.cloud.common;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * FileSystem - класс для выполнения задач, связанных с файловой системой.
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.common
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class FileSystem {

    /**
     *  @access private
     *  @var Stage savedStage -
     */
    private static Stage savedStage;

    /**
     * getFilesFromDirectory - получить список файлов целевой директории
     * @param directory - целевая директория
     * @return List<File>
     */
    public static List<File> getFilesFromDirectory ( String directory ) {

        // Получаем файлы из клиентской папки
        File dir        = new File( directory );
        File[] arrFiles = dir.listFiles();
        List<File> fileList  = Arrays.asList( arrFiles );

        return fileList;
    }

    /**
     * multiUploadFiles - мультизагрузка файлов
     */
    public static List<File> multiUploadFiles () {

        // готовим окно для выбора загружаемых файлов
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( "Выберите один или несколько файлов." );
        fileChooser.setInitialDirectory( new File( System.getProperty( "user.home" ) ) );

        if ( fileChooser == null ) {
            return null;
        }
        else {
            return fileChooser.showOpenMultipleDialog( savedStage );
        }


    }

    /**
     * deleteFile - удалить выбранный файл
     * @param pathFile - путь к выбранному файлу
     */
    public void deleteFile ( String pathFile ) {

    }

    /**
     * deleteAllFiles - удалить все файлы из целевой директории
     * @param storageDir - целевая директория
     */
    public static void deleteAllFiles ( String storageDir ) {
        List<File> fileList = getFilesFromDirectory( storageDir );

        for ( File file : fileList ) {
            file.delete();
        }

        // !Fixme провка удаления файлов
    }
}
