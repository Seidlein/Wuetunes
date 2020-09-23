package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

import java.util.Optional;

public enum ID3FrameType {
    TPE1, TIT2, TALB, TCON, TDRC, TRCK, WCOP, WPUB, APIC;

    public ID3FrameContentType getContentType(){
       if(this.equals(TPE1)||this.equals(TIT2)||this.equals(TALB)||this.equals(TCON)||this.equals(TDRC)||this.equals(TRCK)){
           return ID3FrameContentType.TEXT_INFORMATION;
       }else if(this.equals(WCOP)||this.equals(WPUB)){
           return ID3FrameContentType.URL_LINK;
       }else return ID3FrameContentType.ATTACHED_PICTURE;
    }
    static Optional<ID3FrameType> getByIdentifier(String identifier){
        Validate.requireNonNull(identifier);
        Optional<ID3FrameType> opt ;
        switch (identifier){
            case "TPE1": return opt = Optional.of(TPE1);
            case "TIT2": return opt = Optional.of(TIT2);
            case "TALB": return opt = Optional.of(TALB);
            case "TCON": return opt = Optional.of(TCON);
            case "TDRC": return opt = Optional.of(TDRC);
            case "TRCK": return opt = Optional.of(TRCK);
            case "WCOP": return opt = Optional.of(WCOP);
            case "WPUB": return opt = Optional.of(WPUB);
            case "APIC": return opt = Optional.of(APIC);
            default:
                return opt = Optional.empty();

        }
    }
}

