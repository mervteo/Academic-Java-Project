import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.border.*;

public class StaffScreen extends JFrame {
    public StaffScreen(){      
        setTitle("Login and Registration Selection");
        
        final JTextField usernameTextField = new JTextField(30);
        usernameTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        Border empty = new EmptyBorder(10, 10, 10, 10);
        Border compound = new CompoundBorder(usernameTextField.getBorder(), empty);
        usernameTextField.setBorder(compound);
        final JPasswordField passwordTextField = new JPasswordField(30);
        passwordTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        passwordTextField.setBorder(compound); 
        passwordTextField.setEchoChar('*');

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JLabel registerLabel = new JLabel("<html>Don't have an account? <u>Register Now</u></html>");
        registerLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                    new RegisterScreen();
            }
        });
        
        JToggleButton showPasswordToggleButton = new JToggleButton("Show");
        showPasswordToggleButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        showPasswordToggleButton.setForeground(Color.BLACK);
        showPasswordToggleButton.setBackground(Color.WHITE);
        
        showPasswordToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton button = (JToggleButton) e.getSource();
                if (button.isSelected()) {
                    passwordTextField.setEchoChar((char) 0); // Show password
                } else {
                    passwordTextField.setEchoChar('*'); // Hide password
                }
            }
        });
        
        
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(Color.WHITE);
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader reader = new BufferedReader(new FileReader("user_details.txt"))) {
                    String line;
                    String usernameTextFieldValue = usernameTextField.getText();
                    String passwordTextFieldValue = passwordTextField.getText();
                    boolean loginSuccessful = false;

                    while ((line = reader.readLine()) != null) {
                        String[] current = line.split(",");
                        if (current.length >= 2 && current[0].equals(usernameTextFieldValue) && current[1].equals(passwordTextFieldValue)) {
                            loginSuccessful = true;
                            break;
                        }
                    }

                    if (loginSuccessful) {
                        JOptionPane.showMessageDialog(null, "Login successful");
                        new LoginScreen();
                    } else {
                        usernameTextField.setText("");
                        passwordTextField.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid username or password");
                    }
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Failed to read file");
                }
            }
        });

        
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(Color.WHITE);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VendingMachine();
            }
        });
        
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Times New Roman", Font.BOLD, 30));
        resetButton.setForeground(Color.BLACK);
        resetButton.setBackground(Color.WHITE);
        
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameTextField.setText("");
                passwordTextField.setText("");
            }
        });
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(showPasswordToggleButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);
                
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        Color lightPeachColor = new Color(255, 218, 185);
        panel.setBackground(lightPeachColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        panel.add(usernameLabel, gbc);
        gbc.gridy = 1;
        panel.add(usernameTextField, gbc);   
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridy = 3;
        panel.add(passwordTextField, gbc); 
        gbc.gridy = 4;
        panel.add(buttonPanel, gbc);
        gbc.gridy = 5;
        panel.add(registerLabel, gbc);

        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setLocationRelativeTo(null); 
    }
    
    public static void main(String[] args){
        new StaffScreen();
    }
}
