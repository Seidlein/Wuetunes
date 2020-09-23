package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

public class ID3TagReaderException extends java.io.IOException {
    ID3TagReaderException(String message){
        super(message);
        Validate.requireNonNullNotEmpty(message);
    }
    ID3TagReaderException(String message, Throwable cause){
        super(message,cause);
        Validate.requireNonNullNotEmpty(message);
        Validate.requireNonNull(cause);
    }
}
