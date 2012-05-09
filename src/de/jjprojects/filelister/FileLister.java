package de.jjprojects.filelister;

import java.io.*;
import java.util.List;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;

public class FileLister implements ActionListener {

   JFrame myFrame = null;
   JTextArea myPane = null;
   JMenuItem cmdOpen = null;

   public static void main(String[] a) {
      (new FileLister()).work();
   }
   private void work() {
      myFrame = new JFrame("MP3 Datei Scanner");
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myFrame.setSize(600,400);
      
      myPane = new JTextArea();
      myPane.setText(
         "Bitte waehlen Sie einen Ordner aus\n" 
         + "im Ordner Menu!\n");
      myFrame.setContentPane(myPane);

      JMenuBar myBar = new JMenuBar();
      JMenu myMenu = getFileMenu();
      myBar.add(myMenu); 
      myFrame.setJMenuBar(myBar);

      myFrame.setVisible(true);
   }
   private JMenu getFileMenu() {
      JMenu myMenu = new JMenu("Ordner");
      cmdOpen = new JMenuItem("Oeffnen");
      cmdOpen.addActionListener(this);
      myMenu.add(cmdOpen);

      return myMenu;
   }
   public void actionPerformed(ActionEvent e) {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      FileNameExtensionFilter filter = new FileNameExtensionFilter("*", "*");
      chooser.setFileFilter(filter);

      Object cmd = e.getSource();
      try {
         if (cmd == cmdOpen) {
            int code = chooser.showOpenDialog(myPane);
            if (code == JFileChooser.APPROVE_OPTION) {
               File startingDirectory = chooser.getSelectedFile();

               List<File> files = FileListWorker.getFileListing(startingDirectory);

               //print out all file names, in the the order of File.compareTo()
               myPane.setText("Bearbeitete Dateien:" + newline + newline);

              for(File file : files ){
                 myPane.append(file.getAbsolutePath() + newline);
                 System.out.println(file);
               }
            }
         }
      } catch (Exception f) {
          f.printStackTrace();
      }
   }
   private final static String newline = "\n";

}
