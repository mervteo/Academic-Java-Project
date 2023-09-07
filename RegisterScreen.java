import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class RegisterScreen extends JFrame {
    public RegisterScreen(){
        setTitle("Staff Registration Form");
      
        JLabel title = new JLabel("STAFF REGISTRATION FORM");
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));

        JLabel name = new JLabel("Username");
        name.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JTextField nameTextField = new JTextField(20);
        nameTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        Border empty = new EmptyBorder(10, 10, 10, 10);
        Border compound = new CompoundBorder(nameTextField.getBorder(), empty);
        nameTextField.setBorder(compound);
        
        JLabel password = new JLabel("Password");
        password.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JTextField passwordTextField = new JTextField(20);
        passwordTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        passwordTextField.setBorder(compound);
        
        JLabel number = new JLabel("Phone Number");
        number.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JTextField numberTextField = new JTextField(20);
        numberTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        numberTextField.setBorder(compound);
        
        JLabel email = new JLabel("Email Address");
        email.setFont(new Font("Times New Roman", Font.BOLD, 30));        
        JTextField emailTextField = new JTextField(20);
        emailTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        emailTextField.setBorder(compound);
        
        JLabel gender = new JLabel("Gender");
        gender.setFont(new Font("Times New Roman", Font.BOLD, 30));  
        JRadioButton male = new JRadioButton("Male");
        male.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        male.setSelected(false);   
        JRadioButton female = new JRadioButton("Female");
        female.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        female.setSelected(false);
        ButtonGroup genderBG = new ButtonGroup();
        genderBG.add(male);
        genderBG.add(female);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(male);
        genderPanel.add(female);
        male.setBackground(null);
        female.setBackground(null);
        genderPanel.setBackground(null);
       
        JLabel nationality = new JLabel("Nationality");
        nationality.setFont(new Font("Times New Roman", Font.BOLD, 30));
        JTextField nationalityTextField = new JTextField(20);
        nationalityTextField.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        nationalityTextField.setBorder(compound);
        
        JButton submit = new JButton("Submit");
        submit.setFont(new Font("Times New Roman", Font.BOLD, 30));
        submit.setForeground(Color.BLACK);
        submit.setBackground(Color.WHITE);
        JButton reset = new JButton("Reset");
        reset.setFont(new Font("Times New Roman", Font.BOLD, 30));
        reset.setForeground(Color.BLACK);
        reset.setBackground(Color.WHITE);
        JButton back = new JButton("Back");
        back.setFont(new Font("Times New Roman", Font.BOLD, 30));
        back.setForeground(Color.BLACK);
        back.setBackground(Color.WHITE);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submit);
        buttonPanel.add(reset);
        buttonPanel.add(back);
            
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String password = passwordTextField.getText();
                String number = numberTextField.getText();
                String email = emailTextField.getText();
                String gender = male.isSelected() ? "Male" : "Female";
                String nationality = nationalityTextField.getText();

                if (!name.matches("[a-zA-Z ]+") || name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid name");
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid password");
                    return;
                }

                if (!number.matches("[0-9]+") || number.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid phone number");
                    return;
                }

                if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address");
                    return;
                }

                if (!nationality.matches("[a-zA-Z]+") || nationality.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid nationality");
                    return;
                }

                String userDetails = String.format("%s,%s,%s,%s,%s,%s\n", name, password, number, email, gender, nationality);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_details.txt", true))) {
                    writer.write(userDetails);
                    writer.flush();
                    JOptionPane.showMessageDialog(null, "User details saved successfully!");
                    nameTextField.setText("");
                    passwordTextField.setText("");
                    numberTextField.setText("");
                    emailTextField.setText("");
                    nationalityTextField.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Failed to save user details");
                    ex.printStackTrace();
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the input fields
                nameTextField.setText("");
                passwordTextField.setText("");
                numberTextField.setText("");
                emailTextField.setText("");
                nationalityTextField.setText("");
                
                // Reset the gender selection
                male.setSelected(false);
                female.setSelected(false);
            }
        });
        
        back.addActionListener(new ActionListener() {
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
        panel.add(name, gbc);
        gbc.gridy = 2;
        panel.add(nameTextField, gbc);
        gbc.gridy = 3;
        panel.add(password, gbc);
        gbc.gridy = 4;
        panel.add(passwordTextField, gbc);
        gbc.gridy = 5;
        panel.add(number, gbc);
        gbc.gridy = 6;
        panel.add(numberTextField, gbc);
        gbc.gridy = 7;
        panel.add(email, gbc);
        gbc.gridy = 8;
        panel.add(emailTextField, gbc);
        gbc.gridy = 9;
        panel.add(gender, gbc);
        gbc.gridy = 10;
        panel.add(genderPanel, gbc);
        gbc.gridy = 11;
        panel.add(nationality, gbc);
        gbc.gridy = 12;
        panel.add(nationalityTextField, gbc);
        gbc.gridy = 13;
        panel.add(buttonPanel, gbc);
        
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setContentPane(panel);
    }
    
    public static void main(String[] args){
        new RegisterScreen();
    }
}
