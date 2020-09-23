package jpp.wuetunes.io.files.id3;
import jpp.wuetunes.util.Validate;

import java.net.URL;
public class ID3FrameBodyURLLink implements ID3FrameBody{
    private URL url;
    ID3FrameBodyURLLink(URL url){
        if(url ==null){
            throw new IllegalArgumentException("urllink null");
        }
        this.url = url;
    }
    @Override
    public String dataToString() {
        String result = "[ID3FrameBody URL \"" +url.toString()+"\"]";

        return result;
    }
    public URL getUrl(){
        return url;
    }

}
