package jpp.wuetunes.io.database;

public class InconsistentDatabaseException extends IllegalStateException {
    public InconsistentDatabaseException(String message){
        super(message);
    }
    public InconsistentDatabaseException(String message,Throwable cause){
        super(message,cause);
    }
}
