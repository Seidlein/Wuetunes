package jpp.wuetunes.io.database;

import jpp.wuetunes.model.Song;
import jpp.wuetunes.model.metadata.Genre;
import jpp.wuetunes.model.metadata.Metadata;
import jpp.wuetunes.model.metadata.MetadataPicture;
import jpp.wuetunes.util.Validate;

import javax.xml.crypto.Data;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DatabaseConnection implements java.lang.AutoCloseable {
    private Path path;
    private String url;
    private boolean open = false;
    private Connection connection;

    public DatabaseConnection(Path path) {
        Validate.requireFileExists(path);
        this.path = path;
        this.url = getURL();
    }

    public DatabaseConnection() {
        this.path = Paths.get("src/main/resources/database/MediaLibrary.db");
        this.url = getURL();
    }

    public String getURL() {
        String result = "jdbc:sqlite:" + path.toString();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseConnection that = (DatabaseConnection) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public String toString() {
        String result = "DatabaseConnection to " + getURL();
        return result;

    }

    public void open() {
        open = true;
        try {
            try {
                if (connection == null || connection.isClosed()) {
                    this.connection = DriverManager.getConnection(getURL());
                } else {
                    throw new IllegalStateException();
                }
            } catch (SQLException e) {
                throw new DatabaseOpenException(e.getMessage(), e.getCause());
            }
        } catch (NullPointerException e) {
        }
    }

    public void close() {
        open = false;
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {

        }
    }

    public boolean isOpen() {

        return open;
    }

    public void writeSong(Song song) {
        Validate.requireNonNull(song); //

        if (!open) {
            throw new IllegalStateException();
        }
        String duplicate = "SELECT * FROM  songs ";
        String sql = null;
        String gen = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(duplicate);

            while (resultSet.next()) {
                if (resultSet.getString("filepath").equals(song.getFilePath().toString())) {
                    sql = "UPDATE songs SET filepath = ?, songtitle = ? ,  artist = ? , genreId = ? , albumTitle = ? , rating = ? , playCounter = ? , date = ? , trackNumber = ? , copyrightInfo = ? , webpage = ? , pictureMime = ? , pictureDesc = ? , picture = ? WHERE filepath ='"+song.getFilePath()+"'";
                    gen = "UPDATE genres SET id = ? , genreName = ?";
                }

            }
            if (sql == null) {
                sql = "INSERT INTO songs(filepath,songtitle,artist,genreId,albumTitle,rating,playCounter,date,trackNumber,copyrightInfo,webpage,pictureMime,pictureDesc,picture) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            //ASd
            stmt.close();
            gen = "INSERT INTO genres(id,genreName) VALUES(?,?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            PreparedStatement g = connection.prepareStatement(gen);
            pstmt.setString(1, song.getFilePath().toString());
            //System.out.println(sql);
            if (song.getMetadata().getSongTitle().isPresent()) {
                pstmt.setString(2, song.getMetadata().getSongTitle().get());
            } else {
                pstmt.setString(2, null);
            }
            if (song.getMetadata().getArtist().isPresent()) {
                pstmt.setString(3, song.getMetadata().getArtist().get());
            } else {
                pstmt.setString(3, null);
            }
            if (song.getMetadata().getGenre().isPresent()) {
                //System.out.println(song.getMetadata().getGenre().get().getId());
                pstmt.setInt(4, song.getMetadata().getGenre().get().getId());
                Statement st = connection.createStatement();
                ResultSet set = st.executeQuery("SELECT * FROM genres");
                boolean enthalten = false;
                while(set.next()){
                    if(set.getInt("id")==song.getMetadata().getGenre().get().getId()){
                        enthalten = true;
                    }
                }
                st.close();
                if(!enthalten){
                    g.setInt(1,song.getMetadata().getGenre().get().getId());
                    g.setString(2,song.getMetadata().getGenre().get().getName());
                    //System.out.println(gen);
                    g.executeUpdate(); }
                g.close();
            } else {
                pstmt.setString(4, null);

            }
            if (song.getMetadata().getAlbumTitle().isPresent()) {
                pstmt.setString(5, song.getMetadata().getAlbumTitle().get());
            } else {
                pstmt.setString(5, null);
            }
            if (song.getMetadata().getRating().isPresent()) {
                pstmt.setInt(6, song.getMetadata().getRating().get());
            } else {
                pstmt.setString(6, null);
            }
            if (song.getMetadata().getPlayCounter().isPresent()) {
                pstmt.setInt(7, song.getMetadata().getPlayCounter().get());
            } else {
                pstmt.setString(7, null);
            }
            if (song.getMetadata().getDate().isPresent()) {
                pstmt.setString(8, song.getMetadata().getDate().get().toString());
            } else {
                pstmt.setString(8, null);
            }
            //trackNumber = ? , copyrightInfo = ? , webpage = ? , pictureMime = ? , pictureDesc = ? , pictu
            if (song.getMetadata().getTrackNumber().isPresent()) {
                pstmt.setInt(9, song.getMetadata().getTrackNumber().get());
            } else {
                pstmt.setString(9, null);
            }
            if (song.getMetadata().getCopyrightInformation().isPresent()) {
                pstmt.setString(10, song.getMetadata().getCopyrightInformation().get().toString());
            } else {
                pstmt.setString(10, null);
            }
            if (song.getMetadata().getPublisherWebpage().isPresent()) {
                pstmt.setString(11, song.getMetadata().getPublisherWebpage().get().toString());
            } else {
                pstmt.setString(11, null);
            }
            if (song.getMetadata().getPicture().isPresent()) {
                pstmt.setString(12, song.getMetadata().getPicture().get().getMimeType());
            } else {
                pstmt.setString(12, null);
            }
            if (song.getMetadata().getPicture().isPresent()) {
                pstmt.setString(13, song.getMetadata().getPicture().get().getDescription());
            } else {
                pstmt.setString(13, null);
            }
            if (song.getMetadata().getPicture().isPresent()) {
                pstmt.setBytes(14, song.getMetadata().getPicture().get().getData());
            } else {
                pstmt.setBytes(14, null);
            }
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new DatabaseWriteException(e.getMessage(), e.getCause());
        }

    }

    public void writeSongs(Set<Song> songs) {
        Validate.requireNonNull(songs);
        if (!open) {
            throw new IllegalStateException();
        }
        for (Song s : songs) {
            writeSong(s);
        }

    }

    public void delete() {
        if (!open) {
            throw new IllegalStateException();
        }
        try {
            Statement stat = connection.createStatement();
            stat.execute("DELETE FROM songs;");
            stat.execute("DELETE FROM genres;");
        } catch (SQLException e) {
            throw new DatabaseWriteException(e.getMessage(), e.getCause());
        }
    }

    public List<Genre> loadGenres() {
        if (!open) {
            throw new IllegalStateException();
        }
        List<Genre> list = new ArrayList<>();
        try {
            Statement stat = connection.createStatement();
            ResultSet resultSet = stat.executeQuery("SELECT * FROM genres order by id");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String genreName = resultSet.getString("genreName");
                Genre g = new Genre(id, genreName);
                list.add(g);
            }
        } catch (SQLException e) {
            throw new DatabaseReadException(e.getMessage(), e.getCause());
        }
        return list;
    }

    public Set<Song> loadSongs() {
        if (!open) {
            throw new IllegalStateException();
        }
        Set<Song> songs = new LinkedHashSet<>();
        try {
            Statement stat = connection.createStatement();
            ResultSet resultSet = stat.executeQuery("SELECT * FROM songs");
            while (resultSet.next()) {
                //System.out.println(resultSet.getString("songtitle"));
                Metadata m = new Metadata();
                Path path = Paths.get(resultSet.getString("filepath"));
                if (resultSet.getString("songtitle") != null) {
                    m.setSongTitle(resultSet.getString("songtitle"));
                }
                if (resultSet.getString("artist") != null) {
                    m.setArtist(resultSet.getString("artist"));
                }
                if (resultSet.getString("genreId") != null) { // GENRE
                    List<Genre> listGenre = new ArrayList<>(this.loadGenres());
                    int id = resultSet.getInt("genreid");
                    boolean error = true;
                    for (Genre g : listGenre) {
                        if (g.getId() == id) {
                            m.setGenre(g);
                            error = false;
                        }
                    }
                    if (error) {
                        throw new InconsistentDatabaseException("genreId nicht in genres enthalten");
                    }
                }
                if (resultSet.getString("albumTitle") != null && !resultSet.getString("albumTitle").equals("null")) {
                    m.setAlbumTitle(resultSet.getString("albumTitle"));
                }
                if (resultSet.getString("rating") != null) {
                    m.setRating(resultSet.getInt("rating"));
                }
                if (resultSet.getString("playCounter") != null && !resultSet.getString("playCounter").equals("null")) {
                    m.setPlayCounter(resultSet.getInt("playCounter"));
                }
                if (resultSet.getString("date") != null && !resultSet.getString("date").equals("null")) {    // DATE SONDERFALL FORMATIERUNG
                    String d = resultSet.getString("date");
                    if (d.length() == 4) {
                        Year y = Year.of(Integer.parseInt(d));
                        m.setDate(y);
                    }
                    if (d.length() == 7) {
                        YearMonth y = YearMonth.of(Integer.parseInt(d.substring(0, 4)), Integer.parseInt(d.substring(5, 7)));
                        m.setDate(y);
                    }
                    if (d.length() == 10) {
                        Integer y = Integer.parseInt(d.substring(0, 4));
                        Integer m0 = Integer.parseInt(d.substring(5, 7));
                        Integer d0 = Integer.parseInt(d.substring(8, 10));
                        LocalDate l = LocalDate.of(y, m0, d0);
                        m.setDate(l);
                    }
                    if (d.length() == 19) {
                        try {
                            Integer y = Integer.parseInt(d.substring(0, 4));
                            Integer m0 = Integer.parseInt(d.substring(5, 7));
                            Integer d0 = Integer.parseInt(d.substring(8, 10));
                            Integer h = Integer.parseInt(d.substring(11, 13));
                            Integer mm = Integer.parseInt(d.substring(14, 16));
                            Integer ss = Integer.parseInt(d.substring(17, 19));
                            LocalDateTime l = LocalDateTime.of(y, m0, d0, h, mm, ss);
                            m.setDate(l);
                        } catch (NumberFormatException e) {
                           // System.out.println("FEHLER beim auslesen des Datums aus Datenbank");
                        }
                    }
                }

                if (resultSet.getString("trackNumber") != null) {
                    m.setTrackNumber(resultSet.getInt("trackNumber"));
                }
                if (resultSet.getString("copyrightInfo") != null && !resultSet.getString("copyrightInfo").equals("null")) {
                    try {
                        URL url = new URL(resultSet.getString("copyrightInfo"));
                        m.setCopyrightInformation(url);
                    } catch (MalformedURLException e) {
                        //System.out.println("URL1 error" + resultSet.getString("copyrightInfo"));
                        throw new InconsistentDatabaseException(e.getMessage() + "copy: " + resultSet.getString("copyrightInfo"), e.getCause());
                    }
                }
                if (resultSet.getString("webpage") != null && !resultSet.getString("webpage").equals("null")) {
                    try {
                        URL url = new URL(resultSet.getString("webpage"));
                        m.setPublisherWebpage(url);
                    } catch (MalformedURLException e) {
                        //System.out.println("URL2 error" + resultSet.getString("webpage"));
                        throw new InconsistentDatabaseException(e.getMessage() + "pub: " + resultSet.getString("webpage"), e.getCause());
                    }
                }
                if (resultSet.getString("pictureMime") != null) {
                    byte[] pictureData = resultSet.getBytes("picture");
                    MetadataPicture metadataPicture = new MetadataPicture(resultSet.getString("pictureMime"), resultSet.getString("pictureDesc"), pictureData);
                    m.setPicture(metadataPicture);
                }
                Song song = new Song(path, m);
                songs.add(song);
            }
        } catch (SQLException e) {
            //System.out.println(m.toString());
            throw new DatabaseReadException(e.getMessage(), e.getCause());

        }
        for(Song s :songs){
            // System.out.println(s.getMetadata().toString() +" "+s.getFilePath().toString());
        }
        return songs;
    }


}