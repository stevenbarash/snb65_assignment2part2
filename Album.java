import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Album.
 */
public class Album {

    private String albumID;
    private String title;
    private String releaseDate;
    private String coverImagePath;
    private String recordingCompanyName;
    private int numberOfTracks;
    private String pmrcRating;
    private double length;
    private Map<String, Song> albumSongs;

    /**
     * Constructor for new Album. Sets local variables as well as database details
     *
     * @param title                album title
     * @param releaseDate          date that album was released
     * @param cover_image_path     filepath to the cover image
     * @param recordingCompanyName name of recording company
     * @param numberOfTracks       number of tracks in the album
     * @param pmrcRating           parental guidance rating
     * @param length               length of album in minutes
     * @throws SQLException
     */
    public Album(String title, String releaseDate, String cover_image_path, String recordingCompanyName,
            int numberOfTracks, String pmrcRating, double length) {
        albumSongs = new HashMap<>();
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverImagePath = cover_image_path;
        this.recordingCompanyName = recordingCompanyName;
        this.numberOfTracks = numberOfTracks;
        this.pmrcRating = pmrcRating;
        this.length = length;
        this.albumID = UUID.randomUUID().toString();

        String sql = "INSERT INTO album(album_id,title,release_date,cover_image_path,recording_company_name,number_of_tracks,PMRC_rating,length)";
        sql += "VALUES(?,?,?,?,?,?,?,?);";

        System.out.println(sql);

        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.albumID);
            ps.setString(2, this.title);
            ps.setString(3, this.releaseDate);
            ps.setString(4, this.coverImagePath);
            ps.setString(5, this.recordingCompanyName);
            ps.setInt(6, this.numberOfTracks);
            ps.setString(7, this.pmrcRating);
            ps.setDouble(8, this.length);
            ps.executeUpdate();
            db.closeDbConnection();
            db = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for an existing Album. Sets local variables to those of an album in the DB
     *
     * @param albumID ID of album that you wish to query
     * @throws SQLException
     */
    public Album(String albumID) {
        albumSongs = new HashMap<>();
        String sql = "SELECT * FROM album WHERE album_id = '" + albumID + "';";
        DbUtilities db = new DbUtilities();
        try {
            ResultSet rs = db.getResultSet(sql);
            while (rs.next()) {
                this.albumID = rs.getString("album_id");
                this.title = rs.getString("title");
                this.releaseDate = rs.getString("release_date");
                this.coverImagePath = rs.getString("cover_image_path");
                this.recordingCompanyName = rs.getString("recording_company_name");
                this.numberOfTracks = rs.getInt("number_of_tracks");
                this.pmrcRating = rs.getString("PMRC_rating");
                this.length = rs.getDouble("length");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "SELECT * FROM album_song WHERE fk_album_id = '" + albumID + "';";
        try {
            ResultSet rs = db.getResultSet(sql);
            while (rs.next()) {
                albumSongs.put(rs.getString("song_id"), new Song(rs.getString("song_id")));
            }
        } catch (SQLException e) {
            ErrorLogger.log(e.getMessage());
            JOptionPane.showMessageDialog(null, "Unable to connect to database");
        } catch (NullPointerException e) {
            ErrorLogger.log(e.getMessage());
            JOptionPane.showMessageDialog(null, "Unable to connect to database");
        }
    }

    /**
     * Deletes a song from an album based on songID. Does not delete its entry from the song table. Just the junction table.
     *
     * @param songID the ID of the song you wish to delete from an album
     */
    public void deleteSong(String songID) {
        String sql = "DELETE FROM album_song WHERE fk_song_id = '" + songID + "';";
        System.out.println(sql);

        DbUtilities db = new DbUtilities();
        db.executeQuery(sql);
        db.closeDbConnection();
        db = null;

        albumSongs.remove(songID); //removes the song from hashmap

    }

    /**
     * Deletes a song from an album based on Song object. Does not delete its entry from the song table. Just the junction table.
     *
     * @param song the object of the song you wish to delete from an album
     */
    public void deleteSong(Song song) {
        String sql = "DELETE FROM album_song WHERE fk_song_id = '" + song.getSongID() + "';";
        System.out.println(sql);

        DbUtilities db = new DbUtilities();
        db.executeQuery(sql);
        db.closeDbConnection();
        db = null;

        albumSongs.remove(song.getSongID()); //removes the song from hashmap
    }

    /**
     * Deletes an album from the database. IF YOU ARE DELETING THE SAME ALBUM STORED IN THE OBJECT, DELETE THE OBJECT AS WELL.
     *
     * @param albumID the ID of the album you wish to delete from the database
     */
    public void deleteAlbum(String albumID) {
        String sql = "DELETE FROM album_song WHERE fk_album_id = '" + albumID + "';";
        System.out.println(sql);

        DbUtilities db = new DbUtilities();
        db.executeQuery(sql);
        sql = "DELETE FROM album WHERE album_id = '" + albumID + "';";
        System.out.println(sql);
        db.executeQuery(sql);
        db.closeDbConnection();
        db = null;

    }

    /**
     * Adds a song to this album from a Song object. Updates the database accordingly.
     *
     * @param song the object of the song you wish to add to this album.
     */
    public void addSong(Song song) {
        String sql = "INSERT INTO album_song (fk_album_id, fk_song_id) ";

        sql += "VALUES (?, ?);";
        System.out.println(sql);
        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.albumID);
            ps.setString(2, song.getSongID());
            ps.executeUpdate();
            db.closeDbConnection();
            db = null;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to database.");
            e.printStackTrace();
        }
    }

    /**
     * Getter for albumID.
     *
     * @return String albumID
     */
    public String getAlbumID() {
        return this.albumID;
    }

}
