package de.jjprojects.filelister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.vdheide.mp3.MP3Properties;
import de.vdheide.mp3.NoMP3FrameException;

/**
 * Recursive file listing under a specified directory.
 *  
 * @author Joerg Juenger, JJ-Projects
 * @author based on code from Alex Wang
 */
public final class FileListWorker {

   public static String getDateTime4SQL(Date date) {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return dateFormat.format(date);
   }

   /**
    * Recursively walk a directory tree and return a List of all
    * Files found; the List is sorted using File.compareTo().
    *
    * @param aStartingDir is a valid directory, which can be read.
    */
   static public List<File> getFileListing(File aStartingDir) throws FileNotFoundException {
      validateDirectory(aStartingDir);
      List<File> result = getFileListingNoSort(aStartingDir);
      Collections.sort(result);
      return result;
   }

   // PRIVATE //
   static private List<File> getFileListingNoSort( File aStartingDir ) throws FileNotFoundException {
      List<File> result = null;

      try {
         FileWriter outFile;
         outFile = new FileWriter(aStartingDir.getAbsolutePath() + "/inhalt.txt");
         PrintWriter out = new PrintWriter(outFile);
         out.println("Datum der Liste:  " + FileListWorker.getDateTime4SQL(new Date()));
         // out.println("Basis: " + aStartingDir.getName());


         result = new ArrayList<File>();
         File[] filesAndDirs = aStartingDir.listFiles();
         List<File> filesDirs = Arrays.asList(filesAndDirs);
         for(File file : filesDirs) {
            result.add(file); //always add, even if directory
            if ( ! file.isFile() ) {
               //must be a directory
               //recursive call!
               List<File> deeperList = getFileListingNoSort(file);
               result.addAll(deeperList);
               out.println("Ordner: " + file.getAbsolutePath());
            } else if (0 != file.getName().compareToIgnoreCase("inhalt.txt")) {
               out.println("Datei: " + file.getName());           

               try {
                  MP3Properties mp3Props = new MP3Properties (file);
                  int frameRate = mp3Props.getSamplerate();
                  // dateiname kurz, bitrate, spieldauer, variable ?
                  out.println("       Typ  : MP" + mp3Props.getLayer());
                  out.println("       Rate : " + frameRate + " Hz");
                  out.println("       Dauer: " + mp3Props.getLength() + " Sekunden");

               } catch (FileNotFoundException fe){
                  fe.printStackTrace();
               } catch (NoMP3FrameException me){
                  out.println("       Typ  : kein MP3");
               } catch (ArithmeticException ae) {
                  out.println("       Typ  : kein MP3");
               } catch (IOException ei){
                  ei.printStackTrace();
               } catch (Throwable th){
                  th.printStackTrace();
               } finally {
                  out.println("       Size : " + file.length() + " Bytes");
                  out.println("       Date : " + FileListWorker.getDateTime4SQL(new Date (file.lastModified())));
               }
            }
         }
         out.close();

      } catch (IOException e1) {
         e1.printStackTrace();
      }
      return result;
   }


   /**
    * Directory is valid if it exists, does not represent a file, and can be read.
    */
   static private void validateDirectory ( File aDirectory ) throws FileNotFoundException {
      if (aDirectory == null) {
         throw new IllegalArgumentException("Directory should not be null.");
      }
      if (!aDirectory.exists()) {
         throw new FileNotFoundException("Directory does not exist: " + aDirectory);
      }
      if (!aDirectory.isDirectory()) {
         throw new IllegalArgumentException("Is not a directory: " + aDirectory);
      }
      if (!aDirectory.canRead()) {
         throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
      }
   }

} 



