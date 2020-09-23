package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Metadata;

import java.time.LocalDate;
import java.util.Optional;

public class CFI<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<Integer>>> {

    @Override
    public TableCell<T, Optional<Integer>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<Integer>>() {

            @Override
            protected void updateItem(Optional<Integer> item, boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setText(null);
                    return;
                }
                if(item.isPresent()){
                    setText(Integer.toString(item.get()));}

                else{
                    setText("");
                }
            }

        };
    }

}
