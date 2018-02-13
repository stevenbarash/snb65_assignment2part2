import java.awt.Color;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * GUI class
 */
public class Spotify_GUI {

    private JFrame frame;
    private JTextField txtSearch;
    private JRadioButton radShowAlbums;
    private JRadioButton radShowArtists;
    private JRadioButton radShowSongs;
    private JScrollPane scrollPane;
    private JTable tblData;
    private DefaultTableModel musicData;

    /**
     * Launch the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Spotify_GUI window = new Spotify_GUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Spotify_GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Spotify spotify = new Spotify();
        frame = new JFrame("Spotify");
        frame.setBounds(100, 100, 1000, 600);
        frame.getContentPane().setLayout(null);
        Color spotifyGrey = new Color(33,33,33);
        frame.getContentPane().setBackground(spotifyGrey);

        JLabel lblViewSelector = new JLabel("Select View");
        lblViewSelector.setForeground(Color.white);
        lblViewSelector.setBounds(20, 30, 99, 16);
        frame.getContentPane().add(lblViewSelector);

        radShowAlbums = new JRadioButton("Albums");
        radShowAlbums.setForeground(Color.white);
        radShowAlbums.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (radShowAlbums.isSelected()) {
                    radShowArtists.setSelected(false);
                    radShowSongs.setSelected(false);
                    musicData = spotify.searchAlbums("");
                    tblData.setModel(musicData);
                }
            }
        });
        radShowAlbums.setBounds(40, 60, 150, 25);
        radShowAlbums.setSelected(true);
        frame.getContentPane().add(radShowAlbums);


        radShowArtists = new JRadioButton("Artists");
        radShowArtists.setBounds(40, 85, 150, 25);
        radShowArtists.setForeground(Color.white);
        frame.getContentPane().add(radShowArtists);
        radShowArtists.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (radShowArtists.isSelected()) {
                    radShowAlbums.setSelected(false);
                    radShowSongs.setSelected(false);
                    musicData = spotify.searchArtists("");
                    tblData.setModel(musicData);
                }
            }
        });

        radShowSongs = new JRadioButton("Songs");
        radShowSongs.setForeground(Color.white);
        radShowSongs.setBounds(40, 110, 150, 25);
        frame.getContentPane().add(radShowSongs);
        radShowSongs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (radShowSongs.isSelected()) {
                    radShowAlbums.setSelected(false);
                    radShowArtists.setSelected(false);
                    musicData = spotify.searchSongs("");
                    tblData.setModel(musicData);
                }
            }
        });


        JLabel lblSearch = new JLabel("Search");
        lblSearch.setForeground(Color.white);
        lblSearch.setBounds(20, 290, 100, 20);
        frame.getContentPane().add(lblSearch);

        frame.getContentPane().add(lblViewSelector);
        txtSearch = new JTextField();
        txtSearch.setBounds(20, 315, 200, 30);
        frame.getContentPane().add(txtSearch);
        txtSearch.setColumns(10);

        musicData = getSongData("");
        tblData = new JTable(musicData);
        //tblData.setBounds(299, 45, 600, 400);
        tblData.setFillsViewportHeight(true);
        tblData.setShowGrid(false);
        tblData.setShowHorizontalLines(true);
        tblData.setBackground(spotifyGrey);
        tblData.setForeground(Color.white);
        tblData.setGridColor(Color.white);

        scrollPane = new JScrollPane(tblData);
        scrollPane.setBounds(350, 20, 635, 540);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); //removes border to make it more visually appealing
//        scrollPane.setViewportView(tblData);
        frame.getContentPane().add(scrollPane);
        //frame.getContentPane().add(tblData);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() { //listens to see if other radio buttons are clicked, and unclicks the other ones if neccesaㄹ티
            public void actionPerformed(ActionEvent e) {
                if (radShowSongs.isSelected()) {
                    musicData = spotify.searchSongs(txtSearch.getText());
//                    musicData = getSongData(txtSearch.getText());
                    tblData.setModel(musicData);
                }
                else if (radShowAlbums.isSelected()){
                    musicData = spotify.searchAlbums(txtSearch.getText());
                    tblData.setModel(musicData);
                }
                else if (radShowArtists.isSelected()){
                    musicData = spotify.searchArtists(txtSearch.getText());
                    tblData.setModel(musicData);
                }

            }
        });

        btnSearch.setBounds(103, 357, 117, 29);

        frame.getContentPane().add(btnSearch);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Query the Spotify class for data
     *
     */


    private DefaultTableModel getSongData(String searchTerm) {
        String sql = "SELECT song_id, title, length, release_date, record_date FROM song ";
        if (!searchTerm.equals("")) {
            sql += " WHERE title LIKE '%" + searchTerm + "%';";
        }

        try {
            DbUtilities db = new DbUtilities();
            String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
            return db.getDataTable(sql, columnNames);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Unable to connect to database");
            ErrorLogger.log(e.getMessage());
        }catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to database");
            ErrorLogger.log(e.getMessage());

        }
        return null;
    }

}
