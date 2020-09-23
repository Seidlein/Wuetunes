package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

import java.util.*;

public class ID3Tag {
    private ID3TagHeader header;
    private ArrayList<ID3Frame> frames;

    ID3Tag(ID3TagHeader header, List<ID3Frame> frames){
        Validate.requireNonNull(header);
        Validate.requireNonNull(frames);
        this.header = header;
        this.frames = new ArrayList<ID3Frame>(frames);
    }
    public ID3TagHeader getHeader() {
        return header;
    }
    public List<ID3Frame> getFrames() {
        return frames;
    }

    public List<ID3Frame> getOrderedFrames(ID3FrameType type){
        Validate.requireNonNull(type);
        ArrayList<ID3Frame> result = new ArrayList<>();
        for(ID3Frame f :frames){
            if(f.getHeader().getFrameType().equals(type)){
                result.add(f);
            }
        }
        return result;
    }
    public Optional<ID3Frame> getFrameByIdentifier(ID3FrameType type){
        Validate.requireNonNull(type);
        Optional<ID3Frame> opt;
        ArrayList<ID3Frame> result = new ArrayList<>(this.getOrderedFrames(type));
        if(result.size() ==0){
            return  opt =Optional.empty();
        }else{
            return opt = Optional.of(result.get(0));
        }
    }
    public String toString(){
        String result =header.toString()+"\n";
        for(ID3Frame f :frames){
            result = result + f.toString()+"\n";
        }
        result = result.substring(0,result.length()-1);
        return result;
    }

}
