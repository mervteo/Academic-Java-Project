import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.*;

public class DeleteDrinksScreen extends JFrame {
    public DeleteDrinksScreen(){
        setTitle("Add Drinks");
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Color lightPeachColor = new Color(255, 218, 185);
        panel.setBackground(lightPeachColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JLabel title = new JLabel("DELETE DRINKS");
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));
        panel.add(title, gbc);
        
        // Dropdown Box 1
        String[] drinksTypes = {
                "<html><font size='6'> Select a drink </font></html>",
                "<html><font size='6'> Mineral Water  RM1 </font></html>",
                "<html><font size='6'> Prime Energy  RM5 </font></html>",
                "<html><font size='6'> Monster Energy  RM5 </font></html>",
                "<html><font size='6'> Nescafe  RM3 </font></html>",
                "<html><font size='6'> Vitagen  RM3 </font></html>",
                "<html><font size='6'> Cola  RM2 </font></html>",
                "<html><font size='6'> 100 Plus  RM3 </font></html>",
                "<html><font size='6'> Sprite  RM2 </font></html>",
                "<html><font size='6'> Mountain Dew  RM4 </font></html>",
                "<html><font size='6'> Lipton Tea  RM3 </font></html>",
                "<html><font size='6'> Honest Tea  RM3 </font></html>",
                "<html><font size='6'> Green Tea  RM2 </font></html>"};
        
        JComboBox<String> types = new JComboBox<>(drinksTypes);
        types.setPreferredSize(new Dimension(350,70));
        gbc.gridx = 2;
        gbc.gridy = 1;
        panel.add(types, gbc);
        
        String[] drinksNames = {
            "<html><font size='6'>Select a drink image</font></html>",
            "<html><font size='6'>Mineral Water</font></html>",
            "<html><font size='6'>Prime Energy</font></html>",
            "<html><font size='6'>Monster Energy</font></html>",
            "<html><font size='6'>Nescafe</font></html>",
            "<html><font size='6'>Vitagen</font></html>",
            "<html><font size='6'>Cola</font></html>",
            "<html><font size='6'>100 Plus</font></html>",
            "<html><font size='6'>Sprite</font></html>",
            "<html><font size='6'>Mountain Dew</font></html>",
            "<html><font size='6'>Lipton Tea</font></html>",
            "<html><font size='6'>Honest Tea</font></html>",
            "<html><font size='6'>Green Tea</font></html>"
        };

        String[] drinksImages = {"Select a drink image",
                "C:\\Users\\mervt\\Downloads\\Mineral Water.jpg",
                "C:\\Users\\mervt\\Downloads\\Prime Energy.jpg",
                "C:\\Users\\mervt\\Downloads\\Monster Energy.jpg",
                "C:\\Users\\mervt\\Downloads\\Nescafe.jpg",
                "C:\\Users\\mervt\\Downloads\\Vitagen.jpg",
                "C:\\Users\\mervt\\Downloads\\Cola.jpg",
                "C:\\Users\\mervt\\Downloads\\100 Plus.jpg",
                "C:\\Users\\mervt\\Downloads\\Sprite.jpg",
                "C:\\Users\\mervt\\Downloads\\Mountain Dew.jpg",
                "C:\\Users\\mervt\\Downloads\\Lipton Tea.jpg",
                "C:\\Users\\mervt\\Downloads\\Honest Tea.jpg",
                "C:\\Users\\mervt\\Downloads\\Green Tea.jpg"};
        
        JComboBox<String> images = new JComboBox<>(drinksNames);
        images.setPreferredSize(new Dimension(350,70));
        gbc.gridx = 2;
        gbc.gridy = 2;
        panel.add(images, gbc);
        
        JButton deleteDrinksButton = new JButton("Delete Drinks");
        deleteDrinksButton.setForeground(Color.BLACK);
        deleteDrinksButton.setBackground(Color.WHITE);
        deleteDrinksButton.setFont(deleteDrinksButton.getFont().deriveFont(Font.BOLD, 25));
        deleteDrinksButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        deleteDrinksButton.setPreferredSize(new Dimension(270, 70));
        deleteDrinksButton.setHorizontalAlignment(SwingConstants.CENTER);
        deleteDrinksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dt = (String)types.getSelectedItem();
                String finaldt1 = dt.substring(22,dt.length()-20);
                String dn = (String)images.getSelectedItem();
                String finaldn1 = dn.substring(21,dt.length()-21);
                int selectedIndex = images.getSelectedIndex();
                String selectedImagePath = drinksImages[selectedIndex];

                if (!dt.equals("Select a drink") && !selectedImagePath.equals("Select a drink image")) {
                    if (finaldt1.equals(finaldn1)) {
                        try {
                            String filePath = "drinks_details.txt"; 
                            ArrayList<String> linesToKeep = new ArrayList<>();

                            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                                String currentLine;
                                while ((currentLine = reader.readLine()) != null) {
                                    if (!currentLine.contains(finaldt1)) {
                                        linesToKeep.add(currentLine);
                                    }
                                }
                            }

                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                                for (String line : linesToKeep) {
                                    writer.write(line);
                                    writer.newLine();
                                }
                            }

                            JOptionPane.showMessageDialog(null, "Drink deleted successfully!");
                            types.setSelectedIndex(0); // Reset the types combo box
                            images.setSelectedIndex(0); // Reset the images combo box
                        } catch (IOException ex) {
                            Logger.getLogger(DeleteDrinksScreen.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Failed to delete the drink.");
                        }
                    } else {
                        // Show a message if no drink is selected
                        JOptionPane.showMessageDialog(null, "Drink selected must match with its image.");
                        types.setSelectedIndex(0); // Reset the types combo box
                        images.setSelectedIndex(0); // Reset the images combo box
                    }
                } else {    
                    JOptionPane.showMessageDialog(null, "Please select a drink to delete.");
                }
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setForeground(Color.BLACK);
        resetButton.setBackground(Color.WHITE);
        resetButton.setFont(resetButton.getFont().deriveFont(Font.BOLD, 25));
        resetButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        resetButton.setPreferredSize(new Dimension(200, 70));
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                types.setSelectedIndex(0); // Reset the types combo box
                images.setSelectedIndex(0); // Reset the images combo box
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(Color.WHITE);
        backButton.setFont(backButton.getFont().deriveFont(Font.BOLD, 25));
        backButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        backButton.setPreferredSize(new Dimension(200, 70));
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginScreen();
            }
        });

        // Add Drinks Button
        gbc.gridx = 2;
        gbc.gridy = 3;
        panel.add(deleteDrinksButton, gbc);
        
        // Add Reset Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(resetButton, gbc);    
        
        // Add Back Button
        gbc.gridx = 3;
        gbc.gridy = 3;
        panel.add(backButton, gbc);  
        
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setLocationRelativeTo(null); 
    }
    
    public static void main(String[] args){
        new DeleteDrinksScreen();
    }
}
