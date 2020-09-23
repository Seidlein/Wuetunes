package jpp.wuetunes.model.metadata;

import jpp.wuetunes.util.Validate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MetadataPicture {
    private String mimeType;
    private String description;
    private byte[] data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetadataPicture that = (MetadataPicture) o;

        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = mimeType != null ? mimeType.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    public MetadataPicture(String mimeType, String description, byte[] data){
        if(mimeType == null||description == null|| data == null){
            throw new IllegalArgumentException();
        }
        Validate.requireValidImageMimeType(mimeType);
        if(data == null ||data.length ==0){
            throw new IllegalArgumentException();
        }else {
            this.mimeType = mimeType;
            this.description = description;
            this.data = data.clone();
        }
    }
    public String getMimeType(){
        String result = mimeType;
        return result;
    }
    public String getDescription(){
        String result = description;
        return result;
    }
    public byte[] getData(){
        byte[] result = data.clone();
        return result;
    }
    public String toString(){

        String result = "MIME type: \""+mimeType+"\", \""+description+"\", "+data.length+" bytes";
        return result;
    }

}
