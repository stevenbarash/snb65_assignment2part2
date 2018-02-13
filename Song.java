import javax.swing.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Song.
 */
public class Song {

    private String songID;
    private String title;
    private double length;
    private String filePath;
    private String releaseDate;
    private String recordDate;
    /**
     * The Song artists.
     */
    Map<String, Artist> songArtists = new HashMap<>();

    /**
     * Constructor for new song. Sets local variables as well as updating the database.
     *
     * @param title       song title
     * @param length      song length in minutes
     * @param releaseDate song's release date
     * @param recordDate  date of song recording
     * @throws SQLException
     */


    public Song(String title, double length, String releaseDate, String recordDate) { //for songs not yet in the database
        this.title = title;
        this.length = length;
        this.releaseDate = releaseDate;
        this.recordDate = recordDate;
        this.songID = UUID.randomUUID().toString();


        // System.out.println(this.songID);
        // String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date,fk_album_id) ";
        // sql += "VALUES ('" + this.songID + "', '" + this.title + "', " + this.length + ", '', '" + this.releaseDate + "', '" + this.recordDate + "', '" + this.albumID + "');";
        String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?);";

        System.out.println(sql);

//SQL queryEnsure trailing space at the end


        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.songID);
            ps.setString(2, this.title);
            ps.setDouble(3, this.length);
            ps.setString(4, "");
            ps.setString(5, this.releaseDate);
            ps.setString(6, this.recordDate);
            ps.executeUpdate();
            db.closeDbConnection();
            db = null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Unable to connect to database.");
            e.printStackTrace();
        }

    }

    /**
     * Constructor for existing song. Sets local variables based on database information.
     *
     * @param songId ID of the song that you wish to import
     * @throws SQLException
     */
    public Song(String songId) {  //pull song from database and populate all the properties
        String sql = "SELECT * FROM song_artist JOIN song ON song.song_id = song_artist.fk_song_id";
//        String sql = "SELECT * FROM song WHERE song_id = '" + songId + "';";
        DbUtilities db = new DbUtilities();
        try {
            ResultSet rs = db.getResultSet(sql);

            while (rs.next()) {
                this.songID = rs.getString("song_id");
                // System.out.println("Song ID from database: " + this.songID);
                this.title = rs.getString("title");
                this.releaseDate = rs.getDate("release_date").toString();
                this.recordDate = rs.getDate("record_date").toString();
                this.length = rs.getDouble("length");
                this.songArtists.put(rs.getString("fk_artist_id"), new Artist(rs.getString("fk_artist_id")));
                // System.out.println("Song title from database: " + this.title);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Deletes a song based on inputted songID from the Database. MUST DELETE THE OBJECT AFTER RUNNING THIS.
     *
     * @param songID the song id
     */
    public void deleteSong(String songID) {
        String sql = "DELETE FROM album_song WHERE fk_song_id = '" + songID + "';";
        System.out.println(sql);
        DbUtilities db = new DbUtilities();
        db.executeQuery(sql);
        sql = "DELETE FROM song_artist WHERE fk_song_id = '" + songID + "';";
        System.out.println(sql);

        db.executeQuery(sql);
        sql = "DELETE FROM song WHERE song_id = '" + songID + "';";
        System.out.println(sql);

        db.executeQuery(sql);
        db.closeDbConnection();
        db = null;
    }

    /**
     * Deletes an artist from a song based on artistID. Does not delete it from the database, just the hashmap.
     *
     * @param artistID the ID of the artist you wish to delete from a song
     */
    public void deleteArtist(String artistID) {
        songArtists.remove(artistID);
    }

    /**
     * Deletes an artist from a song based on Artist object. Does not delete it from the database, just the hashmap.
     *
     * @param artist the object of the artist you wish to delete from this song
     */
    public void deleteArtist(Artist artist) {
        songArtists.remove(artist.getArtistID());
    }

    /**
     * Adds an artist to a song based on Artist object. Updates database accordingly.
     *
     * @param artist the object of the artist you wish to add to this song
     */
    public void addArtist(Artist artist) {
        String sql = "INSERT INTO song_artist (fk_song_id, fk_artist_id)";

        sql += "VALUES (?, ?);";
        System.out.println(sql);
        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.songID);
            ps.setString(2, artist.getArtistID());
            ps.executeUpdate();
            db.closeDbConnection();
            db = null;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to database.");
            e.printStackTrace();
        }
    }

    /**
     * Getter for this object's songID
     *
     * @return String songID
     */
    public String getSongID() {
        return songID;
    }

}
