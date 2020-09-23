package jpp.wuetunes.model.metadata;

import jpp.wuetunes.util.Validate;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.util.Optional;

public class Metadata {
    private String songTitle;
    private boolean songT;
    private String artist;
    private boolean art;
    private String albumTitle;
    private int trackNumber;
    private boolean trackN;
    private Genre genre;
    private Temporal date;
    private MetadataPicture picture;
    private URL copyrightInformation;
    private URL publisherWebpage;
    private int rating;
    private boolean rat;
    private int playCounter;
    private boolean play;

    public void setSongTitle(String songTitle) {
        if (songTitle == null) {
            throw new IllegalArgumentException();
        }
        songT = true;
        this.songTitle = songTitle;
    }

    public Optional<String> getSongTitle() {
        Optional<String> opt;
        if (songT) {
            opt = Optional.of(songTitle);
        } else {
            opt = Optional.empty();
        }
        return opt;
    }

    public void setArtist(String artist) {
        if (artist == null) {
            throw new IllegalArgumentException();
        }
        art =true;
        this.artist = artist;
    }

    public Optional<String> getArtist() {
        Optional<String> opt;
        if (art) {
            opt = Optional.of(artist);
        } else {
            opt = Optional.empty();
        }
        return opt;
    }

    public void setAlbumTitle(String albumTitle) {
        if (albumTitle == null) {
            throw new IllegalArgumentException();
        }
        this.albumTitle = albumTitle;
    }

    public Optional<String> getAlbumTitle() {
        Optional<String> opt;
        if (albumTitle == null || albumTitle.length() == 0) {
            opt = Optional.empty();
        } else {
            opt = Optional.of(albumTitle);
        }
        return opt;
    }

    public void setTrackNumber(int trackNumber) {
        trackN = true;
        if (trackNumber < 0) {
            throw new IllegalArgumentException("tracknumer <0: "+trackNumber);
        } else {
            this.trackNumber = trackNumber;
        }

    }

    public Optional<Integer> getTrackNumber() {
        Optional<Integer> opt;
        if (trackN) {
            return opt = Optional.of(trackNumber);
        } else return opt = Optional.empty();
    }

