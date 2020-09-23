package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.util.Validate;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.*;

public class ID3TagReader {
    private static ID3Encoding readEncoding(byte encoding, byte bom0, byte bom1) throws ID3TagReaderException {
        if (encoding == 0) {

            return new ID3Encoding(StandardCharsets.ISO_8859_1, false, 1);
        } else if (encoding == 1) {
            if (bom0 == (byte) 0xFE && bom1 == (byte) 0xFF) {
                return new ID3Encoding(StandardCharsets.UTF_16BE, true, 2);
            }
            if (bom0 == (byte) 0xFF && bom1 == (byte) 0xFE) {
                return new ID3Encoding(StandardCharsets.UTF_16LE, true, 2);
            }
        } else if (encoding == 2) {
            return new ID3Encoding(StandardCharsets.UTF_16BE, false, 2);
        } else if (encoding == 3) {
            return new ID3Encoding(StandardCharsets.UTF_8, false, 1);
        }
        throw new ID3TagReaderException("Bytes_define_noEncoding" + encoding + " " + bom0 + " " + bom1);
    }

    private static ID3TagHeader readTagHeader(byte[] data) throws ID3TagReaderException {
        Validate.requireNonNull(data);
        if (data.length != 10) {
            throw new IllegalArgumentException("datalength_wrong_id3tagreader");
        }
        if (data.length == 10) {
            if (data[0] != (byte) 0x49 || data[1] != (byte) 0x44 || data[2] != (byte) 0x33) {
                throw new ID3TagReaderException("wrong_first_3_bytes_ID3TagReader");
            }
        }
        //ID3TagHeader(int majorVersion, int revision, boolean flagUnsynchronisation, boolean flagExtendedHeader,
        // boolean flagExperimentalIndicator, boolean flagFooterPresent, int tagSize){
        byte majorVersion = data[3];
        byte revision = data[4];
        byte flags = data[5];
        String flag = ID3Utils.byteToBitString(flags);
        boolean flagExperimentalIndicator = false;
        boolean flagFooterPresent = false;
        boolean flagUnsynchronisation = false;
        boolean flagExtendedHeader = false;
        if (flag.charAt(0) == '1') {
            flagUnsynchronisation = true;
        }
        if (flag.charAt(1) == '1') {
            flagExtendedHeader = true;
        }
        if (flag.charAt(2) == '1') {
            flagExperimentalIndicator = true;
        }
        if (flag.charAt(3) == '1') {
            flagFooterPresent = true;
        }
        byte[] synch = {data[6], data[7], data[8], data[9]};
        int tagSize = ID3Utils.read32BitSynchsafeInteger(synch);

        return new ID3TagHeader(majorVersion, revision, flagUnsynchronisation, flagExtendedHeader, flagExperimentalIndicator, flagFooterPresent, tagSize);
    }

