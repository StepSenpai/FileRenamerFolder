import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;

public class Ventana extends JFrame {
    private JPanel panelMain;
    private JLabel labelPath;
    private JTextField textFieldPath;
    private JTextField textFieldNumber;
    private JLabel labelNumber;
    private JButton buttonOK;
    private JLabel labelError;
    private JTextArea textAreaConsole;

    public Ventana() {
        setContentPane(panelMain);
        setTitle("File Renamer");
        setBounds(750, 300, 600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        textFieldPath.setText(System.getProperty("user.home") + "/Desktop/FotoNumer");

        //LISTENERS
        textFieldNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textFieldNumber.getText().isEmpty())
                    textFieldNumber.setText("0");
                if (Double.parseDouble(textFieldNumber.getText()) > 99999999) {
                    e.consume();
                    textFieldNumber.setText(String.valueOf(99999999));
                }
                if (!String.valueOf(e.getKeyChar()).matches("\\d"))
                    e.consume();
            }
        });

        buttonOK.addActionListener(e -> {
            File directory = new File(textFieldPath.getText());
            if (!directory.exists() || !directory.isDirectory()) {
                labelError.setText("The specified directory does not exist.");
            } else {
                labelError.setText("Directory found! Renaming...");

                int number = Integer.parseInt(textFieldNumber.getText());
                File[] files = directory.listFiles();
                if (files != null) {
                    // Sort the files by creation date
                    Arrays.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File file1, File file2) {
                            return Long.compare(getCreationTime(file1), getCreationTime(file2));
                        }
                    });

                    for (File file : files) {
                        if (file.isFile()) {
                            String newFileName = number++ + getFileExtension(file.getName());
                            File newFile = new File(directory, newFileName);
                            if (file.renameTo(newFile)) {
                                textAreaConsole.append("\nRenamed: " + file.getName() + " to " + newFileName);
                            } else {
                                textAreaConsole.append("\nFailed to rename: " + file.getName());
                            }
                        }
                    }
                    textAreaConsole.append("\nDONE!");
                }
            }
        });

    }

    // Helper method to get the creation time of a file
    private long getCreationTime(File file) {
        Path path = file.toPath();
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            return attributes.creationTime().toMillis();
        } catch (IOException e) {
            // Handle the exception as needed
            return 0L; // Default value
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
}



