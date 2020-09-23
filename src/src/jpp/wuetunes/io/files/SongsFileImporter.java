package jpp.wuetunes.io.files;

import jpp.wuetunes.io.files.id3.ID3Tag;
import jpp.wuetunes.io.files.id3.ID3TagReader;
import jpp.wuetunes.io.files.id3.ID3TagReaderException;
import jpp.wuetunes.io.files.id3.ID3TagToMetadataConverter;
import jpp.wuetunes.model.Song;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.GenreManager;
import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.util.Validate;
import jpp.wuetunes.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SongsFileImporter {
    private ID3TagToMetadataConverter converter;
    private ArrayList<Genre> genres;

    public SongsFileImporter(Collection<Genre> genres) {
        Validate.requireNonNull(genres);
        this.genres = new ArrayList<Genre>(genres);
    }

    public SongsFileImportResult importSongsFromFolder(Path folderpath) throws IOException {

        Validate.requireNonNull(folderpath);
        List<Path> path = new ArrayList<>();
        ID3TagToMetadataConverter converter = new ID3TagToMetadataConverter(genres);
        try {
            path = Files.walk(folderpath)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }catch (IOException e){
            throw e;
        }
        Set<SongFileImportFailure> fail = new LinkedHashSet<>();
        Set<Song> songs = new LinkedHashSet<>();
        for(Path p :path){
            if(p.getFileName().toString().endsWith(".mp3")){
                try {
                    ID3Tag tag = ID3TagReader.read(p);
                    //        ID3TagToMetadataConverter converter = new ID3TagToMetadataConverter(genres);
                    Metadata m = converter.convert(tag);
                    Song song = new Song(p,m);
                    // System.out.println("Song: "+song.toString());
                    songs.add(song);

                } catch (ID3TagReaderException e) {
                    SongFileImportFailure failure = new SongFileImportFailure(p,e.getMessage());
                    fail.add(failure);
                }
            //}
            }
        }
        SongsFileImportResult songsFileImportResult = new SongsFileImportResult(songs,fail);
        return songsFileImportResult;
    }
}