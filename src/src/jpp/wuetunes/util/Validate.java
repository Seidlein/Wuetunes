package jpp.wuetunes.util;
import java.nio.file.*;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static int requireNonNegative(int n){
        if(n <0){
            throw new IllegalArgumentException();
        }else{
            return n;
        }
    }
    public static int requireBetween(int n, int min, int max){
        if(n > max || n< min){
            throw new IllegalArgumentException();
        }else{
            return n;
        }
    }
    public static int requireLowerThan(int n ,int max){
        if(n< max){
            return n;
        }else{
            throw new IllegalArgumentException();
        }
    }
    public static <O> O requireNonNull(O o){
        if(o == null){
            throw new IllegalArgumentException();
        }
        else{
            return o;
        }
    }
    public static String requireNonNullNotEmpty(String s){
        if(s == null|| s.isEmpty()){
            throw new IllegalArgumentException();
        }else{
            return s;
        }
    }
    public static String requireLength(String s, int length){
        if(s ==null||s.isEmpty()|| s.length() != length){
            throw new IllegalArgumentException();
        }else{
            return s;
        }
    }
    public static Path requireFileExists(Path path){
        if(path == null||Files.notExists(path) ){
            throw new IllegalArgumentException();
        }else{
            return path;
        }
    }
    public static byte[] requireNonNullNotEmpty(byte[] bytes){
        if(bytes == null|| bytes.length == 0){
            throw new IllegalArgumentException();
        }else{
            return bytes;
        }
    }
    public static byte[] requireLength(byte[] bytes, int length){
        if(bytes == null||bytes.length== 0||bytes.length!=length){
            throw new IllegalArgumentException();
        }else{
            return bytes;
        }
    }
    public static <C extends Collection> C requireNonNullNotEmpty(C c){
        if(c == null|| c.size()==0){
            throw new IllegalArgumentException();
        }else{
            return c;
        }
    }
    public static String requireValidImageMimeType(String mimeType){

        String s = "";
        String a = "";
        Pattern p = Pattern.compile("^[a-z0-9-.]+$");
        if(mimeType == null){
            throw new IllegalArgumentException();
        }
        if(mimeType.length()<6){
            throw new IllegalArgumentException("81");
        }else{
            s = mimeType.substring(6,mimeType.length());
            a = mimeType.substring(0, 6);
            Matcher m = p.matcher(s);
            if(m.matches()&& a.equals( "image/")&&s.length()<128){
                return mimeType;
            }else{
                throw new IllegalArgumentException("88"+ ":"+ s+a);
            }
        }
    }






}
