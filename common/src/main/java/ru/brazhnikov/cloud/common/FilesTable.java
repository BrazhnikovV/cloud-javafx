package ru.brazhnikov.cloud.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * FilesTable -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.common
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class FilesTable {

    /**
     *  @access private
     *  @var TableView<FileInfo> filesTable -
     */
    private TableView<FileInfo> filesTable;

    /**
     * FilesTable - конструктор
     * @param filesTable - список файлов таблицы
     */
    public FilesTable( TableView<FileInfo> filesTable ) {
        this.filesTable = filesTable;
    }

    /**
     * initTable - инизализировать таблицу
     * @param storageDir - путь к файлу
     * @throws IOException
     */
    public void initTable ( String storageDir ) throws IOException {

        List<File> lst = this.getFilesFromDirectory( storageDir );

        ObservableList<FileInfo> personsList = FXCollections.observableArrayList();

        TableColumn<FileInfo, String> tcName = this.getTableColunm( "Name", "name" );
        TableColumn<FileInfo, String> tcLength = this.getTableColunm( "Length", "length" );
        TableColumn<FileInfo, String> tcLastMod = this.getTableColunm( "LastModified", "lastModified" );

        for ( File file : lst ) {
            personsList.add(
                    new FileInfo( file.getName(), file.length(), file.lastModified() )
            );
        }

        this.filesTable.getColumns().addAll( tcName, tcLength, tcLastMod );
        this.filesTable.setItems( personsList );
    }

    /**
     * updateTable - обновить таблицу
     * @param selectedFiles - список загружаемых файлов
     * @throws IOException
     */
    public void updateTable ( List<File> selectedFiles ) throws IOException {

        for ( File file : selectedFiles ) {

            boolean isMatch = false;
            FileInfo fileInfo = new FileInfo( file.getName(), file.length(), file.lastModified() );

            if ( this.filesTable.getItems().isEmpty() ) {
                this.filesTable.getItems().add( fileInfo );
                continue;
            }

            for ( FileInfo fi : this.filesTable.getItems() ) {
                if ( fi.getName().equals( fileInfo.getName() ) ) {
                    isMatch = true;
                    break;
                }
            }

            if ( !isMatch ) {
                this.filesTable.getItems().add( fileInfo );
            }
        }
    }

    /**
     * TableColumn - Получить колонку для таблицы
     * @param titelColumn - название колонки
     * @param nameColumn  - тип информации
     * @return TableColumn<FileInfo, String>
     */
    private TableColumn<FileInfo, String> getTableColunm ( String titelColumn, String nameColumn ) {

        TableColumn<FileInfo, String> tcName = new TableColumn<>( titelColumn );
        tcName.setCellValueFactory(new PropertyValueFactory<>( nameColumn ) );

        return tcName;
    }

    /**
     * getFilesFromDirectory - получить список файлов целевой директории
     * @param directory - целевая директория
     * @return List<File>
     */
    private List<File> getFilesFromDirectory ( String directory ) {

        // Получаем файлы из клиентской папки
        File dir        = new File( directory );
        File[] arrFiles = dir.listFiles();
        List<File> fileList  = Arrays.asList( arrFiles );

        return fileList;
    }
}
