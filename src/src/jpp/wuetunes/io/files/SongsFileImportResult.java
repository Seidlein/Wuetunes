package jpp.wuetunes.io.files;

import jpp.wuetunes.model.Song;
import jpp.wuetunes.util.Validate;

import java.util.HashSet;
import java.util.Set;

public class SongsFileImportResult {
    private Set<Song>songs;
    private Set<SongFileImportFailure> failures;

    public SongsFileImportResult(Set<Song> songs, Set<SongFileImportFailure> failures){
        Validate.requireNonNull(songs);
        Validate.requireNonNull(failures);
        this.songs = new HashSet<Song>(songs);
        this.failures = new HashSet<SongFileImportFailure>(failures);
    }
    public Set<Song> getSongs(){
        return songs;
    }
    public Set<SongFileImportFailure> getFailures(){
        return failures;
    }

}
