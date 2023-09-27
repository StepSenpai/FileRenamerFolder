import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

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
        textFieldPath.setText("C:\\Users\\Sigrid\\Desktop\\FotoNumer");

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
                if (files != null)
                    for (File file : files)
                        if (file.isFile()) {
                            String newFileName = number++ + getFileExtension(file.getName());
                            File newFile = new File(directory, newFileName);
                            if (file.renameTo(newFile))
                                textAreaConsole.append("\nRenamed: " + file.getName() + " to " + newFileName);
                            else
                                textAreaConsole.append("\nFailed to rename: " + file.getName());
                        }
                textAreaConsole.append("\nDONE!");
            }
        });

    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
}
