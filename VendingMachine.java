import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class ScreenButton extends JButton {
    public ScreenButton(String label, ActionListener listener) {
        super(label);
        setFont(new Font("Times New Roman", Font.BOLD, 30));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(300, 100));
        addActionListener(listener);
    }
}

class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        float transparency = 0.9f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g2d.dispose();
    }
}

public class VendingMachine extends JFrame {
    public VendingMachine() {
        setTitle("Role Selection");

        ScreenButton customerButton = new ScreenButton("Customer", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerScreen();
            }
        });

        ScreenButton staffButton = new ScreenButton("Staff", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffScreen();
            }
        });

        BackgroundPanel backgroundPanel = new BackgroundPanel("pexels-photo-2338113.png");
        backgroundPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        backgroundPanel.add(customerButton, gbc);
        gbc.gridy = 1;
        backgroundPanel.add(staffButton, gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(backgroundPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new VendingMachine();
    }
}
