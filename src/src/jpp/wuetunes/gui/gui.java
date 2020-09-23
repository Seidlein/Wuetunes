package jpp.wuetunes.gui;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jpp.wuetunes.io.database.DatabaseConnection;
import jpp.wuetunes.io.database.InconsistentDatabaseException;
import jpp.wuetunes.io.files.SongsFileImporter;
import jpp.wuetunes.model.Song;
import jpp.wuetunes.model.SongFilterFactory;
import jpp.wuetunes.model.SongLibrary;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.GenreManager;
import jpp.wuetunes.model.metadata.Metadata;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class gui extends Application {
    boolean rechts = true;
    static  HBox h;
    static MediaPlayer mediaPlayer;
    private static VBox player;
    boolean sh = false;
    ArrayList<Song> s = new ArrayList<>();
    private static final Button next = new Button();
    private static final Button prev = new Button();
    private static final Button stop = new Button();
    final static Button shuffle = new Button();
    static DatabaseConnection d;
    final static GridPane g = new GridPane();
    static TableView<Metadata> table;
    TableRow<Metadata> links;
    static Image img;
    static ImageView imageView;
    Song test;
    static Text ti;
    static MediaControl mediaControl;
    static TableView<Metadata> playlist;
    Set<Song> son = new HashSet<>();
    private static StackPane scrollPane;
    private static SongLibrary lib = new SongLibrary();
    static jpp.wuetunes.model.Playlist p = new jpp.wuetunes.model.Playlist();
    public gui(){

    }

    //von https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows/28606524
    // Leicht abgeaendert.
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public void start(Stage primaryStage) throws Exception {
        ti = new Text();
        try {
            ti.setStyle("#fancytext {\n" +
                    "    -fx-font: 800px Tahoma;\n" +
                    "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
                    "    -fx-stroke: black;\n" +
                    "    -fx-stroke-width: 1;\n" +
                    "}");
        } catch (Exception e) {

        }

        //ti.setStyle("-fx-stroke: blue;");
        LinkedHashSet<Song> databaseSongs = null;
        try {
            d = new DatabaseConnection();
            d.open();
            //System.out.println(d.loadGenres());
            ArrayList<Song> order = new ArrayList<>(d.loadSongs());
            Collections.sort(order);
            lib.addAll(order);
            databaseSongs = new LinkedHashSet<>(order);
        } catch (Exception e) {
            AlertBox.display("Error",e.getMessage());
        }

        playlist = jpp.wuetunes.gui.Playlist.createContent(null);
        table = jpp.wuetunes.gui.table.createContent(databaseSongs);

        table.setEditable(true);

        table.setRowFactory(tv -> {
            TableRow<Metadata> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    rechts = true;
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            playlist.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {

                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);


                        event.consume();

                }
            });
            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);


                        event.consume();
                    }
                }
            });
            row.setOnDragDropped(event ->{
                    Dragboard db = event.getDragboard();
                    if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                        int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                        Metadata draggedPerson = playlist.getItems().remove(draggedIndex);
                        if(p.getCurrentSong().isPresent()){
                            if(p.getCurrent().get()==draggedIndex){
                                mediaPlayer.stop();
                            }
                        }
                        p.removeAt(draggedIndex);
                        next.fire();
                        event.setDropCompleted(true);
                        event.consume();
                    }
            });

            playlist.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if(rechts){
                        //System.out.println("copy");
                        int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                        //remove
                        Metadata draggedPerson = table.getItems().get(draggedIndex);
                        if(mediaPlayer!=null&&mediaPlayer.getCurrentTime().toMillis()>2){
                            mediaPlayer.stop();
                        }
                        for(Song s :lib.getSongs()){
                            if(s.getMetadata().equals(draggedPerson)){
                                //p.addAt(draggedIndex,s);
                                p.addAt(0,s);
                                break;
                            }
                        }
                        //System.out.println("180");
                        p.setCurrent(0);
                        //System.out.println(p.getSongs());
                        int dropIndex ;
                        //System.out.println("184");
                        if (row.isEmpty()) {
                            dropIndex = playlist.getItems().size() ;
                        } else {
                            dropIndex = row.getIndex();
                        }

                        playlist.getItems().add(0, draggedPerson);
                        //System.out.println("192");
                        event.setDropCompleted(true);
                        playlist.getSelectionModel().select(p.getCurrent().get());
                        //System.out.println("195");
                        if(p.getCurrentSong().isPresent()) {
                            g.getChildren().remove(player);
                            //System.out.println("198");
                            unt(g,p.getCurrentSong().get().getFilePath().toString(),ti );
                            //System.out.println("200");
                        }
                        event.consume();
                    }else{
                        //System.out.println("test");

                        int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                        //remove
                        Metadata draggedPerson = playlist.getItems().remove(draggedIndex);
                        String f = "";
                        for(Song s:lib.getSongs()){
                            if(s.getMetadata().equals(draggedPerson)){
                                f = s.getFilePath().toString();
                            }
                        }
                        Song s = new Song(Paths.get(f),draggedPerson);
                        int dropIndex =playlist.getItems().size();
                        playlist.getItems().add(dropIndex, draggedPerson);
                        p.removeAt(draggedIndex);
                        p.addAt(p.getSongs().size(),s);
                        //g.getChildren().remove(player);
                        //unt(g,p.getCurrentSong().get().getFilePath().toString(),ti);
                        next.fire();
                        mediaPlayer.setAutoPlay(false);
                        event.setDropCompleted(true);
                        event.consume();
                    }
                }
            });


            return row ;
        });
        playlist.setRowFactory(tv -> {
            TableRow<Metadata> row = new TableRow<>();
            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    rechts = false;
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });


            links =row;
            return row ;
        });
        scrollPane  = new StackPane(table);

        //g.setGridLinesVisible(true);
        Button load = new Button("Load Library");
        load.setMinSize(120, 50);
        Button delete = new Button("Empty Database");
        delete.setMinSize(120,50);
        Text t = new Text();
        t.setText("Filters: ");
        t.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        TextField ArtistText = new TextField();
        ArtistText.setPromptText("Artist");
        TextField AlbumText = new TextField();
        AlbumText.setPromptText("Album Title");
        TextField GenreText = new TextField();
        GenreText.setPromptText("Genre ID");
        TextField MinRatText = new TextField();
        MinRatText.setPromptText("Minimum Rating");
        TextField YearText = new TextField();
        YearText.setPromptText("Year");
        Button filter = new Button("Apply Filter");

        VBox vBox = new VBox(load,t,ArtistText,AlbumText,GenreText,MinRatText,YearText,filter,delete);
        delete.setOnAction(e->{
            d = new DatabaseConnection();
            d.open();
            d.delete();
            d.close();

        });
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMinSize(300,400);
        g.add(vBox,2,0);
        ArrayList<Genre>genres = new ArrayList<>();
        SongsFileImporter songsFileImporter = new SongsFileImporter(genres);
        filter.setOnAction(e->{
            try {
                ObservableList<Metadata> dataset2 = FXCollections.observableArrayList();
                String artist = ArtistText.getCharacters().toString();
                String albumTitle = AlbumText.getCharacters().toString();
                int genre = Integer.MIN_VALUE;
                int year = Integer.MIN_VALUE;
                int minimumRating = Integer.MIN_VALUE;
                try {
                    if(MinRatText.getCharacters() !=null&&MinRatText.getCharacters().length() !=0){
                        minimumRating = Integer.parseInt(MinRatText.getCharacters().toString());
                    }} catch (NumberFormatException e1) {
                    AlertBox.display("Error",MinRatText.getCharacters().toString() +"Rating was not a Number");
                }
                try {
                    if(GenreText.getCharacters() !=null&&GenreText.getCharacters().length() !=0){
                        genre = Integer.parseInt(GenreText.getCharacters().toString());
                    }} catch (NumberFormatException e1) {
                    AlertBox.display("Error",GenreText.getCharacters().toString() +"Genre was not a Number");
                }
                try {
                    if(YearText.getCharacters() !=null&&YearText.getCharacters().length() !=0){
                        year = Integer.parseInt(YearText.getCharacters().toString());
                    }} catch (NumberFormatException e1) {
                    AlertBox.display("Error",YearText.getCharacters().toString() +"Year was not a Number");
                }
                s.addAll(lib.getSongs());
                LinkedHashSet n = new LinkedHashSet();
                if(artist!=null&&artist.length()!=0) {
                    n = new LinkedHashSet(lib.getSongs(SongFilterFactory.getArtistFilter(artist)));
                    lib = new SongLibrary(n);
                }
                if(albumTitle!=null&&albumTitle.length()!=0){
                    n= new LinkedHashSet(lib.getSongs(SongFilterFactory.getAlbumTitleFilter(albumTitle)));
                    lib = new SongLibrary(n);
                }
                if(genre !=Integer.MIN_VALUE){
                    Genre g = new Genre(genre,"wont matter");
                    n= new LinkedHashSet(lib.getSongs(SongFilterFactory.getGenreFilter(g)));
                    lib = new SongLibrary(n);
                }
                if(minimumRating!=Integer.MIN_VALUE){
                    n= new LinkedHashSet(lib.getSongs(SongFilterFactory.getMinRatingFilter(minimumRating)));
                    lib = new SongLibrary(n);
                }
                if(year!=Integer.MIN_VALUE){
                    LinkedHashSet<Song> lin = new LinkedHashSet<>();
                    for(Song s: lib.getSongs()){
                        if(s.getMetadata().getDate().isPresent()){
                            if(Integer.parseInt(s.getMetadata().getDate().get().toString().substring(0,4)) == year){
                                lin.add(s);
                            }
                        }
                    }
                    n= new LinkedHashSet(lin);
                    lib = new SongLibrary(n);
                }
                if(artist.length()==0&&albumTitle.length()==0&&genre==Integer.MIN_VALUE&&minimumRating==Integer.MIN_VALUE&&year==Integer.MIN_VALUE){
                    n = new LinkedHashSet(s);
                    lib = new SongLibrary(s);
                }
                Set<Metadata> fi = new HashSet<>();

                for(Song s : lib.getSongs()){
                    fi.add(s.getMetadata());
                }
                ObservableList list = FXCollections.observableArrayList(fi);
                TableView<Metadata> filtered = jpp.wuetunes.gui.table.createContent(n);
                table.getItems().removeAll();
                table.setItems(list);

            } catch (Exception e0) {
                //System.out.println("364");
            }
        });

        load.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File dir = directoryChooser.showDialog(primaryStage);
            try {
                //System.out.println(dir.toPath()+"77");
                ArrayList<Song> songs = new ArrayList<>(songsFileImporter.importSongsFromFolder(dir.toPath()).getSongs());
                Collections.sort(songs);
                lib.addAll(songs);
                ObservableList<Metadata> dataset = FXCollections.observableArrayList();
                GenreManager genreManager = new GenreManager();
                for(Song s :songs){
                    dataset.add(s.getMetadata());
                    son.add(s);
                }
                try {
                    d.writeSongs(son);
                } catch (Exception e1) {
                }
                int richtig = songs.size();
                int falsch = songsFileImporter.importSongsFromFolder(dir.toPath()).getFailures().size();
                AlertBox.display("FileImport","Anzahl richtig eingelesener Musikst\u00FCcke: "+ richtig + "\n\n"+"Anzahl falsch eingelesener Musikst\u00FCcke: "+ falsch);
                //table = jpp.wuetunes.gui.table.createContent(new LinkedHashSet<>(lib.getSongs()));
                table.setItems(dataset);
                table.setEditable(true);
                scrollPane = new StackPane(table);

                g.getChildren().removeAll(scrollPane);
                g.add(scrollPane,1,0);
            } catch (IOException e1) {
                //System.out.println("err"+e.getSource());
                //e1.printStackTrace();
                AlertBox.display("Error",e1.getMessage());
            } catch (NullPointerException n){
                //n.printStackTrace();
                AlertBox.display("Error",n.getMessage());
            }
        });

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(70);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(25);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(40);
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(25);
        g.getRowConstraints().addAll(row1);
        g.getColumnConstraints().addAll(column1,column2,column3);
        if(p.getCurrentSong().isPresent()) {
            unt(g,p.getCurrentSong().get().getFilePath().toString(),ti );
        }

        g.add(playlist,0,0);
        g.add(scrollPane,1,0);
        g.setPrefSize(1400,700);



        Scene scene = new Scene(g);
        primaryStage.setTitle("WueTunes");
        primaryStage.setScene(scene);
        primaryStage.show();

        next.setOnAction(e-> {
            if(mediaPlayer!= null){
                mediaPlayer.stop();
            }
            g.getChildren().remove(player);
            g.getChildren().remove(h);
            p.next();
            String file = p.getCurrentSong().get().getFilePath().toString();
            unt(g,file,ti);
            mediaPlayer.setAutoPlay(true);
            playlist.getSelectionModel().select(p.getCurrent().get());
        });
        stop.setOnAction(e->{
            mediaPlayer.stop();
            MediaControl.playi.setText(">");
        });
        prev.setOnAction(e->{
            if(mediaPlayer!= null){
                mediaPlayer.stop();
            }
            //System.out.println("prev");
            if(mediaPlayer.getCurrentTime().toMillis()>3000){
                mediaPlayer.stop();
                mediaPlayer.play();
            }else {
                g.getChildren().remove(player);
                g.getChildren().remove(h);
                p.previous();
                String file = p.getCurrentSong().get().getFilePath().toString();
                unt(g, file, ti);
                mediaPlayer.setAutoPlay(true);
                playlist.getSelectionModel().select(p.getCurrent().get());
            }
        });
        //g.setStyle("-fx-background-color: #FF00F0");
        shuffle.setOnAction(e->{
            try {
                if(sh==false){
                    p.setShuffle(true);
                    sh = true;
                    //System.out.println(sh);
                    shuffle.setStyle("-fx-background-color: #99b3ff");//setBackground(new Background(new BackgroundFill(javafx.scene.paint.Paint.valueOf("00ff00"), null, null)));
                }else{
                    p.setShuffle(false);
                    sh = false;
                    shuffle.setStyle("-fx-background-color: #E6E6E6");
                    //System.out.println(sh);
                }
            } catch (Exception e1) {
                AlertBox.display("Error Shuffle",e1.getMessage());
            }
        });
    }
    public static void end(){
        if(mediaPlayer!= null){
            mediaPlayer.stop();
        }
        //System.out.println("end");
        g.getChildren().remove(player);
        g.getChildren().remove(h);
        p.next();
        String file = p.getCurrentSong().get().getFilePath().toString();
        unt(g,file,ti);
        mediaPlayer.setAutoPlay(true);
        playlist.getSelectionModel().select(p.getCurrent().get());
    }
    public static void unt(GridPane g ,String filepath,Text ti){
        Media media = null;
        try {
            media = new Media(new File(filepath).toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        }
       //mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaControl = new MediaControl(mediaPlayer);
        //mediaControl.setPrefWidth(750);
        mediaControl.setPrefHeight(300);
        if(p.getCurrentSong().get().getMetadata().getSongTitle().isPresent())
            ti.setText(p.getCurrentSong().get().getMetadata().getSongTitle().get());
        else{
            ti.setText("");
        }
        VBox medi = new VBox(mediaControl);
        ti.setStyle(" -fx-font: 42px Tahoma;\n" +
                "    -fx-fill: linear-gradient(from 0% 0% to 100% 100%, repeat, aqua 0%, red 50%);\n" +
                "    -fx-stroke: black;\n" +
                "    -fx-stroke-width: 1;");
        player = new VBox(ti,medi);
        player.setAlignment(Pos.TOP_CENTER);
        next.setPrefSize(80,80);
        next.setText("\u23E9");
        next.setStyle("-fx-font: 32 arial;");
        stop.setPrefSize(80,80);
        stop.setText("\u2B1B");
        stop.setStyle("-fx-font: 32 arial;");
        if(p.getCurrentSong().get().getMetadata().getPicture().isPresent()) {
            img = new Image(new ByteArrayInputStream(p.getCurrentSong().get().getMetadata().getPicture().get().getData()));
            imageView = new ImageView(img);
        }else{
            imageView = new ImageView();
        }
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        shuffle.setText("\uD83D\uDD00");
        shuffle.setPrefSize(80,80);
        h = new HBox(imageView,shuffle);
        h.setSpacing(40);
        h.setAlignment(Pos.CENTER_LEFT);
        g.add(h,0,1);
        prev.setPrefSize(80,80);
        prev.setText("\u23EA");
        prev.setStyle("-fx-font: 32 arial;");
        HBox ne =  new HBox(prev,stop,next);
        ne.setAlignment(Pos.BOTTOM_LEFT);
        ne.setSpacing(10);
        HBox unten = new HBox(player);
        g.add(ne,2,1);
        g.add(unten,1,1,1,1);
        medi.prefWidthProperty().bind(scrollPane.widthProperty());
    }

    public static void ratingDatabase(int pos,int n){
        Metadata ma = table.getItems().get(pos);
        //System.out.println(ma);
        for(Song s: lib.getSongs()){
            if(s.getMetadata().equals(ma)){
                s.getMetadata().setRating(n);
                try {
                    d = new DatabaseConnection();
                    d.open();
                    d.writeSong(s);
                    d.close();
                } catch (Exception e) {
                    //System.out.println("572");
                }
            }
        }
    }
    public static void playCounterDatabase(){
        try {
            Metadata ma  = playlist.getItems().get(p.getCurrent().get());
            int pos = 0;
            for(int k = 0; k<table.getItems().size();k++){
                if(table.getItems().get(k).equals(ma)){
                    pos = k;
                }
            }
            int old =0;
            Path filepath;
            for(Song s : lib.getSongs()){
                if(s.getMetadata().equals(ma)){
                    if(s.getMetadata().getPlayCounter().isPresent()){
                        old = s.getMetadata().getPlayCounter().get();
                    }
                    filepath = s.getFilePath();
                    ma.setPlayCounter(old+1);
                    Metadata n = ma;
                    Song song = new Song(filepath,n);
                    table.getItems().set(pos,n);
                    try {
                        d = new DatabaseConnection();
                        d.open();
                        d.writeSong(song);
                        d.close();
                    } catch (Exception e) {
                        //System.out.println("604");
                    }
                }
            }
        } catch (Exception e) {
            AlertBox.display("Error",e.getMessage());
        }

    }
    public void test(){

    }



    public static void launch(){
        gui g = new gui();
        Stage primaryStage = new Stage();
        try {
            g.start(primaryStage);
        } catch (Exception e) {
            //e.printStackTrace();
            AlertBox.display("Error starting GUI",e.getMessage());
        }
    }

}