    public void setGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException();
        }
        this.genre = genre;
    }

    public Optional<Genre> getGenre() {
        Optional<Genre> opt;
        if (genre == null) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(genre);
    }

    public void setDate(Temporal date) {
        if (date == null) {
            throw new IllegalArgumentException();
        }
        this.date = date;
    }

    public Optional<Temporal> getDate() {
        Optional<Temporal> opt;
        if (date == null) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(date);
    }

    public void setPicture(MetadataPicture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = picture;
    }

    public Optional<MetadataPicture> getPicture() {
        Optional<MetadataPicture> opt;
        if (picture == null) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(picture);
    }

    public void setCopyrightInformation(URL copyrightInformation) {
        if (copyrightInformation == null) {
            throw new IllegalArgumentException();
        }
        this.copyrightInformation = copyrightInformation;
    }

    public Optional<URL> getCopyrightInformation() {
        Optional<URL> opt;
        if (copyrightInformation == null) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(copyrightInformation);
    }

    public void setPublisherWebpage(URL publisherWebpage) {
        if (publisherWebpage == null) {
            throw new IllegalArgumentException();
        }
        this.publisherWebpage = publisherWebpage;
    }

    public Optional<URL> getPublisherWebpage() {
        Optional<URL> opt;
        if (publisherWebpage == null) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(publisherWebpage);
    }

    public void setRating(int rating) {
        rat = true;
        Validate.requireBetween(rating, 0, 5);
        this.rating = rating;
    }

    public Optional<Integer> getRating() {

        Optional<Integer> opt;
        if (!rat) {
            return opt = Optional.empty();
        }

        return opt = Optional.of(rating);
    }

    public void setPlayCounter(int playCounter) {
        play = true;
        Validate.requireNonNegative(playCounter);
        this.playCounter = playCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metadata metadata = (Metadata) o;

        if (trackNumber != metadata.trackNumber) return false;
        if (rating != metadata.rating) return false;
        if (playCounter != metadata.playCounter) return false;
        if (songTitle != null ? !songTitle.equals(metadata.songTitle) : metadata.songTitle != null) return false;
        if (artist != null ? !artist.equals(metadata.artist) : metadata.artist != null) return false;
        if (albumTitle != null ? !albumTitle.equals(metadata.albumTitle) : metadata.albumTitle != null) return false;
        if (genre != null ? !genre.equals(metadata.genre) : metadata.genre != null) return false;
        if (date != null ? !date.equals(metadata.date) : metadata.date != null) return false;
        if (picture != null ? !picture.equals(metadata.picture) : metadata.picture != null) return false;
        if (copyrightInformation != null ? !copyrightInformation.equals(metadata.copyrightInformation) : metadata.copyrightInformation != null)
            return false;
        if (publisherWebpage != null ? !publisherWebpage.equals(metadata.publisherWebpage) : metadata.publisherWebpage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = songTitle != null ? songTitle.hashCode() : 0;
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (albumTitle != null ? albumTitle.hashCode() : 0);
        result = 31 * result + trackNumber;
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (copyrightInformation != null ? copyrightInformation.hashCode() : 0);
        result = 31 * result + (publisherWebpage != null ? publisherWebpage.hashCode() : 0);
        result = 31 * result + rating;
        result = 31 * result + playCounter;
        return result;
    }

    public Optional<Integer> getPlayCounter() {
        Optional<Integer> opt;
        if (!play) {
            return opt = Optional.empty();
        }
        return opt = Optional.of(playCounter);
    }

    public String toString() {
        String result = "";
        if (getSongTitle().isPresent()) {
            result = "Song title: " + songTitle + "\n";
        }
        if (getArtist().isPresent()) {
            result = result + "Artist: " + artist + "\n";
        }
        if (getAlbumTitle().isPresent()) {
            result = result + "Album title: " + albumTitle + "\n";
        }
        if (getTrackNumber().isPresent()) {
            result = result + "Track number: " + trackNumber + "\n";
        }
        if (getGenre().isPresent()) {
            result = result + "Genre: " + genre + "\n";
        }
        if (getDate().isPresent()) {
            YearMonth y = YearMonth.of(2,2);
            LocalDateTime t = LocalDateTime.of(3,2,2,2,2);
            Year y0 = Year.of(4);
            if(date.getClass().equals(t.getClass())) {

                String[] s = date.toString().split("-");
                result = result + "Date: " + s[0] + "\n";
            }else if(y.getClass().equals(date.getClass())){
                String[] s = date.toString().split("-");
                result = result + "Date: " + s[0] + "\n";
            }else if(y0.getClass().equals(date.getClass())){

                result = result + "Date: " + date.toString() + "\n";
            }else{
                String[] s = date.toString().split("-");
                result = result + "Date: " + s[0] + "\n";
            }


        }
        if (getPicture().isPresent()) {
            result = result + "Picture: " + picture.toString() + "\n";
        }
        if (getCopyrightInformation().isPresent()) {
            result = result + "Copyright information: " + copyrightInformation.toString() + "\n";
        }
        if (getPublisherWebpage().isPresent()) {
            result = result + "Publisher webpage: " + publisherWebpage.toString() + "\n";
        }
        if (getRating().isPresent()) {
            String stern = "";
            for(int i = 0;i<rating;i++){
                stern = stern +"*";
            }
            result = result + "Rating: " + stern + "\n";
        }
        if (getPlayCounter().isPresent()) {
            result = result + "Play counter: " + playCounter + "\n";
        }
        if(result.length()>0){
            result = result.substring(0,result.length()-1);
        }

        return result;
    }
}
