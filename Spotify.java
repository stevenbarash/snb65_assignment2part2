import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Controls the searching of data in the database and the displaying of data in the UI
 */
public class Spotify {
    /**
     * Searches for inputted song in DB, if no term is inputted it returns all songs
     *
     * @param searchTerm input for what song to search for
     * @return the default table model
     * @throws SQLException
     */

    public DefaultTableModel searchSongs(String searchTerm) {
        String sql = "SELECT song_id, title, length, release_date, record_date FROM song ";
        if (!searchTerm.equals("")) {
            sql += " WHERE title LIKE '%" + searchTerm + "%';"; //searches for similar item through "like"
        }

        try {
            DbUtilities db = new DbUtilities();
            String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
            return db.getDataTable(sql, columnNames); //returns the datatable with search results

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to database");
            ErrorLogger.log(e.getMessage()); //logs error if found
        }
        return null;

    }

    /**
     * Searches for inputted album in DB, if no term is inputted it returns all albums
     *
     * @param searchTerm input for what album to search for
     * @return the default table model
     * @throws SQLException
     */


    public DefaultTableModel searchAlbums(String searchTerm) {
        String sql = "SELECT album_id, title, release_date, cover_image_path, recording_company_name, number_of_tracks, PMRC_rating, length FROM album ";
        if (!searchTerm.equals("")) {
            sql += " WHERE title LIKE '%" + searchTerm + "%';";
        }
        try {
            DbUtilities db = new DbUtilities();
            String[] columnNames = {"Album ID", "Title", "Release Date", "Cover Image Path", "Recording Company", "No. of Tracks", "PMRC Rating", "Length"};
            return db.getDataTable(sql, columnNames);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to database");
            ErrorLogger.log(e.getMessage());
        }
        return null;
    }

    /**
     * Searches for inputted artist in DB, if no term is inputted it returns all artists
     *
     * @param searchTerm input for what artist to search for
     * @return the default table model
     * @throws SQLException
     */
    public DefaultTableModel searchArtists(String searchTerm) {
        String sql = "SELECT artist_id, first_name, last_name, band_name, bio FROM artist ";
        if (!searchTerm.equals("")) {
            sql += " WHERE first_name LIKE '%" + searchTerm + "%' OR last_name LIKE '%" + searchTerm + "%'OR band_name LIKE '%" + searchTerm + "%';"; //uses three different parameters to find the item
        }
        try {
            DbUtilities db = new DbUtilities();
            String[] columnNames = {"Artist ID", "First Name", "Last Name", "Band Name", "Biography"};
            return db.getDataTable(sql, columnNames);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to database");
            ErrorLogger.log(e.getMessage());
        }
        return null;
    }


}