    //private
    private static int readFrame(byte[] framesData, int offset, List<ID3Frame> frames) throws ID3TagReaderException {
        Validate.requireNonNull(framesData);
        Validate.requireNonNull(frames);
        for (byte b : framesData) {
            //System.out.println(b);
        }

        /*Es soll eine ID3TagReaderException geworfen werden, wenn
        ein das Encoding definierendes Byte nicht den erlaubten Werten entspricht oder
        das ein BOM ungültige Werte hat.*/
        // ID3Frame frame;
        char[] c = {(char) framesData[offset], (char) framesData[offset + 1], (char) framesData[offset + 2], (char) framesData[offset + 3]};
        String str = new String(c);
        StringBuilder sb = new StringBuilder();
        int size = ID3Utils.read32BitSynchsafeInteger(new byte[]{framesData[offset + 4], framesData[offset + 5], framesData[offset + 6], framesData[offset + 7]});
        if (ID3FrameType.getByIdentifier(str).isPresent()) {

            ID3FrameType id3FrameType = ID3FrameType.getByIdentifier(str).get();
            ID3FrameContentType contentType = id3FrameType.getContentType();
            ID3FrameHeader id3FrameHeader = new ID3FrameHeader(id3FrameType, size, framesData[offset + 8], framesData[offset + 9]);
            //System.out.println(id3Encoding.getCharset().name() + id3Encoding.isWithBOM() + contentType.toString());

            if (contentType.equals(ID3FrameContentType.TEXT_INFORMATION)) { //    ID3FrameBodyTextInformation(String text)
                ID3Encoding id3Encoding = ID3TagReader.readEncoding(framesData[10 + offset], framesData[11 + offset], framesData[12 + offset]);

                int bom = 0;
                int numNull = id3Encoding.getNumNullBytes();

                if (id3Encoding.isWithBOM()) {
                    bom = 2;
                }
                int result = 0;
                result = result + offset + 11 + bom;
                boolean test = false;
                for (int i = 11 + bom + offset; i < framesData.length; i++) {
                    if (i < framesData.length - 1) {


                        if (framesData[i] == 0 && framesData[i + 1] == 0) {

                            test = true;
                            if (i < framesData.length - 2) {
                                if (i + 2 < framesData.length && framesData[i + 2] == 0) {
                                    result++;
                                }
                            }
                            result = result + 2;
                            break;
                        }
                    }
                    if (framesData[i] != 0) {
                        sb.append((char) framesData[i]);
                    } else if (numNull == 1) {
                        break;
                    }

                    result++;
                }
                if (!test) {
                    result++;
                }

                ID3FrameBodyTextInformation id3FrameBodyTextInformation = new ID3FrameBodyTextInformation(sb.toString());
                ID3Frame frame1 = new ID3Frame(id3FrameHeader, id3FrameBodyTextInformation);
                frames.add(frame1);
                //System.out.println("Offset: " +offset+ " Result: "+result);
                return result;

            } else if (contentType.equals(ID3FrameContentType.URL_LINK)) {
                int result;

                StringBuilder sb1 = new StringBuilder();
                for (int i = 10 + offset; i < framesData.length; i++) {
                    if (framesData[i] != 0) {
                        sb1.append((char) framesData[i]);
                    } else {
                        result = i + 1;
                        break;
                    }
                    if (i < framesData.length - 4) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append((char) framesData[i + 1]);
                        sb4.append((char) framesData[i + 2]);
                        sb4.append((char) framesData[i + 3]);
                        sb4.append((char) framesData[i + 4]);
                        if (ID3FrameType.getByIdentifier(sb4.toString()).isPresent()) {
                            break;
                        }
                    }
                }
                ID3FrameBodyURLLink id3FrameBodyURLLink;
                ID3Frame frame;
                try {
                    URL url = new URL(sb1.toString());
                    id3FrameBodyURLLink = new ID3FrameBodyURLLink(url);
                    frame = new ID3Frame(id3FrameHeader, id3FrameBodyURLLink);
                    frames.add(frame);
                } catch (MalformedURLException e) {
                    throw new ID3TagReaderException("url_is_not_valid: " + sb1.toString());
                }

            } else if (contentType.equals(ID3FrameContentType.ATTACHED_PICTURE)) {
                if (id3FrameHeader.isFlagUnsynchronization()) {
                    int result =0;
                    byte[] data = framesData.clone();
                    //byte[] data = new byte[size];
                    int start = offset + 10;

                    StringBuilder sb1 = new StringBuilder();
                    /*
                     for (int j = 10; j < data.length; j++) {

                     data[j] = framesData[offset + j];

                     }*/

                    //data=Arrays.copyOf(framesData,offset+10,offset+10+size);
                    //byte[] synch = ID3Utils.synchronize(data);
                    int anfang = start;
                    //beachten des flaglengthindicators
                    if (id3FrameHeader.isFlagDataLengthIndicator() && anfang > 0) {

                        anfang = anfang + 4;
                    }
                    //encoding besimmen
                    byte encoding = data[anfang];
                    anfang++;
                    //mimeType berechnen
                    int counter = anfang;
                    for (int i = counter; i < data.length; i++) {
                        if (i > 0 && data[i] != 0) {
                            sb.append((char) data[i]);
                        } else {
                            break;
                        }
                        counter++;
                    }
                    String mimeType = sb.toString();
                    int image = counter + 1;
                    int imagetype = data[image];
                    int encode = image + 1;
                    byte bom0 = 0;
                    byte bom1 = 0;
                    if (encode > 0) {
                        bom0 = data[encode++];
                        bom1 = data[encode];
                    }

                    ID3Encoding id3Encoding = ID3TagReader.readEncoding(encoding, bom0, bom1);

                    int bom = 0;
                    int nNullByte = id3Encoding.getNumNullBytes();
                    //bom beachten
                    if (id3Encoding.isWithBOM()) {
                        bom = 2;
                    }
                    //beschreibung bestimmen
                    StringBuilder sb2 = new StringBuilder();
                    int zaehler = 2;
                    boolean test = false;
                    int index = 0;
                    for (int i = image + 1; i < data.length; i++) {
                        if (i < data.length - 1) {


                            if (data[i] == 0 && data[i + 1] == 0) {

                                test = true;
                                for (int cd = 0; cd < data.length; cd++) {
                                    zaehler++;
                                    if (i < data.length - 2) {
                                        if (i + 2 < data.length && data[i + 2] == 0) {
                                            if (zaehler > 2) {
                                                i++;
                                            }
                                        }
                                    }

                                }

                                i = i + 2;
                                index = i;
                                break;
                            }
                        }
                        if (data[i] != 0) {     //keine leeren bytes anhaengen
                            sb2.append((char) data[i]);
                        } else if (nNullByte == 1) {
                            index = i;
                            break;
                        }


                    }
                    if (!test) {
                        index++;
                    }
                    String description = sb2.toString();

                    //ByteBuffer bb = ByteBuffer.allocate(framesData.length - result);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //bildrohdaten bestimmen
                    byte[] pic = new byte[size - (index - start)];

                    for (int i = 0; i < pic.length; i++) {
                        pic[i] = data[index++];
                    }
                    /*System.out.println("MimeType: " + mimeType);
                    System.out.println("Encoding: " + id3Encoding.getCharset().name());
                    System.out.println("ImageType: " + imagetype);
                    System.out.println("Beschreibung: " + description);
                    System.out.println("Length: " + framesData.length);

                    System.out.println(id3FrameHeader.isFlagUnsynchronization());
                       */

                    ID3FrameBodyAttachedPicture id3FrameBodyAttachedPicture = new ID3FrameBodyAttachedPicture(mimeType, imagetype, description, ID3Utils.synchronize(pic));
                    ID3Frame frame1 = new ID3Frame(id3FrameHeader, id3FrameBodyAttachedPicture);
                    frames.add(frame1);
                    result = offset + 10 + size;

                    return result;
                } else {
                    int result = 0;
                    if (id3FrameHeader.isFlagDataLengthIndicator()) {

                        result = result + 4;
                    }
                    StringBuilder sb1 = new StringBuilder();
                    byte encoding = framesData[10 + offset];
                    result++;
                    for (int i = 11 + offset; i < framesData.length; i++) {
                        if (framesData[i] != 0) {
                            sb.append((char) framesData[i]);
                        } else {
                            result = result + i + 1;
                            break;
                        }
                    }
                    String mimeType = sb.toString();
                    // System.out.println("MimeType: "+mimeType);
                    int imagetype = framesData[result - 1];
                    result++;
                    byte bom0 = 0;
                    byte bom1 = 0;
                    if (encoding == 1) {
                        bom0 = framesData[result - 1];
                        result++;
                        bom1 = framesData[result - 1];
                        result++;
                    }
                    ID3Encoding id3Encoding = ID3TagReader.readEncoding(encoding, bom0, bom1);
                    //System.out.println(id3Encoding.getCharset().name());
                    int bom = 0;
                    int numNull = id3Encoding.getNumNullBytes();

                    if (id3Encoding.isWithBOM()) {
                        bom = 2;
                    }

                    StringBuilder sb2 = new StringBuilder();
                    boolean test = false;
                    for (int i = result - 1; i < framesData.length; i++) {
                        if (i < framesData.length - 1) {


                            if (framesData[i] == 0 && framesData[i + 1] == 0) {

                                test = true;
                                if (i < framesData.length - 2) {
                                    if (i + 2 < framesData.length && framesData[i + 2] == 0) {
                                        result++;
                                    }
                                }
                                result = result + 2;
                                break;
                            }
                        }
                        if (framesData[i] != 0) {
                            sb2.append((char) framesData[i]);
                        } else if (numNull == 1) {
                            break;
                        }

                        result++;
                    }
                    if (!test) {
                        result++;
                    }
                    String description = sb2.toString();
                    //System.out.println(imagetype);
                    //System.out.println(description);
                    //System.out.println(framesData.length);

                    //  System.out.println(id3FrameHeader.isFlagUnsynchronization());
                    ByteBuffer bb = ByteBuffer.allocate(framesData.length - result);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();


                    byte[] picturedata = new byte[framesData.length - result];

                    for (int i = result - 1; i < framesData.length; i++) {
                        baos.write(new byte[]{framesData[i]}, 0, 1);
                        result++;
                    }
                    result--;

                    ID3FrameBodyAttachedPicture id3FrameBodyAttachedPicture = new ID3FrameBodyAttachedPicture(mimeType, imagetype, description, baos.toByteArray());
                    ID3Frame frame1 = new ID3Frame(id3FrameHeader, id3FrameBodyAttachedPicture);
                    frames.add(frame1);
                    return result;
                }


            }

        }


