package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

public class ID3TagHeader {
    private int majorVersion;
    private int revision;
    private boolean flagUnsynchronisation =false;
    private boolean flagExtendedHeader =false;
    private boolean flagExperimentalIndicator =false;
    private boolean flagFooterPresent = false;
    private int tagSize;
    ID3TagHeader(int majorVersion, int revision, boolean flagUnsynchronisation, boolean flagExtendedHeader, boolean flagExperimentalIndicator, boolean flagFooterPresent, int tagSize){
        Validate.requireNonNegative(majorVersion);
        Validate.requireNonNegative(revision);
        Validate.requireNonNegative(tagSize);

        this.majorVersion =majorVersion;
        this.revision =revision;
        this.flagUnsynchronisation = flagUnsynchronisation;
        this.flagExperimentalIndicator = flagExperimentalIndicator;
        this.flagExtendedHeader = flagExtendedHeader;
        this.flagFooterPresent = flagFooterPresent;
        this.tagSize =tagSize;
    }
    public int getMajorVersion(){
        return majorVersion;
    }
    public int getRevision(){
        return revision;
    }
    public boolean isFlagUnsynchronisation(){
        return flagUnsynchronisation;
    }
    public boolean isFlagExtendedHeader(){
        return flagExtendedHeader;
    }
    public boolean isFlagExperimentalIndicator(){
        return flagExperimentalIndicator;
    }
    public boolean isFlagFooterPresent(){
        return flagFooterPresent;
    }
    public int getTagSize(){
        return tagSize;
    }
    public String toString(){
        String result = "[ID3TagHeader version: ID3v2."+majorVersion+"."+revision+", unsynchronisation: "+flagUnsynchronisation;
        result =result +", extended header: "+flagExtendedHeader+", experimental indicator: "+flagExperimentalIndicator+", footer present: "+ flagFooterPresent +", tag size: "+tagSize+" bytes]";
        return result;
    }



}
