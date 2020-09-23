package jpp.wuetunes.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import jpp.wuetunes.gui.cellFactory.*;
import jpp.wuetunes.io.database.DatabaseConnection;
import jpp.wuetunes.model.Song;
import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.model.metadata.MetadataPicture;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;

public class table {
    public  static TableView<Metadata> createContent(LinkedHashSet<Song> songs){
        TableView<Metadata> table = new TableView();
        try {
            TableColumn<Metadata,MetadataPicture> picture = new TableColumn<>("");
            picture.setMinWidth(50);
            picture.setMaxWidth(50);

            picture.setCellValueFactory(new PropertyValueFactory<>("picture"));
            picture.setCellFactory(new CFP());

            TableColumn<Metadata,String> title = new TableColumn<>("Title");
            title.setMinWidth(200);
            title.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
            title.setCellFactory(new CF());

            TableColumn<Metadata,String> artist = new TableColumn<>("Artist");
            artist.setMinWidth(200);
            artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
            artist.setCellFactory(new CF());

            TableColumn<Metadata,String> album = new TableColumn<>("Album");
            album.setMinWidth(200);
            album.setCellValueFactory(new PropertyValueFactory<>("albumTitle"));
            album.setCellFactory(new CF());

            TableColumn<Metadata,Integer> track = new TableColumn<>("Track");
            track.setMinWidth(100);
            track.setCellValueFactory(new PropertyValueFactory<>("trackNumber"));
            track.setCellFactory(new CFI());

            TableColumn<Metadata,String> genre = new TableColumn<>("Genre");
            genre.setMinWidth(200);
            genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
            genre.setCellFactory(new CFG());

            TableColumn<Metadata,Integer> year  = new TableColumn<>("Year");
            year.setMinWidth(100);
            year.setCellValueFactory(new PropertyValueFactory<>("date"));
            year.setCellFactory(new CFD());
            //private URL copyrightInformation;  publisherWebpage ;rating ;playCounter
            ObservableList<Integer> options = FXCollections.observableArrayList(
                    1,2,3,4,5
            );
            final ComboBox comboBox = new ComboBox(options);


            TableColumn<Metadata,String> rating  = new TableColumn<>("Rating");
            rating.setMinWidth(100);
            rating.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Metadata, String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Metadata, String> meta){
                    if(meta.getValue().getRating().isPresent()) {
                        String result ="";
                        for(int i = 0;i<meta.getValue().getRating().get();i++){
                            result = result +"*";
                        }
                        return new SimpleStringProperty(result);
                    } else{
                        return new SimpleStringProperty("0");
                    }
                }
            });
            //https://www.youtube.com/watch?v=SNkY1yekN8E
            ObservableList<String> masterData3 = FXCollections.observableArrayList();
            masterData3.add("1");
            masterData3.add("2");
            masterData3.add("3");
            masterData3.add("4");
            masterData3.add("5");

            rating.setCellFactory(ComboBoxTableCell.forTableColumn(masterData3));

            rating.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Metadata,String>>(){

                public void handle(TableColumn.CellEditEvent<Metadata,String> event){
                    //System.out.println(table.getSelectionModel().getSelectedIndex()+"Index");
                    gui.ratingDatabase(table.getSelectionModel().getSelectedIndex(),Integer.parseInt(event.getNewValue()));
                   // System.out.println("Value : " + event.getNewValue());
                 }
            });
            table.setEditable(true);

            TableColumn<Metadata,String> counter  = new TableColumn<>("PlayCounter");
            counter.setMinWidth(100);
            counter.setCellValueFactory(new PropertyValueFactory<>("playCounter"));
            counter.setCellFactory(new CFI());

            TableColumn<Metadata,String> webpage  = new TableColumn<>("Webpage");
            webpage.setMinWidth(200);
            webpage.setCellValueFactory(new PropertyValueFactory<>("publisherWebpage"));
            webpage.setCellFactory(new CFU());

            TableColumn<Metadata,String> copy  = new TableColumn<>("Copyright Information");
            copy.setMinWidth(200);
            copy.setCellValueFactory(new PropertyValueFactory<>("copyrightInformation"));
            copy.setCellFactory(new CFU());


            table.getColumns().addAll(picture,title,artist,album,track,genre,year,rating,counter,webpage,copy);
            table.setColumnResizePolicy(UNCONSTRAINED_RESIZE_POLICY);
            table.setMinHeight(500);
            ObservableList<Metadata> dataset = FXCollections.observableArrayList();
            for(Song s :songs){
                dataset.add(s.getMetadata());
            }
            table.setItems(dataset);
            //table.setStyle(".table-row-cell {-fx-cell-size: 50px;}");
        } catch (Exception e) {
            //AlertBox.display("Error",e.getMessage());
        }


        return table;
    }
}
