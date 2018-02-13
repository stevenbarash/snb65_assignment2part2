import javax.swing.*;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Artist.
 */
public class Artist {


    private String artistID;
    private String firstName;
    private String lastName;
    private String bandName;
    private String bio;

    /**
     * Constructor for new Artist. Sets local variables as well as database details
     *
     * @param firstName Artist first name
     * @param lastName  Artist's last name
     * @param bandName  band's name (optional)
     * @param bio       Artist's bio (optional)
     * @throws SQLException
     */
    public Artist(String firstName, String lastName, String bandName, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bandName = bandName;
        this.bio = bio;
        this.artistID = UUID.randomUUID().toString();

        String sql = "INSERT INTO artist (artist_id,first_name,last_name,band_name,bio) ";
        sql += "VALUES (?, ?, ?, ?, ?);";
        System.out.println(sql);

        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.artistID);
            ps.setString(2, this.firstName);
            ps.setString(3, this.lastName);
            ps.setString(4, this.bandName);
            ps.setString(5, this.bio);
            ps.executeUpdate();
            db.closeDbConnection();
            db = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for existing Artist. Sets local variables to those of an artist in the DB
     *
     * @param artistID input for selecting which artist to query
     * @throws SQLException
     */


    public Artist(String artistID) {
        String sql = "SELECT * FROM artist WHERE artist_id = '" + artistID + "';";
        DbUtilities db = new DbUtilities();
        try {
            ResultSet rs = db.getResultSet(sql);
            while (rs.next()) {
                this.artistID = rs.getString("artist_id");
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.bandName = rs.getString("band_name");
                this.bio = rs.getString("bio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for this object's artistID
     *
     * @return String artistID
     */


    public String getArtistID() {
        return artistID;
    }

    /**
     * Deletes an artist based on inputted artistID from the Database. MUST DELETE THE OBJECT AFTER RUNNING THIS.
     *
     * @param artistID the ID of the artist you wish to delete.
     */
    public void deleteArtist(String artistID) {
        String sql = "DELETE FROM song_artist WHERE fk_artist_id = '" + artistID + "';";
        System.out.println(sql);

        DbUtilities db = new DbUtilities();
        db.executeQuery(sql);
        sql = "DELETE FROM artist WHERE artist_id = '" + artistID + "';";
        System.out.println(sql);
        db.executeQuery(sql);
        db.closeDbConnection();
        db = null;
    }
}
