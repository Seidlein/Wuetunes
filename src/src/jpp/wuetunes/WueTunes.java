package jpp.wuetunes;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jpp.wuetunes.*;

import jpp.wuetunes.gui.gui;
import jpp.wuetunes.io.database.DatabaseConnection;
import jpp.wuetunes.model.Song;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.Metadata;

import static javafx.application.Application.launch;
import static jpp.wuetunes.gui.gui.launch;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
public class WueTunes extends Application{

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        gui.launch();
    }
}
