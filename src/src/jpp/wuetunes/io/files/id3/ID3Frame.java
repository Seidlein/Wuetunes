package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

public class ID3Frame<B extends ID3FrameBody>   {
    private ID3FrameHeader header;
    private ID3FrameBody body;

    ID3Frame(ID3FrameHeader header, B body){
        Validate.requireNonNull(header);
        Validate.requireNonNull(body);
        this.header = header;
        this.body = (B) body;
    }
    public B getBody() {
        return (B) body;
    }

    public ID3FrameHeader getHeader() {
        return header;
    }
    public String toString(){
        String result = header.toString()+body.dataToString();
        return result;
    }


}
