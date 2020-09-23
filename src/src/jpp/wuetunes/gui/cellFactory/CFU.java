package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Metadata;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

public class CFU<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<URL>>> {

    @Override
    public TableCell<T, Optional<URL>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<URL>>() {

            @Override
            protected void updateItem(Optional<URL> item, boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setText(null);
                    return;
                }
                if(item.isPresent()){
                    setText(item.get().toString());}
                else{
                    setText("");
                }
            }

        };
    }

}
