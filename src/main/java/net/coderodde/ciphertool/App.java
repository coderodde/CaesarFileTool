package net.coderodde.ciphertool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.coderodde.encryption.CipherTools;
import net.coderodde.file.FileTools;

/**
 * This class implements the command line app for encrypting and decrypting 
 * files with a key.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 29, 2016) 
 */
public class App {
    
    private static final String HELP_MESSAGE = 
     "Usage: java -jar File.jar [-e | -d <key> FILE1 [FILE2 [...]]]\n" +
     "Where       -e   encrypt the files.\n" +
     "            -d   decrypt the files.\n" + 
     "         <key>   the key in decimal; " + 
                      "use prefix \"0x\" for hexadecimal.\n" +
     "If you omit all arguments a GUI is started instead.";
    
    private enum Mode {
        ENCRYPTING,
        DECRYPTING,
    }
    
    private boolean graphicalInterfaceRequested;
    private boolean printHelpMessage;
    private Mode mode;
    private int key;
    private String[] args;
    
    public void processArguments(String[] args) {
        this.args = args.clone();
        
        if (args.length == 0) {
            graphicalInterfaceRequested = true;
            return;
        }
        
        if (args.length < 3) {
            printHelpMessage = true;
            return;
        }
        
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
        
        if (args[1].length() >= 2 && 
           (args[1].startsWith("0x") || args[1].startsWith("0X"))) {
            String keyString = args[1].substring(2).trim().toLowerCase();
            
            try {
                key = Integer.parseInt(keyString, 16);
            } catch (NumberFormatException ex) {
                printHelpMessage = true;
                return;
            }
        } else {
            try {
                key = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                printHelpMessage = true;
                return;
            }
        }
    }
    
    public boolean helpMessageRequested() {
        return printHelpMessage;
    }
    
    public void exec() {
        if (graphicalInterfaceRequested) {
            execAsGUI();
        } else {
            execAsCommandLine();
        }
    }
    
    private void execAsGUI() {
        System.out.println("GUI");
    }
    
    private void execAsCommandLine() {
        List<File> fileList = getFileList(args);
        
        switch (mode) {
            case ENCRYPTING:
                encryptAll(fileList, key);
                break;
                
            case DECRYPTING:
                decryptAll(fileList, key);
                break;
        }
        
    }
    
    private void encryptAll(List<File> fileList, int key) {
        fileList.stream().forEach((File file) -> {
            try {
                byte[] data = FileTools.readFile(file);
                byte[] encryptedData = CipherTools.encrypt(data, key);
                FileTools.writeFile(file, encryptedData);
            } catch (IOException ex) {
                System.err.println("ERROR: " + ex.getMessage());
            }
        });
    }
    
    private void decryptAll(List<File> fileList, int key) {
        fileList.stream().forEach((File file) -> {
            try {
                byte[] data = FileTools.readFile(file);
                byte[] decryptedData = CipherTools.decrypt(data, key);
                FileTools.writeFile(file, decryptedData);
            } catch (IOException ex) {
                System.err.println("ERROR: " + ex.getMessage());
            }
        });
    }
    
    private List<File> getFileList(String[] args) {
        // We subtract 2 in order to omit the fist two arguments in 'args' that
        // are the switch and the key.
        List<File> fileList = new ArrayList<>(args.length - 2);
        
        for (int i = 2; i < args.length; ++i) {
            fileList.add(new File(args[i]));
        }
        
        return fileList;
    }
    
    private String getHelpMessage() {
        return HELP_MESSAGE;
    }
    
    public static void main(String[] args) throws IOException {
        try {
            App app = new App();
            app.processArguments(args);
            
            if (app.helpMessageRequested()) {
                System.out.println(app.getHelpMessage());
                System.exit(0);
            }
            
            app.exec();
            
        } catch (RuntimeException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }
}
