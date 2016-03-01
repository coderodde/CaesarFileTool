package net.coderodde.ciphertool;

import java.io.File;
import javax.swing.JFileChooser;
import net.coderodde.file.FileTools;

/**
 * This class implements the command line app for encrypting and decrypting 
 * files with a key.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 29, 2016) 
 */
public class CommandLineApp {
    
    private static final String HELP_MESSAGE = 
             "Usage: java -jar File.jar -e | -d <key> FILE1 [FILE2 [...]]\n" +
             "Where       -e   encrypt the files.\n" +
             "            -d   decrypt the files.\n" + 
             "         <key>   the key.";
    
    private enum Mode {
        ENCRYPTING,
        DECRYPTING,
    }
    
    private boolean printHelpMessage;
    
    public void processArguments(String[] args) {
        if (args.length < 3) {
            printHelpMessage = true;
            return;
        }
        
        Mode mode = null;
        
        switch (args[0]) {
            case "-e":
                mode = Mode.ENCRYPTING;
                break;
                
            case "-d":
                mode = Mode.DECRYPTING;
                break;
        }
        
        if (mode == null) {
            printHelpMessage = true;
            return;
        }
        
        int key = 0;
        
        try {
            key = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("");
        }
    }
    
    public boolean helpMessageRequested() {
        return printHelpMessage;
    }
    
    private String getHelpMessage() {
        return HELP_MESSAGE;
    }
    
    public static void main(String[] args) {
        try {
            CommandLineApp app = new CommandLineApp();
            app.processArguments(args);
            
            if (app.helpMessageRequested()) {
                System.out.println(app.getHelpMessage());
                System.exit(0);
            }
            
        } catch (RuntimeException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
        
//        JFileChooser fileChooser = new JFileChooser("fdsal");
//        int status = fileChooser.showOpenDialog(null);
//
//        if (status == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            
//            
//            byte[] data = FileTools.readFile(file);
//            
//            if (data == null) {
//                
//            }
//        }
    }
}
