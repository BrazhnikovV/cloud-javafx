package ru.brazhnikov.cloud.client;

import javafx.scene.control.TreeItem;
import ru.brazhnikov.cloud.common.FileSystem;

import java.io.File;
import java.util.List;

public class TreeItemDir {

    /**
     * getItems -
     */
    public static TreeItem<String> getItems ( String serverStorageDir ) {

        TreeItem<String> root = new TreeItem<String>( serverStorageDir );
        root.setExpanded( true );

        List<File> lst = FileSystem.getFilesFromDirectory( serverStorageDir );
        for ( File file : lst ) {
            root.getChildren().add(new TreeItem<String>(file.getName()));
        }

        return  root;
    }
}
