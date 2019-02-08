package ru.brazhnikov.cloud.client;

import javafx.scene.control.TreeItem;
import ru.brazhnikov.cloud.common.FileSystem;

import java.io.File;
import java.util.List;

/**
 * TreeItemDir - класс для работы с деревом файлов и каталогов
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class TreeItemDir {

    /**
     *  @access private
     *  @var TreeItem<String> root - корневой узел
     */
    private static TreeItem<String> root = new TreeItem<String>( "server_storage/" );

    /**
     * getItems - получить элементы дерева
     * @param dir - название директории
     */
    public static TreeItem<String> getItems ( String dir ) {

        root.setExpanded( true );

        List<File> lst = FileSystem.getFilesFromDirectory( dir );
        for ( File file : lst ) {
            if ( file.isFile() ) {
                root.getChildren().add(new TreeItem<String>( file.getName() ) );
            }
            else {
                root.getChildren().addAll( getNodesForDirectory( file ) );
            }
        }

        return  root;
    }

    /**
     * getNodesForDirectory - получить файлы в директории
     * @param directory - информация о диретории
     * @return TreeItem<String>
     */
    private static TreeItem<String> getNodesForDirectory( File directory ) {

        TreeItem<String> root = new TreeItem<String>( directory.getName() );
        for( File f : directory.listFiles() ) {

            if( f.isDirectory() ) {
                root.getChildren().add( getNodesForDirectory( f ) );
            }
            else {
                root.getChildren().add( new TreeItem<String>( f.getName() ) );
            }
        }
        return root;
    }
}
