package jpp.wuetunes.gui;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.geometry.*;

public class AlertBox {

    public static void display(String title, String message ) {

        Platform.runLater(new Runnable() {

            public void run() {Stage window = new Stage();

                //Block events to other windows
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle(title);
                window.setMinWidth(650);
                window.setMinHeight(200);


                Label labelWL = new Label();
                labelWL.setText(message);



                Button closeButton = new Button("Alright got it");
                closeButton.setOnAction(e -> window.close());

                VBox layout = new VBox(10);
                layout.getChildren().addAll(labelWL, closeButton);
                layout.setAlignment(Pos.CENTER);

                //Display window and wait for it to be closed before returning
                Scene scene = new Scene(layout);
                window.setScene(scene);
                window.showAndWait();



            }});}}