package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.Metadata;

import java.time.LocalDate;
import java.util.Optional;

public class CFG<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<Genre>>> {

    @Override
    public TableCell<T, Optional<Genre>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<Genre>>() {

            @Override
            protected void updateItem(Optional<Genre> item, boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setText(null);
                    return;
                }
                if(item.isPresent()){
                    setText(item.get().getName());}
                else{
                    setText("");
                }
            }

        };
    }

}
