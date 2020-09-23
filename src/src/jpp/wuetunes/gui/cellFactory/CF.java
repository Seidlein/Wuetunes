package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Metadata;

import java.time.LocalDate;
import java.util.Optional;

public class CF<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<String>>> {

    @Override
    public TableCell<T, Optional<String>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<String>>() {

            @Override
            protected void updateItem(Optional<String> item, boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setText(null);
                    return;
                }
                if(item.isPresent()){
                setText(item.get());}
                else{
                    setText("");
                }
            }

        };
    }

}
