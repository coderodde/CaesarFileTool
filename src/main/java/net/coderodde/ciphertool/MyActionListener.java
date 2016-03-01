package net.coderodde.ciphertool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static net.coderodde.ciphertool.App.decryptAll;
import static net.coderodde.ciphertool.App.encryptAll;

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
        File[] files = askUserToChooseFiles(mode == Mode.ENCRYPTING ? 
                                            "Choose files to encrypt" :
                                            "Choose files to decrypt",
                                            ownerFrame);
        if (files == null) {
            return;
        }

        String keyString = JOptionPane.showInputDialog(
                                       ownerFrame,
                                       mode == Mode.ENCRYPTING ?
                                       "Type in the encryption key:" :
                                       "Type in the decryption key:",
                                       "",
                                       JOptionPane.QUESTION_MESSAGE);

        if (keyString.length() >= 2
                && (keyString.startsWith("0x")
                || keyString.startsWith("0X"))) {
            String keyStringPrepared = keyString.substring(2)
                    .trim()
                    .toLowerCase();

            try {
                int key = Integer.parseInt(keyStringPrepared, 16);
                
                switch (mode) {
                    case ENCRYPTING: 
                        encryptAll(Arrays.asList(files), key);
                        break;
                        
                    case DECRYPTING:
                        decryptAll(Arrays.asList(files), key);
                        break;
                        
                    default:
                        throw new IllegalStateException(
                                "Should never get here.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        ownerFrame,
                        "\"" + keyString + "\" is an invalid key.",
                        "",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                int key = Integer.parseInt(keyString);
                switch (mode) {
                    case ENCRYPTING: 
                        encryptAll(Arrays.asList(files), key);
                        break;
                        
                    case DECRYPTING:
                        decryptAll(Arrays.asList(files), key);
                        break;
                        
                    default:
                        throw new IllegalStateException(
                                "Should never get here.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        ownerFrame,
                        "\"" + keyString + "\" is an invalid key.",
                        "",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private File[] askUserToChooseFiles(String title, JFrame ownerFrame) {
        JFileChooser chooser = new JFileChooser(title);
        chooser.setMultiSelectionEnabled(true);
        int status = chooser.showOpenDialog(ownerFrame);
        
        if (status == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFiles();
        }
        
        return null;
    }
}
