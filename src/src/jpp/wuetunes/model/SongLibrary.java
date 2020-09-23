package jpp.wuetunes.model;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import jpp.wuetunes.model.metadata.*;

public class SongLibrary {
    Collection<Song> songs;
    public SongLibrary(){
        songs = new ArrayList<Song>();
    }
    public SongLibrary(Collection<Song> songs){
        if(songs == null){
            throw new IllegalArgumentException("songs nullin SongLibrary");
        }
        this.songs = new ArrayList<Song>(songs);
    }
    public void add(Song song){
        if(song== null){
            throw new IllegalArgumentException();
        }

        boolean contain = false;
        for(Song s: songs){
            if(s.equals(song)){
                contain = true;
            }
        }
        if(!contain){
            songs.add(song);
        }
    }
    public void addAll(Collection<Song> songs){
        if(songs == null){
            throw new IllegalArgumentException();
        }
        if(this.songs==null){
            this.songs = new ArrayList<Song>();
            for(Song s :songs){
                if(s !=null){
                    this.songs.add(s);
                }
            }
            return;
        }
        for(Song s :songs){
            if(s !=null){
                this.songs.add(s);
            }
        }

    }
    public boolean remove(Song song){
        ArrayList<Song> removeSong = new ArrayList<>();
        if(song ==null||songs ==null){
            throw new IllegalArgumentException();
        }
        if(songs.size()==0){
            return false;
        }
        boolean contain = false;
        for(Song s :songs){
            if(song.equals(s)&&s!=null){
                removeSong.add(s);
                contain = true;
            }
        }
        this.songs.removeAll(removeSong);
        return contain;
    }
    public boolean removeAll(Collection<Song> songs){
        ArrayList<Song> removeSong = new ArrayList<>();
        if(songs == null||songs ==null){
            throw new IllegalArgumentException();
        }
        if(songs.size()==0){
            return false;
        }
        boolean contain = false;
        for(Song s :this.songs){
            for(Song s0 :songs){
                if(s!=null&&s.equals(s0)){
                    removeSong.add(s);
                    contain = true;
                }
            }
        }
        this.songs.removeAll(removeSong);
        return contain;
    }
    public Set<Song> getSongs(){

        LinkedHashSet<Song> result =new LinkedHashSet<Song>() ;
        if(songs == null){
            return result;
        }
        if(songs.size()== 0){
            return result;
        }else{
            result.addAll(songs);
        }
        return result;
    }
    public Set<Song> getSongs(Predicate<Song> filter){
        LinkedHashSet<Song> result =new LinkedHashSet<Song>() ;
        if(songs == null){
            return result;
        }
        if(songs.size()== 0){
            return result;
        }else{
            Set<Song> r = new HashSet<Song>(songs.stream().filter(filter).collect(Collectors.toSet()));
            return r;
        }

    }
}
