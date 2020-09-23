package jpp.wuetunes.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jpp.wuetunes.gui.cellFactory.*;
import jpp.wuetunes.model.metadata.Metadata;

import java.util.ArrayList;

public class Playlist {
    public  static TableView<Metadata> createContent(ArrayList<Metadata> metadata) {
        TableView<Metadata> table = new TableView();
        try {
            TableColumn<Metadata, String> title = new TableColumn<>("Title");
            //title.setMinWidth(200);
            title.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
            title.setCellFactory(new CF());

            TableColumn<Metadata, String> artist = new TableColumn<>("Artist");
            //artist.setMinWidth(200);
            artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
            artist.setCellFactory(new CF());

            TableColumn<Metadata, String> album = new TableColumn<>("Album");
            //album.setMinWidth(200);
            album.setCellValueFactory(new PropertyValueFactory<>("albumTitle"));
            album.setCellFactory(new CF());

            TableColumn<Metadata, Integer> track = new TableColumn<>("Nr");
            //track.setMinWidth(200);
            track.setCellValueFactory(new PropertyValueFactory<>("trackNumber"));
            track.setCellFactory(new CFI());


            table.getColumns().addAll(title, artist, album, track);
            table.setPrefHeight(500);
        } catch (Exception e) {
            AlertBox.display("Error",e.getMessage());
        }

        return table;
    }
}
