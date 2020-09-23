package jpp.wuetunes.io.database;

public class DatabaseOpenException extends  IllegalStateException{
    public DatabaseOpenException(String message){
        super(message);
    }
    public DatabaseOpenException(String message,Throwable cause){
        super(message,cause);
    }
}
