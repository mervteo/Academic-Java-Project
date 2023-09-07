import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginScreen extends JFrame{
    public LoginScreen(){
        
        class ModifyDrinksAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                new AddDrinksScreen();
            }
        }
        
        class DeleteDrinksAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                new DeleteDrinksScreen();
            }
        }
        
        class ViewDrinksAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader reader = new BufferedReader(new FileReader("drinks_details.txt"))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String drinkName = parts[0]; 
                        String drinkImageSource = parts[1];
                        
                        String price = "";
                        String[] drinkInfo = drinkName.split("\\s+");
                        if (drinkInfo.length > 1) {
                            price = drinkInfo[drinkInfo.length - 1]; // Last element is the price
                        }
                        // Extract the drink name
                        String name = drinkName.replaceFirst("\\s+" + price, "");
                        
                        sb.append("==============Drink Information==============\n");
                        sb.append("\nDrink Name: ");
                        sb.append(name);
                        sb.append("\nDrink Price: ");
                        sb.append(price);
                        sb.append("\nDrink Image Source: ");
                        sb.append(drinkImageSource);
                        sb.append("\n\n===========================================\n\n\n");
                        
                    }
                    JTextPane textPane = new JTextPane();
                    textPane.setText(sb.toString());
                    textPane.setEditable(false);
                    
                    Font font = new Font("Times New Roman", Font.BOLD , 20); // Change font name, style, and size
                    textPane.setFont(font);
                    
                    StyledDocument doc = textPane.getStyledDocument();
                    SimpleAttributeSet centerStyle = new SimpleAttributeSet();
                    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
                    doc.setParagraphAttributes(0, doc.getLength(), centerStyle, false);

                    JScrollPane scrollPane = new JScrollPane(textPane);
                    scrollPane.setPreferredSize(new Dimension(500, 500));

                    JOptionPane.showMessageDialog(null, scrollPane, "Drinks Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Failed to read file");
                }
            }
        }
        
        class ViewOrdersAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }

                    JTextPane textPane = new JTextPane();
                    textPane.setText(sb.toString());
                    textPane.setEditable(false);
                    
                    Font font = new Font("Times New Roman", Font.BOLD , 20); // Change font name, style, and size
                    textPane.setFont(font);
                    
                    StyledDocument doc = textPane.getStyledDocument();
                    SimpleAttributeSet centerStyle = new SimpleAttributeSet();
                    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
                    doc.setParagraphAttributes(0, doc.getLength(), centerStyle, false);

                    JScrollPane scrollPane = new JScrollPane(textPane);
                    scrollPane.setPreferredSize(new Dimension(400, 400));

                    JOptionPane.showMessageDialog(null, scrollPane, "Customers Orders", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Failed to read file");
                }
            }
        }
        
        class ViewReportsAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                int highestAmount = 0;
                int lowestAmount = 1;
                String line;
                StringBuilder sb = new StringBuilder();
                
                double amount = 0.0;
                int totalDrinksSold = 0;
                double totalAmount = 0.0;
                int totalOrders = 0;

                String drinkNameFinal = null;
                int orderedAmount = 0;
                Map<String, Integer> drinkOrders = new HashMap<>();

                try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) { 
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("x")) {  
                            String quantityString = line.substring(line.indexOf("(")+2, line.indexOf(")"));
                            int quantity = Integer.parseInt(quantityString);
                            totalDrinksSold += quantity;
                        }
                        if (line.contains("Total")){
                            String quantityAmount = line.substring(line.indexOf("RM")+2, line.indexOf("."));
                            amount = Double.parseDouble(quantityAmount);
                            totalAmount += amount;
                        }
                        if (line.contains("ORDERS")){
                            totalOrders += 1;
                        }
                        if (line.contains("x")) {
                            String drinkName = line.substring(0, line.indexOf("RM")-1);
                            String quantityString = line.substring(line.indexOf("(")+2, line.indexOf(")"));
                            int quantity = Integer.parseInt(quantityString);
                            drinkOrders.put(drinkName, drinkOrders.getOrDefault(drinkName, 0) + quantity);    
                        }    
                        sb.append(line);
                        sb.append("\n");
                    }
                
                    sb.append("==================================");
                    sb.append("\nTotal number of orders: " + totalOrders);
                    sb.append("\nTotal amount of drinks sold: " + totalDrinksSold);
                    sb.append("\nTotal revenue: RM " + totalAmount);
                    sb.append("\n=================================");
                    JTextPane textPane = new JTextPane();
                    textPane.setText(sb.toString());
                    textPane.setEditable(false);
                    Font font = new Font("Times New Roman", Font.BOLD , 20); // Change font name, style, and size
                    textPane.setFont(font); 
                    StyledDocument doc = textPane.getStyledDocument();
                    SimpleAttributeSet centerStyle = new SimpleAttributeSet();
                    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
                    doc.setParagraphAttributes(0, doc.getLength(), centerStyle, false);
                    JScrollPane scrollPane = new JScrollPane(textPane);
                    scrollPane.setPreferredSize(new Dimension(400, 400));
                    JOptionPane.showMessageDialog(null, scrollPane, "Sales and Revenue Report", JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    
                    StringBuilder rb = new StringBuilder();
                    String drinkWithHighestAmount = null;
                    rb.append("=======DRINKS ORDERED=======\n");
                    for (Map.Entry<String, Integer> entry : drinkOrders.entrySet()) {
                        drinkNameFinal = entry.getKey();
                        orderedAmount = entry.getValue();
                        rb.append(drinkNameFinal).append("(x").append(orderedAmount).append(")").append("\n");
                        if (orderedAmount > highestAmount) {
                            highestAmount = orderedAmount;
                            drinkWithHighestAmount = drinkNameFinal;
                        }
                    }
                    rb.append("=================================");
                    rb.append("\nMost popular drink: " + drinkWithHighestAmount);
                    rb.append("\n==================================");
                    JTextPane textPane1 = new JTextPane();
                    textPane1.setEditable(false);
                    textPane1.setText(rb.toString());
                    textPane1.setFont(font);
                    StyledDocument doc1 = textPane1.getStyledDocument();
                    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
                    doc1.setParagraphAttributes(0, doc1.getLength(), centerStyle, false);
                    JScrollPane scrollPane1 = new JScrollPane(textPane1);
                    scrollPane1.setPreferredSize(new Dimension(400, 400));
                    JOptionPane.showMessageDialog(null, scrollPane1, "Most Popular Drink Report", JOptionPane.INFORMATION_MESSAGE);  
                    
                    
                    
                    StringBuilder rbrb = new StringBuilder();
                    ArrayList<String> drinkWithLowestAmount = new ArrayList<>();
                    rbrb.append("=======DRINKS ORDERED=======\n");
                    for (Map.Entry<String, Integer> entry : drinkOrders.entrySet()) {
                        drinkNameFinal = entry.getKey();
                        orderedAmount = entry.getValue();
                        rbrb.append(drinkNameFinal).append("(x").append(orderedAmount).append(")").append("\n");
                        if (orderedAmount == lowestAmount) {
                            lowestAmount = orderedAmount;
                            drinkWithLowestAmount.add(drinkNameFinal);
                        }
                    }
                    rbrb.append("===================================");
                    rbrb.append("\nLeast popular drink: " + String.join(", ", drinkWithLowestAmount));
                    rbrb.append("\n===================================");
                    JTextPane textPane2 = new JTextPane();
                    textPane2.setEditable(false);
                    textPane2.setText(rbrb.toString());
                    textPane2.setFont(font);
                    StyledDocument doc2 = textPane2.getStyledDocument();
                    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
                    doc2.setParagraphAttributes(0, doc1.getLength(), centerStyle, false);
                    JScrollPane scrollPane2 = new JScrollPane(textPane2);
                    scrollPane2.setPreferredSize(new Dimension(400, 400));
                    JOptionPane.showMessageDialog(null, scrollPane2, "Least Popular Drink Report", JOptionPane.INFORMATION_MESSAGE); 
                     
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        setTitle("Staff Functions");
        
        JLabel title = new JLabel("STAFF FUNCTIONS");
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));
        
        JButton modifyDrinksButton = new JButton("Add Drinks");
        modifyDrinksButton.setForeground(Color.BLACK);
        modifyDrinksButton.setBackground(Color.WHITE);
        modifyDrinksButton.setFont(modifyDrinksButton.getFont().deriveFont(Font.BOLD, 25));
        modifyDrinksButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        modifyDrinksButton.setPreferredSize(new Dimension(250, 70));
        modifyDrinksButton.setHorizontalAlignment(SwingConstants.CENTER);
        modifyDrinksButton.addActionListener(new ModifyDrinksAction());
        
        JButton deleteDrinksButton = new JButton("Modify Drinks");
        deleteDrinksButton.setForeground(Color.BLACK);
        deleteDrinksButton.setBackground(Color.WHITE);
        deleteDrinksButton.setFont(deleteDrinksButton.getFont().deriveFont(Font.BOLD, 25));
        deleteDrinksButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        deleteDrinksButton.setPreferredSize(new Dimension(250, 70));
        deleteDrinksButton.setHorizontalAlignment(SwingConstants.CENTER);
        deleteDrinksButton.addActionListener(new DeleteDrinksAction());
        
        JButton viewDrinksButton = new JButton("View Drinks");
        viewDrinksButton.setForeground(Color.BLACK);
        viewDrinksButton.setBackground(Color.WHITE);
        viewDrinksButton.setFont(viewDrinksButton.getFont().deriveFont(Font.BOLD, 25));
        viewDrinksButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        viewDrinksButton.setPreferredSize(new Dimension(250, 70));
        viewDrinksButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewDrinksButton.addActionListener(new ViewDrinksAction());
   
        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.setForeground(Color.BLACK);
        viewOrdersButton.setBackground(Color.WHITE);
        viewOrdersButton.setFont(viewOrdersButton.getFont().deriveFont(Font.BOLD, 25));
        viewOrdersButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        viewOrdersButton.setPreferredSize(new Dimension(250, 70));
        viewOrdersButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewOrdersButton.addActionListener(new ViewOrdersAction());
            
        JButton viewReportsButton = new JButton("View Reports");
        viewReportsButton.setForeground(Color.BLACK);
        viewReportsButton.setBackground(Color.WHITE);
        viewReportsButton.setFont(viewReportsButton.getFont().deriveFont(Font.BOLD, 25));
        viewReportsButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        viewReportsButton.setPreferredSize(new Dimension(250, 70));
        viewReportsButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewReportsButton.addActionListener(new ViewReportsAction());
        
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setForeground(Color.BLACK);
        logOutButton.setBackground(Color.WHITE);
        logOutButton.setFont(logOutButton.getFont().deriveFont(Font.BOLD, 25));
        logOutButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        logOutButton.setPreferredSize(new Dimension(250, 70));
        logOutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StaffScreen();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Color lightPeachColor = new Color(255, 218, 185);
        panel.setBackground(lightPeachColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(title, gbc);
        
        gbc.gridy = 1;
        panel.add(modifyDrinksButton, gbc);
        
        gbc.gridy = 2;
        panel.add(deleteDrinksButton, gbc);
        
        gbc.gridy = 3;
        panel.add(viewDrinksButton, gbc);
        
        gbc.gridy = 4;
        panel.add(viewOrdersButton, gbc);
        
        gbc.gridy = 5;
        panel.add(viewReportsButton, gbc);
        
        gbc.gridy = 6;
        panel.add(logOutButton, gbc);
        
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setLocationRelativeTo(null);
    }

    static class DrinkData {
        int totalSales;
        double totalRevenue;
    }
    
    public static void main(String[] args){
        new LoginScreen();
    }
}
