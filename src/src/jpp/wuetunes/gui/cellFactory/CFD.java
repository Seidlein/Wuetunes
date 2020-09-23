package jpp.wuetunes.gui.cellFactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jpp.wuetunes.model.metadata.Metadata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.util.Optional;

public class CFD<T> implements Callback<TableColumn<T, Metadata>, TableCell<T, Optional<Temporal>>> {

    @Override
    public TableCell<T, Optional<Temporal>> call(TableColumn<T, Metadata> col) {
        return new TableCell<T, Optional<Temporal>>() {

            @Override
            protected void updateItem(Optional<Temporal> item, boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setText(null);
                    return;
                }
                if(item.isPresent()){
                    YearMonth y = YearMonth.of(2,2);
                    LocalDate da = LocalDate.of(5,5,5);
                    LocalDateTime t = LocalDateTime.of(3,2,2,2,2);
                    Year y0 = Year.of(4);
                    String result ="";

                    setText(item.get().toString().substring(0,4));}
                else{
                    setText("");
                }
            }

        };
    }

}
