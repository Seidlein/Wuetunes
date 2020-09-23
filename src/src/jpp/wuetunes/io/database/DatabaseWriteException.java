package jpp.wuetunes.io.database;

public class DatabaseWriteException extends IllegalStateException {
    public DatabaseWriteException(String message){
        super(message);
    }
    public DatabaseWriteException(String message,Throwable cause){
        super(message,cause);

    }
}
