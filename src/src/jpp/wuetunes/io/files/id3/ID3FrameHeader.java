package jpp.wuetunes.io.files.id3;

public class ID3FrameHeader {
    private  ID3FrameType type;
    private int size;
    private byte statusFlags;
    private byte formatFlags;
    ID3FrameHeader(ID3FrameType type, int size, byte statusFlags, byte formatFlags){
        if(size<=0){
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.size =size;
        this.formatFlags =formatFlags;
        this.statusFlags =statusFlags;
    }
    public boolean isFlagDataLengthIndicator(){
        String s= ID3Utils.byteToBitString(formatFlags);
        if(s.charAt(7)=='1'){
            return true;
        }else{
            return false;
        }
    }
    public boolean isFlagUnsynchronization(){
        String s= ID3Utils.byteToBitString(formatFlags);
        if(s.charAt(6)=='1'){
            return true;
        }else{
            return false;
        }
    }
    public ID3FrameType getFrameType(){
        return type;
    }
    public int getSize(){
        return size;
    }
    public String toString(){
        String result = "";
        result = result +"[ID3FrameHeader " + type +", "+ "size: " +size+", "+"status flags: "+ID3Utils.byteToBitString(statusFlags)+", ";
        result= result + "format flags: "+ID3Utils.byteToBitString(formatFlags)+", "+ "data length indicator: "+this.isFlagDataLengthIndicator()+", ";
        result = result + "unsynchronization: "+this.isFlagUnsynchronization()+"]";
        return result;
    }

}
