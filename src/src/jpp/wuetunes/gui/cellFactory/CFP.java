package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.model.metadata.MetadataPicture;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Optional;

public class CFP<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<MetadataPicture>>> {

    @Override
    public TableCell<T, Optional<MetadataPicture>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<MetadataPicture>>() {

            @Override
            protected void updateItem(Optional<MetadataPicture> item, boolean empty) {
                super.updateItem(item, empty);
                setMinHeight(50);
                if ((item == null) || empty) {
                    ImageView imageView = new ImageView();
                    imageView.setImage(null);
                    setGraphic(imageView);
                    return;
                }
                if(item.isPresent()) {
                    Image img = new Image(new ByteArrayInputStream(item.get().getData()));
                    ImageView imageView = new ImageView(img);
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);
                }
                else{
                    ImageView imageView = new ImageView();
                    imageView.imageProperty().set(null);
                    setGraphic(imageView);
                }
            }

        };
    }

}
