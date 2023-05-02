import org.ini4j.Wini;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.io.File;

public class Preview extends JFrame {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    Wini ini;
    Preview(String level) {
        // Init
        System.out.println("Initializing preview...");
        JFrame frame = new JFrame();
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        // Create cache directory
        String directoryPath = "HPULevelsManager_Cache";

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            boolean created = directory.mkdir();

            if (created) {
                System.out.println("Cache created successfully!");
            } else {
                System.out.println(ANSI_RED+"[ERROR] Failed to create cache!"+ANSI_RESET);
                JOptionPane.showMessageDialog(null, "ERROR: There was problem creating cache folder for the preview!\nLevel preview requires it work properly!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Cache already exists!");
        }

        // Replace all : in ini to fix reading
        String inputFilePath = "Created Levels/"+level+".ini";
        try {
            String inputString = Files.readString(Paths.get(inputFilePath)); // Load file into string
            PrintWriter out = new PrintWriter("HPULevelsManager_Cache/preview_data-"+level+".ini");
            out.println(inputString.replace(':', ' '));
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Load ini
        try {
            ini = new Wini(new File("HPULevelsManager_Cache/preview_data-"+level+".ini"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Level Info
        String infoDisplay = "Level Name: "+ini.get("Level Info", "Name");
        JLabel levelInfo = new JLabel(infoDisplay);
        levelInfo.setBounds(0, 0, infoDisplay.length()*7, 16);
        levelInfo.setForeground(new Color(255, 255, 255));
        levelInfo.setOpaque(true);
        levelInfo.setBackground(new Color(0, 0, 0));
        frame.add(levelInfo);

        String infoDisplayC = "Creator: "+ini.get("Level Info", "Creator");
        JLabel levelInfoC = new JLabel(infoDisplayC);
        levelInfoC.setBounds(0, 16, infoDisplayC.length()*7, 16);
        levelInfoC.setForeground(new Color(255, 255, 255));
        levelInfoC.setOpaque(true);
        levelInfoC.setBackground(new Color(0, 0, 0));
        frame.add(levelInfoC);

        //String ocDisplay = "[LEGACY VARIABLE] Object Count: "+ini.get("Level Info", "Object Count");
        //JLabel oCount = new JLabel(ocDisplay);
        //oCount.setBounds(0, 32, ocDisplay.length()*7, 16);
        //oCount.setForeground(new Color(255, 255, 255));
        //oCount.setOpaque(true);
        //oCount.setBackground(new Color(0, 0, 0));
        //frame.add(oCount);

        // Resources
        ImageIcon block = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/block.png");
            block = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            block = new ImageIcon("Assets/block.png");
        }
        ImageIcon downbarrier = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/downbarrier.png");
            downbarrier = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            downbarrier = new ImageIcon("Assets/downbarrier.png");
        }
        ImageIcon upbarrier = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/upbarrier.png");
            upbarrier = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            upbarrier = new ImageIcon("Assets/upbarrier.png");
        }
        ImageIcon hlaser = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/hlaser.png");
            hlaser = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            hlaser = new ImageIcon("Assets/hlaser.png");
        }
        ImageIcon slime = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/slime.png");
            slime = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            slime = new ImageIcon("Assets/slime.png");
        }
        ImageIcon spikey = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/spikey.png");
            spikey = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            spikey = new ImageIcon("Assets/spikey.png");
        }
        ImageIcon vlaser = null;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Assets/vlaser.png");
            vlaser = new ImageIcon(Objects.requireNonNull(url));
        } catch (Exception ex) {
            System.out.println("Resource failed to load from class loader! Resource will be loaded from files...");
            vlaser = new ImageIcon("Assets/vlaser.png");
        }

        // Generate Level
        int yRead = 0;
        int xRead = 0;
        while (yRead < 100) {
            xRead = 0;
            while (xRead < 150) { // All items
                if (Objects.equals(ini.get("Tiles", "X  " + xRead + " Y  " + yRead), "Block")) {
                    System.out.println("[PREVIEW] block at X" + xRead + " Y" + yRead);
                    JLabel blockTile = new JLabel();
                    blockTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    blockTile.setIcon(block);
                    frame.add(blockTile);
                }
                if (Objects.equals(ini.get("Tiles", "X  " + xRead + " Y  " + yRead), "Laser")) {
                    System.out.println("[PREVIEW] vlaser at X" + xRead + " Y" + yRead);
                    JLabel vlaserTile = new JLabel();
                    vlaserTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    vlaserTile.setIcon(vlaser);
                    frame.add(vlaserTile);
                }
                if (Objects.equals(ini.get("Tiles", "X  " + xRead + " Y  " + yRead), "Rotated Laser")) {
                    System.out.println("[PREVIEW] hlaser at X" + xRead + " Y" + yRead);
                    JLabel hlaserTile = new JLabel();
                    hlaserTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    hlaserTile.setIcon(hlaser);
                    frame.add(hlaserTile);
                }
                if (Objects.equals(ini.get("Tiles", "X  " + xRead + " Y  " + yRead), "Slime Block")) {
                    System.out.println("[PREVIEW] slime at X" + xRead + " Y" + yRead);
                    JLabel slimeTile = new JLabel();
                    slimeTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    slimeTile.setIcon(slime);
                    frame.add(slimeTile);
                }
                if (Objects.equals(ini.get("Tiles", "X  " + xRead + " Y  " + yRead), "Spikey")) {
                    System.out.println("[PREVIEW] spikey at X" + xRead + " Y" + yRead);
                    JLabel spikeyTile = new JLabel();
                    spikeyTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    spikeyTile.setIcon(spikey);
                    frame.add(spikeyTile);
                }
                if (Objects.equals(ini.get("Barriers", "X  " + xRead + " Y  " + yRead), "Up Barrier")) {
                    System.out.println("[PREVIEW] upbarrier at X" + xRead + " Y" + yRead);
                    JLabel upbarrierTile = new JLabel();
                    upbarrierTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    upbarrierTile.setIcon(upbarrier);
                    frame.add(upbarrierTile);
                }
                if (Objects.equals(ini.get("Barriers", "X  " + xRead + " Y  " + yRead), "Down Barrier")) {
                    System.out.println("[PREVIEW] downbarrier at X" + xRead + " Y" + yRead);
                    JLabel downbarrierTile = new JLabel();
                    downbarrierTile.setBounds(xRead*5-20, yRead*5-20, 45, 45);
                    downbarrierTile.setIcon(downbarrier);
                    frame.add(downbarrierTile);
                }
                xRead++;
            }
            yRead++;
        }

        // Creating blocks that should exist in editor but aren't saved to the level ini
        int i = 0; while (i < 17) { // Celling
            JLabel blockTile = new JLabel();
            blockTile.setBounds(i*45, 0, 45, 45);
            blockTile.setIcon(block);
            frame.add(blockTile);
            i++;
        }
        i = 0; while (i < 17) { // Floor
            JLabel blockTile = new JLabel();
            blockTile.setBounds(i*45, 450, 45, 45);
            blockTile.setIcon(block);
            frame.add(blockTile);
            i++;
        }
        i = 0; while (i < 8) { // Left Wall
            JLabel blockTile = new JLabel();
            blockTile.setBounds(0, i*45, 45, 45);
            blockTile.setIcon(block);
            frame.add(blockTile);
            i++;
        }
        i = 0; while (i < 8) { // Right Wall
            JLabel blockTile = new JLabel();
            blockTile.setBounds(45*16, i*45, 45, 45);
            blockTile.setIcon(block);
            frame.add(blockTile);
            i++;
        }

        // Finalize
        System.out.println("Finishing...");
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("icon.png");
            ImageIcon appIcon = new ImageIcon(Objects.requireNonNull(url));
            frame.setIconImage(appIcon.getImage());
        } catch (Exception ex) {
            System.out.println(ANSI_RED + "ERROR: Could not load app icon!" + ANSI_RESET);
            ImageIcon appIcon = new ImageIcon("icon.png");
            frame.setIconImage(appIcon.getImage());
        }
        frame.setTitle("HPU Levels Manager");
        frame.setSize(780, 530);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.getContentPane().setBackground(new Color(0, 0, 0));
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("Finished creating Menu window!");
    }
}
