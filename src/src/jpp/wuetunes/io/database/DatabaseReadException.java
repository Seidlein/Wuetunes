package jpp.wuetunes.io.database;

public class DatabaseReadException extends IllegalStateException {
    public DatabaseReadException(String message){
        super(message);
    }
    public DatabaseReadException(String message,Throwable cause){
        super(message,cause);

    }
}
