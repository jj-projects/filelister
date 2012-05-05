package de.jjprojects.filelister;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Recursive file listing under a specified directory.
 *  
 * @author javapractices.com
 * @author Alex Wong
 * @author anonymous user
 */
public final class FileLister {

   /**
    * Demonstrate use.
    * 
    * @param aArgs - <tt>aArgs[0]</tt> is the full name of an existing 
    * directory that can be read.
    */
   public static void main(String... aArgs) throws FileNotFoundException {
      File startingDirectory= new File("./dist");
      List<File> files = FileLister.getFileListing(startingDirectory);

      //print out all file names, in the the order of File.compareTo()
      for(File file : files ){
         System.out.println(file);
      }
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
         FileNameMap fileNameMap = URLConnection.getFileNameMap();

         FileWriter outFile;
         outFile = new FileWriter(aStartingDir.getAbsolutePath() + "/inhalt.txt");
         PrintWriter out = new PrintWriter(outFile);
         out.println("Datum der Liste:  " + FileLister.getDateTime4SQL(new Date()));
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
               out.println("       Typ  : " + fileNameMap.getContentTypeFor(file.getName()));

               try {
                  AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
                  AudioFormat format = aff.getFormat();

                  long audioFileLength = file.length();
                  int frameSize = aff.getFrameLength();
                  float frameRate = format.getFrameRate();
                  float durationInSeconds = (audioFileLength / (frameSize * frameRate));
                  // dateiname kurz, bitrate, spieldauer, variable ?
                  out.println("       Rate : " + frameRate);
                  out.println("       Dauer: " + durationInSeconds);

               } catch (IOException ei){
               } catch (UnsupportedAudioFileException e) {
                  out.println("       Size : " + file.length() + " Byte");
                  out.println("       Date : " + FileLister.getDateTime4SQL(new Date (file.lastModified())));
               }
            }
         }
         out.close();

      } catch (IOException e1) {
         // TODO Auto-generated catch block
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

   public static String getDateTime4SQL(Date date) {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return dateFormat.format(date);
   }

} 



