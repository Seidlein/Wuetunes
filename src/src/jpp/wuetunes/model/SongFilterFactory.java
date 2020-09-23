package jpp.wuetunes.model;

import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.util.Validate;
import jpp.wuetunes.model.metadata.*;
import java.util.Collection;
import java.util.function.Predicate;

public class SongFilterFactory {
    public static Predicate<Song> getArtistFilter(String artist){
        Validate.requireNonNull(artist);
        Predicate<Song> p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                if(!song.getMetadata().getArtist().isPresent()){
                    return false;
                }
                if(song.getMetadata().getArtist().get().equals(artist)){
                    return true;
                }else{
                    return false;
                }
            }
        };
        return p;
    }
    public static Predicate<Song> getAlbumTitleFilter(String albumTitle){
        Validate.requireNonNull(albumTitle);
        Predicate<Song> p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                if(!song.getMetadata().getAlbumTitle().isPresent()) {
                    return false;
                }
                if(song.getMetadata().getAlbumTitle().get().equals(albumTitle)){
                    return true;
                }else{
                    return false;
                }
            }
        };
        return p;
    }
    public static Predicate<Song> getGenreFilter(Genre genre){
        Validate.requireNonNull(genre);
        Predicate<Song> p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                if(song.getMetadata().getGenre().isPresent()&&song.getMetadata().getGenre().get().equals(genre)){
                    return true;
                }else{
                    return false;
                }
            }
        };
        return p;
    }
    public static Predicate<Song> getMinRatingFilter(int rating){
        Validate.requireBetween(rating,0,5);
        Predicate<Song> p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                if(song.getMetadata().getRating().isPresent()&&song.getMetadata().getRating().get()>= rating){
                    return true;
                }else{
                    return false;
                }
            }
        };
        return p;
    }
    public static Predicate<Song> combineAnd(Collection<Predicate<Song>> filters){
        Validate.requireNonNull(filters);
        if(filters.size()==0){
            throw new IllegalArgumentException();
        }
        Predicate<Song> p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                boolean result = true;
                for(Predicate<Song> c:filters){
                    if(!c.test(song)){
                        result = false;
                    }
                }
                return result;
            }
        };
        return p;
    }
    public static Predicate<Song> combineOr(Collection<Predicate<Song>> filters){
        Validate.requireNonNull(filters);
        if(filters.size()==0){
            throw new IllegalArgumentException();
        }
        Predicate<Song>p = new Predicate<Song>() {
            @Override
            public boolean test(Song song) {
                boolean result = false;
                for(Predicate<Song>c:filters){
                    if(c.test(song)){
                        result = true;
                    }
                }
                return result;
            }
        };
        return p;
    }
}
