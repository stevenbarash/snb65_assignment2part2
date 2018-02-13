/**
 * The type Tester.
 */
public class Tester {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String [] args){
        //Song s = new Song();
//        Artist test = new Artist("bob","samuel",null,"This is a bio");
//
//        Artist test2 = new Artist("12cdbffd-39a5-47e7-a8f4-e0e039a6c664");

        // Album test3 = new   Album("title", "2012/02/20",null,"Record Records", 200, "explicit", 2.23);

        //Artist test2 = new Artist("1stname","lastname","nameofband","text");
        // Artist test = new Artist("afbf49d3-0a35-48c3-9a4a-d6e9464e4a8a");
//        test.deleteArtist("afbf49d3-0a35-48c3-9a4a-d6e9464e4a8a");
        //        System.out.println(test2.getFirstName());
//        System.out.println(test.getFirstName());

//DELETE DATA by marking it in a bit column called "is active" as zero
        System.out.println("Creating song");

        Song newSong = new Song("SongTitle", 2.1,"1980/01/03",null);

        System.out.println("Creating Artist");

        Artist newArtist = new Artist("First","Last",null,null);

        System.out.println("Creating Album");

        Album newAlbum = new Album("Album Title","1960/09/09",null,"Infsci Records",12,"explicit",50.30);

        System.out.println("Adding song to album");

        newAlbum.addSong(newSong);

        System.out.println("Adding artist to song");

        newSong.addArtist(newArtist);

        System.out.println("Removing artist from song");

        newSong.deleteArtist(newArtist);

        System.out.println("Removing song from album");

        newAlbum.deleteSong(newSong);

        System.out.println("Deleting song");

        newSong.deleteSong(newSong.getSongID());

        newSong = null;

        System.out.println("Deleting Album");

        newAlbum.deleteAlbum(newAlbum.getAlbumID());

        newAlbum = null;

        System.out.println("Deleting artist");


        newArtist.deleteArtist(newArtist.getArtistID());

        newArtist = null;



    }
}
