import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CustomerScreen extends JFrame {
    public double balanceAmount = 0.0;
    private boolean paymentSuccessful = false;
    public CustomerScreen(){
        setTitle("Drinks Selection");
        
        String[] captions = {
            "captions[0]",
            "captions[1]",
            "captions[2]",
            "captions[3]",
            "captions[4]",
            "captions[5]",
            "captions[6]",
            "captions[7]",
            "captions[8]",
            "captions[9]",
            "captions[10]",
            "captions[11]"
        };
        
        String[] imagePaths = {
            "imagePaths[0]",
            "imagePaths[1]",
            "imagePaths[2]",
            "imagePaths[3]",
            "imagePaths[4]",
            "imagePaths[0]",
            "imagePaths[5]",
            "imagePaths[6]",
            "imagePaths[7]",
            "imagePaths[8]",
            "imagePaths[9]",
            "imagePaths[10]",
            "imagePaths[11]"
        };
       
        //define images panel
        JPanel imagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //define image grid
        int gridx = 0;
        int gridy = 0;
        
        //define variables
        ArrayList<Drink> selectedDrinks = new ArrayList<>();
        JLabel textLabel = new JLabel("DRINKS SELECTED");
        JLabel selectedDrinkLabel = new JLabel("");
        JLabel textLabelFinal = new JLabel("");
        JLabel balanceLabel = new JLabel("Balance: RM" + String.format("%.2f", balanceAmount));
        JButton topUpButton = new JButton("Top Up");
        JButton payButton = new JButton("Pay");
        JButton receiptButton = new JButton("Receipt");
        JButton resetButton = new JButton("Reset");
        JButton exitButton = new JButton("Exit");
        
        //import drinks image
        ImageIcon[] imageIcons = new ImageIcon[12];
        
        //put images into 3x4 grid layout
        try (BufferedReader reader = new BufferedReader(new FileReader("drinks_details.txt"))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < 12) {
                System.out.println("Line read from file: " + line);
                String[] current = line.split(",");
                captions[i] = current[0].trim();
                imagePaths[i] = current[1].trim();

                //import image as icon and make them as button
                imageIcons[i] = new ImageIcon(imagePaths[i]);
                Image scaledImage = imageIcons[i].getImage().getScaledInstance(200, 230, Image.SCALE_SMOOTH);
                imageIcons[i] = new ImageIcon(scaledImage);
                JButton imageButton = new JButton(imageIcons[i]);

                //import image caption as label
                JLabel captionLabel = new JLabel(captions[i]);
                captionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                captionLabel.setFont(captionLabel.getFont().deriveFont(Font.BOLD, 16));

                //add image button into panel
                gbc.gridx = gridx;
                gbc.gridy = gridy;
                gbc.insets = new Insets(20, 20, 5, 20); //Add padding between images
                imagePanel.add(imageButton, gbc);

                //add image caption below images in panel
                gbc.gridy = gridy + 1; // Position caption below the image
                gbc.insets = new Insets(5, 20, 50, 20); // Add padding between caption and next image
                imagePanel.add(captionLabel, gbc);

                //add action into each image button
                final String selectedDrink = captions[i];
                imageButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // use methods to set actions
                        if (selectedDrinksContains(selectedDrinks, selectedDrink)) {
                            removeSelectedDrink(selectedDrinks, selectedDrink);
                            JButton clickedButton = (JButton) e.getSource();
                            clickedButton.setBackground(null); // Reset background color
                        } else {
                            int quantity = showQuantitySelectionDialog();
                            if (quantity > 0) {
                                addSelectedDrink(selectedDrinks, selectedDrink, quantity);
                                JButton clickedButton = (JButton) e.getSource();
                                clickedButton.setBackground(Color.GRAY);
                            }
                        }

                        // Update the selectedDrinkLabel with the selected drinks
                        StringBuilder drinkTextBuilder = new StringBuilder();
                        for (Drink drink : selectedDrinks) {
                            drinkTextBuilder.append(drink.getName()).append(" (x").append(drink.getQuantity()).append(")<br><br>");
                        }
                        selectedDrinkLabel.setText("<html>" + drinkTextBuilder.toString() + "</html>");
                        double totalAmount = calculateTotalAmount(selectedDrinks);
                        textLabelFinal.setText("TOTAL AMOUNT: RM" + String.format("%.2f", totalAmount));
                    }
                });
                i++;
                gridx++;
                if (gridx >= 4) {
                    gridx = 0;
                    gridy += 2;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Failed to read file");
        }
        
        // Add action listener to the "Top Up" button
        topUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter top-up amount:");
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        balanceAmount += amount;
                        balanceLabel.setText("Balance: RM" + String.format("%.2f", balanceAmount));
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a valid positive amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid amouunt.");
                }
            }
        });
        
        
        // Add action listener to the "Pay" button
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedDrinks.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No drinks selected! Please select at least one drink.");
                } else {
                    double totalAmount = calculateTotalAmount(selectedDrinks);
                    if (totalAmount <= balanceAmount) {
                        balanceAmount -= totalAmount;
                        balanceLabel.setText("Balance: RM" + String.format("%.2f", balanceAmount));
                        JOptionPane.showMessageDialog(null, "Payment successful!");
                        paymentSuccessful = true;

                        // Write selected drinks and total amount to a text file
                        try {
                            FileWriter writer = new FileWriter("transactions.txt", true);
                            writer.write("=============ORDERS=============\n");
                            for (Drink drink : selectedDrinks) {
                                writer.write(drink.getName() + " (x" + drink.getQuantity() + ")\n");
                            }
                            writer.write("Total Amount: RM" + String.format("%.2f", totalAmount) + "\n");
                            writer.write("==================================\n\n");
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient balance. Please top up!");
                    }
                }
            }
        });
        
        
        // Add action listener to the "Receipt" button
        receiptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paymentSuccessful) {
                    JOptionPane.showMessageDialog(null, "Please complete the payment before generating a receipt.");
                } else if (selectedDrinks.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No drinks selected! Please select at least one drink.");
                } else {
                    StringBuilder receiptBuilder = new StringBuilder();
                    receiptBuilder.append("===========RECEIPT===========\n\n");

                    for (Drink drink : selectedDrinks) {
                        receiptBuilder.append(drink.getName()).append(" (x").append(drink.getQuantity()).append(")\n");
                    }

                    double totalAmount = calculateTotalAmount(selectedDrinks);
                    receiptBuilder.append("\nTotal Amount: RM").append(String.format("%.2f", totalAmount)).append("\n");
                    receiptBuilder.append("-------------------------------------------------\n");

                    String receiptText = receiptBuilder.toString();

                    JOptionPane.showMessageDialog(null, receiptText, "Receipt", JOptionPane.INFORMATION_MESSAGE);

                    // Write the receipt to a text file
                    try {
                        FileWriter writer = new FileWriter("receipt.txt", true);
                        writer.write(receiptText);
                        writer.write("\n\n\n");
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Add action listener to the "reset" button
        resetButton.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               // Reset the selected drinks and update the labels
               selectedDrinks.clear();
               selectedDrinkLabel.setText("");
               textLabelFinal.setText("");
               
               Component[] components = imagePanel.getComponents();
               for (Component component : components) {
                    if (component instanceof JButton) {
                        JButton imageButton = (JButton) component;
                        imageButton.setBackground(null);
                    }
                }
            }       
        });
        
        //Add action listener to the "exit" button
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                new VendingMachine();
            }
        });
        
       
        //customize the labels and put them into text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, 40));
        textLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 280));

        selectedDrinkLabel.setFont(selectedDrinkLabel.getFont().deriveFont(Font.BOLD, 24));
        selectedDrinkLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 280));

        textLabelFinal.setFont(textLabelFinal.getFont().deriveFont(Font.BOLD, 24));
        textLabelFinal.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 280));
          
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 25));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 280));
        
        // Customize the buttons
        topUpButton.setFont(topUpButton.getFont().deriveFont(Font.BOLD, 25));
        topUpButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        topUpButton.setForeground(Color.BLACK);
        topUpButton.setBackground(Color.WHITE);
        
        payButton.setFont(payButton.getFont().deriveFont(Font.BOLD, 25));
        payButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        payButton.setForeground(Color.BLACK);
        payButton.setBackground(Color.WHITE);
        
        
        receiptButton.setFont(receiptButton.getFont().deriveFont(Font.BOLD, 25));
        receiptButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        receiptButton.setForeground(Color.BLACK);
        receiptButton.setBackground(Color.WHITE);
        
        resetButton.setFont(resetButton.getFont().deriveFont(Font.BOLD, 25));
        resetButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        resetButton.setForeground(Color.BLACK);
        resetButton.setBackground(Color.WHITE);
        
        exitButton.setFont(payButton.getFont().deriveFont(Font.BOLD, 25));
        exitButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.WHITE);

        //add buttons and labels into panel
        textPanel.add(textLabel);
        textPanel.add(selectedDrinkLabel);
        textPanel.add(textLabelFinal);
        textPanel.add(balanceLabel);
        textPanel.add(topUpButton);
        textPanel.add(Box.createVerticalStrut(30));
        textPanel.add(payButton);
        textPanel.add(Box.createVerticalStrut(30));
        textPanel.add(receiptButton);
        textPanel.add(Box.createVerticalStrut(30));
        textPanel.add(resetButton);
        textPanel.add(Box.createVerticalStrut(30));
        textPanel.add(exitButton);

        //define frame
        JFrame frame = new JFrame("Customer Screen");
        frame.getContentPane().setLayout(new BorderLayout());
        
        Color lightPeachColor = new Color(255, 218, 185);
        textPanel.setBackground(lightPeachColor);
        imagePanel.setBackground(lightPeachColor);
        
        // Combine textPanel and imagePanel into a single panel
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.add(imagePanel, BorderLayout.WEST);
        combinedPanel.add(textPanel, BorderLayout.EAST);
        combinedPanel.setBackground(lightPeachColor);

        // Create a JScrollPane containing the combinedPanel
        JScrollPane scrollPane = new JScrollPane(combinedPanel);
        scrollPane.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        scrollPane.setViewportBorder(null);

        // Add the scrollPane to the JFrame
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    

    //to check whether the drink exists inside the array list
    private boolean selectedDrinksContains(ArrayList<Drink> selectedDrinks, String drinkName) {
        for (Drink drink : selectedDrinks) {
            if (drink.getName().equals(drinkName)) {
                return true;
            }
        }
        return false;
    }

    //add drinks based on quantity
    private void addSelectedDrink(ArrayList<Drink> selectedDrinks, String drinkName, int quantity) {
        for (Drink drink : selectedDrinks) {
            if (drink.getName().equals(drinkName)) {
                drink.setQuantity(quantity);
            }
        }
        selectedDrinks.add(new Drink(drinkName, quantity));
    }

    //remove selected drinks from the array list
    private void removeSelectedDrink(ArrayList<Drink> selectedDrinks, String drinkName) {
        for (Drink drink : selectedDrinks) {
            if (drink.getName().equals(drinkName)) {
                selectedDrinks.remove(drink);
            }
        }
    }

    //calculate total amount based on drinks selected
    private double calculateTotalAmount(ArrayList<Drink> selectedDrinks) {
        double totalAmount = 0.0;
        for (Drink drink : selectedDrinks) {
            String[] parts = drink.getName().split("RM");
            if (parts.length > 1) {
                try {
                    double price = Double.parseDouble(parts[1].trim());
                    totalAmount += price * drink.getQuantity();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalAmount;
    }

    //generate a quantity selection dialog
    private int showQuantitySelectionDialog() {
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        JComponent editor = spinner.getEditor();
        Dimension editorSize = editor.getPreferredSize();
        editorSize = new Dimension(150, 70);
        editor.setPreferredSize(editorSize);

        int result = JOptionPane.showOptionDialog(null, spinner, "Select Quantity", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (result == JOptionPane.OK_OPTION) {
            return (int) spinner.getValue();
        }

        return 0;
    }

    private class Drink {
        private String name;
        private int quantity;

        public Drink(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static void main(String[] args){
        new CustomerScreen();
    }
}
