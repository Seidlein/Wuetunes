package jpp.wuetunes.model;

import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.util.Validate;

import java.nio.file.Path;

public class Song implements Comparable<Song> {
    Path filePath;
    Metadata metadata;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return filePath != null ? filePath.equals(song.filePath) : song.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }

    public Song(Path filePath, Metadata metadata){
        Validate.requireFileExists(filePath);
        String name = filePath.getFileName().toString();
        if(!name.endsWith(".mp3")){
           throw new IllegalArgumentException();
        }
        if(filePath ==null|| metadata == null){
            throw new IllegalArgumentException();
        }
        this.filePath = filePath;
        this.metadata = metadata;
    }

    public Path getFilePath(){
        return filePath;
    }
    public String toString(){
        String result = "Filename: "+ filePath.getFileName()+"\n"+metadata.toString();
        return result;
    }
    public Metadata getMetadata(){
        return metadata;
    }





    @Override
    public int compareTo(Song o) {
        String artist = "";
        if (metadata.getArtist().isPresent()) {
            artist = metadata.getArtist().get();
        }
        String oArtist = "";
        if (o.metadata.getArtist().isPresent()) {
            oArtist = o.metadata.getArtist().get();
        }
        String albumTitle = "";
        if (metadata.getAlbumTitle().isPresent()) {
            albumTitle = metadata.getAlbumTitle().get();
        }
        String oAlbumTitle = "";
        if (o.metadata.getAlbumTitle().isPresent()) {
            oAlbumTitle = o.metadata.getAlbumTitle().get();
        }
        int trackNumber = 0;
        if (metadata.getTrackNumber().isPresent()) {
            trackNumber = metadata.getTrackNumber().get();
        }
        int oTrackNumber = 0;
        if (o.metadata.getTrackNumber().isPresent()) {
            oTrackNumber = o.metadata.getTrackNumber().get();
        }
        String songTitle = "";
        if (metadata.getSongTitle().isPresent()) {
            songTitle = metadata.getSongTitle().get();
        }
        String oSongTitle = "";
        if (o.metadata.getSongTitle().isPresent()) {
            oSongTitle = o.metadata.getSongTitle().get();
        }

        if (artist.compareTo(oArtist) == 0) {
            if (albumTitle.compareTo(oAlbumTitle) == 0) {
                if (trackNumber == oTrackNumber) {
                    return songTitle.compareTo(oSongTitle);
                }
                Integer i = new Integer(trackNumber);
                Integer i0 = new Integer(oTrackNumber);
                return i.compareTo(i0);
            } else {
                return albumTitle.compareTo(oAlbumTitle);
            }
        } else {
            return artist.compareTo(oArtist);
        }
    }
}