        return size + offset + 10;
    }


    public static ID3Tag read(Path path) throws ID3TagReaderException {
        Validate.requireNonNull(path);
        Validate.requireFileExists(path);
        try {
            byte[] data = Files.readAllBytes(path);
            if (data[0] != 0x49 || data[1] != 0x44 || data[2] != 0x33) {
                throw new ID3TagReaderException("headindetifier");
            }
            byte[] size = {data[6], data[7], data[8], data[9]};
            int si = ID3Utils.read32BitSynchsafeInteger(size);
            ID3TagHeader id3TagHeader = new ID3TagHeader(data[3], data[4], ID3Utils.readFlag(data[5], 1), ID3Utils.readFlag(data[5], 2), ID3Utils.readFlag(data[5], 3), ID3Utils.readFlag(data[5], 4), si);
            if (data[3] != 4 || data[4] != 0) {
                List<ID3Frame> list = new ArrayList<>();
                ID3Tag id3tag = new ID3Tag(id3TagHeader, list);
                return id3tag;
            } else {
                List<ID3Frame> list = new ArrayList<>();
                int offset = 10;
                for (int i = 10; i < id3TagHeader.getTagSize(); i++) {
                    if (i < data.length) {
                        if (i < id3TagHeader.getTagSize() - 4) {
                            if (data[i] == 0x00 && data[i + 1] == 0x00 && data[i + 2] == 0x00 && data[i + 3] == 0x00) {
                                break;
                            }
                        }
                        try {
                            ID3TagReader.readFrame(data, i, list);
                        } catch (ID3TagReaderException e) {
                            throw new ID3TagReaderException("sth went wrong reading the frame");
                        }
                    }
                }
                ID3Tag id3tag = new ID3Tag(id3TagHeader, list);
                return id3tag;
            }
        } catch (IOException e) {
            throw new ID3TagReaderException("PathtobyteArray went wrong");
        }

    }


}