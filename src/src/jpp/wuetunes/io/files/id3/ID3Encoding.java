package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

import java.nio.charset.Charset;

public class ID3Encoding {
    private Charset charSet;
    private boolean withBOM;
    private int numNullBytes;

    ID3Encoding(Charset charSet, boolean withBOM, int numNullBytes){
        Validate.requireNonNull(charSet);
        if(numNullBytes >2 ||numNullBytes<1){
            throw new IllegalArgumentException("numNullBytes");
        }
        this.charSet = charSet;
        this.withBOM = withBOM;
        this.numNullBytes = numNullBytes;
    }
    public Charset getCharset(){
        return charSet;
    }
    public boolean isWithBOM(){
        return withBOM;
    }

    public int getNumNullBytes() {
        return numNullBytes;
    }
}
