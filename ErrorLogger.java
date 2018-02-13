import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Error logger.
 */
public class ErrorLogger {

    /**
     * Log.
     *
     * @param errorMessage the error message
     */
    public static void log(String errorMessage) {
        File file = new File("errorlog.txt");
        try {
            LocalDateTime now = LocalDateTime.now(); //grabs local date/time
            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //formats it as either the date or the time
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
            BufferedWriter bf = new BufferedWriter(new FileWriter(file,true)); //append is set to true so that the file is not overwritten
            bf.write(date.format(now) + "," + time.format(now) + "," + errorMessage+"\n"); //writes the data, separated by commas
            bf.flush();
            bf.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error writing to file");
        }
    }
}