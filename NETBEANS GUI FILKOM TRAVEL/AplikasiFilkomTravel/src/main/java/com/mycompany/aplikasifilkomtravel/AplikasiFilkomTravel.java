package com.mycompany.aplikasifilkomtravel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class AplikasiFilkomTravel {

    private static MediaPlayer mediaPlayer;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        // Initialize JavaFX
        JFXPanel fxPanel = new JFXPanel(); // Ensure JavaFX is initialized

        //============= START BAGIAN CONTAINER==========
        // Membuat objek container dari JFrame
        JFrame form = new JFrame("APLIKASI FILKOM TRAVEL");

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        form.setSize(screenWidth, screenHeight);

        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Biar bisa exit (fungsinya)
        form.setLocationRelativeTo(null); // Biar di tengah
        form.setLayout(new BorderLayout()); // Use BorderLayout for main frame

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        form.add(mainPanel, BorderLayout.CENTER);
        form.setVisible(true); // Nampilin form
        //============= END BAGIAN CONTAINER==========

        //============= BAGIAN COMPONENT==========

        // Create Home Panel
        JPanel homePanel = createHomePanel(screenWidth, screenHeight, fxPanel);
        mainPanel.add(homePanel, "Home");

        // Create Contact Us Panel
        JPanel contactUsPanel = createContactUsPanel(screenWidth, screenHeight);
        mainPanel.add(contactUsPanel, "ContactUs");

        // Show Home Panel initially
        cardLayout.show(mainPanel, "Home");
    }

    private static JPanel createHomePanel(int screenWidth, int screenHeight, JFXPanel fxPanel) {
        JPanel homePanel = new JPanel(null);

        // Bagian atas panel untuk judul
        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(null);
        panelAtas.setBackground(Color.BLACK);
        panelAtas.setBounds(0, 0, screenWidth, 100); // Increased height to accommodate buttons

        JLabel labelJudul = new JLabel("FILKOMTRAVEL"); // Membuat objek komponen
        labelJudul.setFont(new Font("Montserrat", Font.BOLD, 48)); // Mengatur font dan ukuran font
        labelJudul.setForeground(Color.WHITE);

        // Calculate the position for the title label
        int labelWidth = labelJudul.getPreferredSize().width;
        int labelHeight = labelJudul.getPreferredSize().height;
        int x = 20; // Set the title label near the left edge
        int y = (100 - labelHeight) / 2;

        labelJudul.setBounds(x, y, labelWidth, labelHeight); // Mengatur posisi komponen
        panelAtas.add(labelJudul); // Masukkan komponen ke panel

        // Navigation buttons
        String[] buttonLabels = {"About Us", "Booking", "Home", "Contact Us", "Join Us"};
        int buttonWidth = 100;
        int buttonHeight = 30;
        int spacing = 10;
        int totalWidth = (buttonWidth + spacing) * buttonLabels.length - spacing;
        int startX = screenWidth - totalWidth - 30; // Start from the right edge
        int buttonY = (100 - buttonHeight) / 2;

        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            button.setFont(new Font("Montserrat", Font.BOLD, 12));
            button.setBackground(Color.WHITE); // Set button background color to yellow
            int buttonX = startX + (buttonWidth + spacing) * java.util.Arrays.asList(buttonLabels).indexOf(buttonLabel);
            button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            panelAtas.add(button);

            button.addActionListener((ActionEvent e) -> {
                System.out.println(button.getText() + " button clicked");
                if (button.getText().equals("Contact Us")) {
                    cardLayout.show(mainPanel, "ContactUs");
                } else if (button.getText().equals("Home")) {
                    cardLayout.show(mainPanel, "Home");
                }
                // Add more navigation logic here
            });
        }

        homePanel.add(panelAtas); // Masukkan panel ke form

        // Panel untuk video di tengah
        JPanel panelVideo = new JPanel(new BorderLayout());
        panelVideo.setBounds(0, 100, screenWidth, screenHeight - 330); // Adjust height to accommodate top and bottom panels
        panelVideo.add(fxPanel, BorderLayout.CENTER);
        homePanel.add(panelVideo);

        // Inisialisasi JavaFX video player
        Platform.runLater(() -> {
            try {
                String videoPath = "file:///C:/Users/HP%20PAVILION%20X360/Documents/VIDEO%20GUI%20.mp4"; // Proper URI format
                Media media = new Media(videoPath);
                mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);

                // Ensure the video is HD
                mediaView.setFitWidth(1280);  // Set width to 1280 for 720p HD video
                mediaView.setFitHeight(720);  // Set height to 720 for 720p HD video

                Scene scene = new Scene(new javafx.scene.Group(mediaView));
                fxPanel.setScene(scene);

                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Play the video in a loop
                mediaPlayer.setAutoPlay(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Panel baru di bawah video untuk menampilkan logo
        JPanel panelBaru = new JPanel();
        panelBaru.setLayout(new GridBagLayout()); // Use GridBagLayout for precise positioning
        panelBaru.setBackground(Color.BLACK);
        panelBaru.setBounds(0, screenHeight - 250, screenWidth, 200); // Adjust bounds to place it below the video and above the bottom panel

        // Label for "100+ Vehicle manufacturers"
        JLabel titleLabel = new JLabel("100+  V e h i c l e  M a n u f a c t u r e r s ");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Set constraints for the title label
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns (number of logos)
        gbcTitle.insets = new Insets(2, 0, 3, 0); // Top padding
        gbcTitle.anchor = GridBagConstraints.CENTER;
        panelBaru.add(titleLabel, gbcTitle);

        // Add logo labels
        String[] logoPaths = {
            "C:\\Users\\HP PAVILION X360\\Downloads\\Vector.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\Vector (1).png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\cbi_bmw.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\cbi_honda.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\arcticons_daihatsu.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\simple-icons_ford.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\simple-icons_suzuki.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\simple-icons_toyota.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\icon-park-outline_renault.png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\Vector (2).png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\Vector (3).png",
            "C:\\Users\\HP PAVILION X360\\Downloads\\Vector (4).png",
            // Add more paths as needed
        };

        int logoSize = 60; // Set desired logo size to 60
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 30, 5); // Set padding between logos
        gbc.gridy = 1; // Start at row 1, below the title label

        for (String logoPath : logoPaths) {
            JLabel logoLabel = new JLabel();
            ImageIcon icon = new ImageIcon(logoPath);

            // Resize the image to the desired size
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
            icon = new ImageIcon(resizedImg);

            logoLabel.setIcon(icon); // Set resized icon to the label

            panelBaru.add(logoLabel, gbc);
        }

        homePanel.add(panelBaru); // Masukkan panel ke form

        return homePanel;
    }
    //PAGE FOR CONTACT US
    private static JPanel createContactUsPanel(int screenWidth, int screenHeight) {
        JPanel contactUsPanel = new JPanel(null);
        contactUsPanel.setBackground(Color.BLACK);

        // Header panel for title and navigation
        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(null);
        panelAtas.setBackground(Color.BLACK);
        panelAtas.setBounds(0, 0, screenWidth, 100);

        JLabel labelJudul = new JLabel("FILKOMTRAVEL");
        labelJudul.setFont(new Font("Montserrat", Font.BOLD, 48));
        labelJudul.setForeground(Color.WHITE);

        int labelWidth = labelJudul.getPreferredSize().width;
        int labelHeight = labelJudul.getPreferredSize().height;
        int x = 20; // Set the title label near the left edge
        int y = (100 - labelHeight) / 2;

        labelJudul.setBounds(x, y, labelWidth, labelHeight);
        panelAtas.add(labelJudul);

        // Navigation buttons
        String[] buttonLabels = {"About Us", "Booking", "Home", "Contact Us", "Join Us"};
        int buttonWidth = 100;
        int buttonHeight = 30;
        int spacing = 10;
        int totalWidth = (buttonWidth + spacing) * buttonLabels.length - spacing;
        int startX = screenWidth - totalWidth - 30; // Start from the right edge
        int buttonY = (100 - buttonHeight) / 2;

        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            button.setFont(new Font("Montserrat", Font.BOLD, 12));
            button.setBackground(Color.WHITE); // Set button background color to yellow
            int buttonX = startX + (buttonWidth + spacing) * java.util.Arrays.asList(buttonLabels).indexOf(buttonLabel);
            button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            panelAtas.add(button);

            button.addActionListener((ActionEvent e) -> {
                System.out.println(button.getText() + " button clicked");
                if (button.getText().equals("Contact Us")) {
                    cardLayout.show(mainPanel, "ContactUs");
                } else if (button.getText().equals("Home")) {
                    cardLayout.show(mainPanel, "Home");
                }
                // Add more navigation logic here
            });
        }
        contactUsPanel.add(panelAtas);
        // Main content panel
        JLabel nameLabel = new JLabel("I'm Syifani Adillah Salsabila", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Montserrat", Font.BOLD, 48));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds((screenWidth - 800) / 2, 200, 800, 60);
        contactUsPanel.add(nameLabel);

        JLabel infoLabel = new JLabel("235150207111052 | syfaads16@student.ub.ac.id", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Montserrat", Font.PLAIN, 24));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBounds((screenWidth - 800) / 2, 270, 800, 30);
        contactUsPanel.add(infoLabel);

            ImageIcon originalIcon = new ImageIcon("C://Users//HP PAVILION X360//Downloads//Party Inaugurasi.png");
            Image originalImage = originalIcon.getImage();
            int newWidth = 225; // Set the desired width
            int newHeight = 400; // Set the desired height
            
            // Using Lanczos interpolation for better quality
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(resizedImage, 0, 0, null);
            g2d.dispose();
            
            ImageIcon resizedIcon = new ImageIcon(bufferedImage);

            JLabel profileLabel = new JLabel(resizedIcon);
            profileLabel.setBounds((screenWidth - newWidth) / 2, (screenHeight - newHeight) / 2, newWidth, newHeight); // Center the image
            contactUsPanel.add(profileLabel);
        // Footer panel for social media icons
        JPanel panelFooter = new JPanel();
        panelFooter.setLayout(new GridLayout(1, 4, 20, 0));
        panelFooter.setBackground(Color.BLACK);
        panelFooter.setBounds((screenWidth - 500) / 2, screenHeight - 150, 500, 50);

        String[] socialMediaIcons = {
            "path/to/dribbble/icon.png",
            "path/to/linkedin/icon.png",
            "path/to/medium/icon.png",
            "path/to/behance/icon.png"
        };

        for (String iconPath : socialMediaIcons) {
            JLabel iconLabel = new JLabel(new ImageIcon(iconPath));
            panelFooter.add(iconLabel);
        }

        contactUsPanel.add(panelFooter);

        return contactUsPanel;
    }
}
