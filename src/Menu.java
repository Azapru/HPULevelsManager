import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Menu extends JFrame {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    int i;
    String currentLevelSelection;
    JButton deleteButton;
    Menu() {
        File appExec = new File("HPULevelsManager.jar");
        if (!appExec.exists()) {
            JOptionPane.showMessageDialog(null, "WARNING: It appears that HPULevelsManager.jar file has been renamed to something else!\nPlease use original filename or this application might not work properly!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }

        // Init
        System.out.println("Initializing...");
        JFrame frame = new JFrame();
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        // Title text
        System.out.println("Creating titleLabel...");
        JLabel titleLabel = new JLabel("HARD PLATFORMER ULTIMATE Levels Manager");
        titleLabel.setBounds(0, 16, 575, 16);
        titleLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        frame.add(titleLabel);

        // Credits text
        System.out.println("Creating creditsLabel...");
        JLabel creditsLabel = new JLabel("v0.1-alpha (HPUv2.0.0)");
        creditsLabel.setBounds(5, 440, 575, 16);
        frame.add(creditsLabel);

        // Levels List
        System.out.println("Creating levelsListObj...");
        try {
                String[] levelsList = listLevels("Created Levels").toArray(new String[0]);
            i = 0;
            while (i < levelsList.length) {
                levelsList[i] = levelsList[i].substring(0, levelsList[i].length() - 4);
                i++;
            }
            JList<String> levelsListObj = new JList<>(levelsList);
            levelsListObj.setBounds(50, 50, 300, 365);
            levelsListObj.setBorder(border);
            levelsListObj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            levelsListObj.addListSelectionListener(e -> {
                currentLevelSelection = levelsListObj.getSelectedValue();
                System.out.println("Level selection changed: "+currentLevelSelection);
            });
            frame.add(levelsListObj);
        } catch (Exception exception) {
            System.out.println(ANSI_RED+"ERROR: There was problem creating levelsListObj"+ANSI_RESET);
            JOptionPane.showMessageDialog(null, "ERROR: There was problem creating levelsListObj!\nThis most likely means that \"Created Levels\" directory cannot be accessed, or it is missing.\n\nAre you sure you put HPULevelsManager together with HPU in the same directory?", "ERROR", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, exception.getStackTrace(), exception.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Create Preview Button
        System.out.println("Creating previewButton...");
        JButton previewButton = new JButton("Preview");
        previewButton.setBounds(400, 50, 100, 50);
        previewButton.addActionListener(e -> {
            try {
                Preview preview = new Preview(currentLevelSelection);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.add(previewButton);

        // Create "Delete Button"
        System.out.println("Creating deleteButton...");
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(400, 390, 100, 25);
        deleteButton.addActionListener(e -> {
            if (e.getSource() == deleteButton) {
                File level = new File("Created Levels/"+currentLevelSelection+".ini");
                System.out.println("Deleting \"Created Levels/"+currentLevelSelection+".ini\" ..."); // Remove level
                if (level.delete()) {
                    System.out.println("Successfully deleted the level: "+currentLevelSelection);
                    try {
                        Runtime.getRuntime().exec("java -jar HPULevelsManager.jar"); // I know I could have done it better, but I don't know how...
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                } else {
                    System.out.println(ANSI_RED+"ERROR: Failed to delete the file."+ANSI_RESET);
                }
            }
        });
        frame.add(deleteButton);

        // Create Import Level Button
        System.out.println("Creating importButton...");
        JButton importButton = new JButton("Import");
        importButton.setBounds(400, 125, 100, 50);
        importButton.addActionListener(e -> {
            Path temp = null;
            try {
                JFileChooser fChoose = new JFileChooser();
                fChoose.setFileFilter(new FileNameExtensionFilter("HPU Levels (.ini)", "ini"));
                fChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fChoose.showOpenDialog(null);
                File selectedFileImport = new File(fChoose.getSelectedFile().toURI());
                Files.copy(Paths.get(selectedFileImport.getAbsoluteFile().toURI()), Paths.get("Created Levels/"+selectedFileImport.getName()));
                JOptionPane.showMessageDialog(null, "Level imported successfully!\nThe view will be refreshed...", "", JOptionPane.INFORMATION_MESSAGE);
                try {
                    Runtime.getRuntime().exec("java -jar HPULevelsManager.jar"); // I know I could have done it better, but I don't know how...
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getStackTrace(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                System.exit(0);
            } catch (Exception ex) {
                System.out.println("A wild error has showed up! Did you cancel the level import window?");
            }
        });
        frame.add(importButton);

        // Upload Level Button
        System.out.println("Creating uploadButton...");
        JButton uploadButton = new JButton("Upload Lvl");
        uploadButton.setBounds(400, 200, 100, 50);
        uploadButton.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://itch.io/t/2540425/uploading-levels-101"));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, new RuntimeException(ex), "", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.add(uploadButton);

        // Get Levels Button
        System.out.println("Creating getLevelsButton...");
        JButton getLevelsButton = new JButton("Get Levels");
        getLevelsButton.setBounds(400, 275, 100, 50);
        getLevelsButton.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://redshadowxd.itch.io/hpultimate/community"));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, new RuntimeException(ex), "", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.add(getLevelsButton);

        // Config
        System.out.println("Finishing...");
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("icon.png");
            ImageIcon appIcon = new ImageIcon(Objects.requireNonNull(url));
            frame.setIconImage(appIcon.getImage());
        } catch (Exception ex) {
            System.out.println(ANSI_RED+"ERROR: Could not load app icon!"+ANSI_RESET);
            ImageIcon appIcon = new ImageIcon("icon.png");
            frame.setIconImage(appIcon.getImage());
        }
        frame.setTitle("HPU Levels Manager");
        frame.setSize(575, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(240, 240, 240));
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("Finished creating Menu window!");
    }

    public static Set<String> listLevels(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
