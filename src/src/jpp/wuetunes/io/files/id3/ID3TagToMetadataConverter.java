package jpp.wuetunes.io.files.id3;

import jpp.wuetunes.io.files.id3.ID3Tag;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.GenreManager;
import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.model.metadata.MetadataPicture;
import jpp.wuetunes.util.Validate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ID3TagToMetadataConverter {
    private ArrayList<Genre> genres;
    private GenreManager genreManager;
    public ID3TagToMetadataConverter(){
        this.genreManager = new GenreManager();
    }
    public ID3TagToMetadataConverter(Collection<Genre> genres){
        this.genreManager = new GenreManager(genres);
    }

    public Metadata convert(ID3Tag tag){
        Metadata result = new Metadata();
        Validate.requireNonNull(tag);
        ArrayList<ID3Frame> frames = new ArrayList<>(tag.getFrames());
        if(tag.getFrameByIdentifier(ID3FrameType.TIT2).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation )tag.getFrameByIdentifier(ID3FrameType.TIT2).get().getBody();
            result.setSongTitle(i.getText());
        }
        if(tag.getFrameByIdentifier(ID3FrameType.TPE1).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation )tag.getFrameByIdentifier(ID3FrameType.TPE1).get().getBody();
            result.setArtist(i.getText());
        }
        if(tag.getFrameByIdentifier(ID3FrameType.TALB).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation )tag.getFrameByIdentifier(ID3FrameType.TALB).get().getBody();
            result.setAlbumTitle(i.getText());
        }
        if(tag.getFrameByIdentifier(ID3FrameType.TCON).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation )tag.getFrameByIdentifier(ID3FrameType.TCON).get().getBody();
            //System.out.println("Converter Genre: "+i.getText());
            try {
                int id =Integer.parseInt(i.getText());
                if(genreManager.getGenreById(id).isPresent()){
                    result.setGenre(genreManager.getGenreById(id).get());
                }else{
                    Genre g = new Genre(id,"unknown genre "+id);
                    genreManager.add(g);
                    result.setGenre(g);
                }
            } catch (NumberFormatException e) {
                if(genreManager.getGenreByName(i.getText()).isPresent()){
                    Genre g =genreManager.getGenreByName(i.getText()).get();
                    result.setGenre(g);
                }else{

                    result.setGenre(genreManager.add(i.getText()));
                }
            }

        }
        if(tag.getFrameByIdentifier(ID3FrameType.TDRC).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation )tag.getFrameByIdentifier(ID3FrameType.TDRC).get().getBody();
            if(i.getText().length() ==4&&i.getText().matches("[0-9---:]+")){
                Year y = Year.of(Integer.parseInt(i.getText()));
                result.setDate(y);
            }
            if(i.getText().length()==7&&i.getText().matches("[0-9---:]+")){
                YearMonth y = YearMonth.of(Integer.parseInt(i.getText().substring(0,4)),Integer.parseInt(i.getText().substring(5,7)));
                result.setDate(y);
            }
            if(i.getText().length()==10&&i.getText().matches("[0-9---:]+")){
                Integer y = Integer.parseInt(i.getText().substring(0,4));
                Integer m = Integer.parseInt(i.getText().substring(5,7));
                Integer d = Integer.parseInt(i.getText().substring(8,10));
                LocalDate l = LocalDate.of(y,m,d);
                result.setDate(l);
            }
            if(i.getText().length()==19){
                try {
                    Integer y = Integer.parseInt(i.getText().substring(0,4));
                    Integer m = Integer.parseInt(i.getText().substring(5,7));
                    Integer d = Integer.parseInt(i.getText().substring(8,10));
                    Integer h = Integer.parseInt(i.getText().substring(11,13));
                    Integer mm = Integer.parseInt(i.getText().substring(14,16));
                    Integer ss = Integer.parseInt(i.getText().substring(17,19));
                    LocalDateTime l = LocalDateTime.of(y,m,d,h,mm,ss);
                    result.setDate(l);
                } catch (NumberFormatException e) {

                }
            }

        }
        if(tag.getFrameByIdentifier(ID3FrameType.TRCK).isPresent()) {
            ID3FrameBodyTextInformation i = (ID3FrameBodyTextInformation) tag.getFrameByIdentifier(ID3FrameType.TRCK).get().getBody();
            try {
                Integer in =Integer.parseInt(i.getText());
                if(in>0) {
                    result.setTrackNumber(in);
                }else{
                    result.setTrackNumber(0);
                }
            } catch (NumberFormatException e) {
                result.setTrackNumber(0);
            }
        }
        if(tag.getFrameByIdentifier(ID3FrameType.WCOP).isPresent()) {
            ID3FrameBodyURLLink i = (ID3FrameBodyURLLink) tag.getFrameByIdentifier(ID3FrameType.WCOP).get().getBody();
            result.setCopyrightInformation(i.getUrl());
        }
        if(tag.getFrameByIdentifier(ID3FrameType.WPUB).isPresent()) {
            ID3FrameBodyURLLink i = (ID3FrameBodyURLLink) tag.getFrameByIdentifier(ID3FrameType.WPUB).get().getBody();
            result.setPublisherWebpage(i.getUrl());
        }
        if(tag.getFrameByIdentifier(ID3FrameType.APIC).isPresent()) {
            boolean drei = false;
            for(ID3Frame f :tag.getOrderedFrames(ID3FrameType.APIC)){
                ID3FrameBodyAttachedPicture pic = (ID3FrameBodyAttachedPicture)f.getBody();
                if(pic.getImageType()==3){
                    MetadataPicture mpic = new MetadataPicture(pic.getMimeType(),pic.getDescription(),pic.getPictureData());
                    result.setPicture(mpic);
                    drei = true;
                }
            }
            if(!drei) {
                ID3FrameBodyAttachedPicture pic = (ID3FrameBodyAttachedPicture) tag.getFrameByIdentifier(ID3FrameType.APIC).get().getBody();
                MetadataPicture mpic = new MetadataPicture(pic.getMimeType(), pic.getDescription(), pic.getPictureData());
                result.setPicture(mpic);
            }
        }

        return result;
    }
}
