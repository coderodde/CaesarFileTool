package net.coderodde.ciphertool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static net.coderodde.ciphertool.App.parseKey;

/**
 * This class implements the action listener for the GUI buttons.
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Mar 1, 2016)
 */
final class MyActionListener implements ActionListener {

    private final JFrame ownerFrame;
    private final Mode mode;

    MyActionListener(JFrame ownerFrame, Mode mode) {
        this.mode = Objects.requireNonNull(mode, "The input mode is null.");
        this.ownerFrame = ownerFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File[] files = askUserToChooseFiles(ownerFrame);
        
        if (files == null) {
            return;
        }

        Integer key = askKeyFromUser(
            ownerFrame,
            mode == Mode.ENCRYPTING ?
            "Enter the encryption key. (Use prefix \"0x\" for hexadecimal.)" :
            "Enter the decryption key: (Use prefix \"0x\" for hexadecimal.)");
        
        if (key == null) {
            return;
        }
        
        List<File> fileList = Arrays.asList(files);
        
        switch (mode) {
            case ENCRYPTING:
                App.encryptAll(fileList, key);
                break;
                
            case DECRYPTING:
                App.decryptAll(fileList, key);
                break;
                
            default:
                throw new IllegalStateException(
                        "Should not ever get here. Please, debug.");
        }
    }

    private static File[] askUserToChooseFiles(JFrame ownerFrame) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int status = chooser.showOpenDialog(ownerFrame);

        if (status == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFiles();
        }

        return null;
    }
    
    private static Integer askKeyFromUser(JFrame ownerFrame, String prompt) {
        String keyString = JOptionPane.showInputDialog(
                                       ownerFrame,
                                       prompt,
                                       "",
                                       JOptionPane.QUESTION_MESSAGE);

        if (keyString == null || keyString.isEmpty()) {
            // User cancelled the entry of the key.
            return null;
        }

        try {
            return parseKey(keyString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    ownerFrame,
                    "\"" + keyString + "\" is an invalid key.",
                    "",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
