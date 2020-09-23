package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

public class ID3FrameBodyAttachedPicture implements ID3FrameBody {
    private String mimeTyp;
    private int imageType;
    private String description;
    private byte[] data;

    ID3FrameBodyAttachedPicture(String mimeType, int imageType, String description, byte[] data){
        Validate.requireBetween(imageType,0,20);
        Validate.requireValidImageMimeType(mimeType);
        Validate.requireNonNullNotEmpty(data);
        Validate.requireNonNull(mimeType);
        Validate.requireNonNull(description);
        this.mimeTyp = mimeType;
        this.imageType =imageType;
        this.description = description;
        this.data = data.clone();
    }

    public String getMimeType(){
        return mimeTyp;
    }

    public int getImageType() {
        return imageType;
    }

    public String getDescription() {
        return description;
    }
    public byte[] getPictureData(){
        return data.clone();
    }


    @Override
    public String dataToString() {

        String result = "[ID3FrameBody Picture MIME type: \""+mimeTyp+"\", "+"image type: "+imageType+", ";
        result = result +"\"" +description +"\", " +data.length +" bytes, " +"picture hash: " +java.util.Arrays.hashCode(data)+"]";
        return result;
    }
}
