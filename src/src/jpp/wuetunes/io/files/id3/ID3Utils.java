package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

public class ID3Utils {

    static byte[] synchronize(byte[] bytes) {
        // synchronisation is replacing instances of:
        // 11111111 00000000 111xxxxx with 11111111 111xxxxx and
        // 11111111 00000000 00000000 with 11111111 00000000
        int count = sizeSynchronisationWouldSubtract(bytes);
        if (count == 0) return bytes;
        byte[] newBuffer = new byte[bytes.length - count];
        int i = 0;
        for (int j = 0; j < newBuffer.length - 1; j++) {
            newBuffer[j] = bytes[i];
            if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
                i++;
            }
            i++;
        }
        newBuffer[newBuffer.length - 1] = bytes[i];
        return newBuffer;
    }

    private static int sizeSynchronisationWouldSubtract(byte[] bytes) {
        int count = 0;
        for (int i = 0; i < bytes.length - 2; i++) {
            if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
                count++;
            }
        }
        if (bytes.length > 1 && bytes[bytes.length - 2] == (byte) 0xff && bytes[bytes.length - 1] == 0) count++;
        return count;
    }
    static String byteToBitString(byte b){
        String result = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        return result;
    }
    static String intToBitString(int value){
        String result = Integer.toBinaryString(value);
        int dif = 32-result.length();
        for(int i = 0;i<dif;i++){
            result ="0"+result;
        }
        return result;
    }
    static boolean readFlag(byte value, int flagPosition){
        Validate.requireBetween(flagPosition,0,7);
        String s =ID3Utils.byteToBitString(value);
        if(s.charAt(s.length()-1-flagPosition) == '1'){
            return true;
        }else{
            return false;
        }
    }
    static int read32BitSynchsafeInteger(byte[] data){
        Validate.requireNonNull(data);
        if(data.length!=4){
            throw new IllegalArgumentException("Bitsynchsafefehler");
        }
        String str = "";
        for(int i = 0;i<data.length;i++){
            String s = ID3Utils.byteToBitString(data[i]);
            s=s.substring(1,s.length());
            str = str+s;
        }
        return Integer.parseInt(str,2);
    }

}