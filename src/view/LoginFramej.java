package view;

import controller.LoginController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//INHERITANCE CONCEPT
public class LoginFramej extends javax.swing.JFrame {

    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnReset;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblSubtitle;

    private LoginController loginController;

    public LoginFramej() {
        loginController = new LoginController();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fresh Slice 65 - Login");
        setResizable(false);
        setSize(500, 550);
        setLocationRelativeTo(null);
        
        //POLIMORPHISM CONCEPT
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(255, 250, 240);
                Color color2 = new Color(255, 228, 196);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Logo/Icon Panel
        JLabel lblIcon = new JLabel("🍞");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcon.setBounds(200, 30, 100, 70);
        mainPanel.add(lblIcon);
        
        // Title
        lblTitle = new JLabel("Fresh Slice 65");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(139, 69, 19));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(50, 110, 400, 40);
        mainPanel.add(lblTitle);
        
        // Subtitle
        lblSubtitle = new JLabel("Bakery Management System");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblSubtitle.setForeground(new Color(101, 67, 33));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitle.setBounds(50, 155, 400, 25);
        mainPanel.add(lblSubtitle);
        
        // Login form panel with rounded border
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.setColor(new Color(222, 226, 230));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(null);
        formPanel.setBounds(75, 210, 350, 260);
        mainPanel.add(formPanel);
        
        // Username label and field
        lblUsername = new JLabel("● Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(new Color(52, 58, 64));
        lblUsername.setBounds(30, 30, 120, 25);
        formPanel.add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(30, 60, 290, 40);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtUsername);
        
        // Password label and field
        lblPassword = new JLabel("● Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(52, 58, 64));
        lblPassword.setBounds(30, 110, 120, 25);
        formPanel.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(30, 140, 290, 40);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtPassword);
        
        // Login button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBounds(30, 200, 135, 45);
        btnLogin.setBackground(new Color(40, 167, 69));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnLogin);
        
        // Reset button
        btnReset = new JButton("RESET");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnReset.setBounds(185, 200, 135, 45);
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setBorderPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnReset);
        
        // Footer text
        JLabel lblFooter = new JLabel("(c) 2024 Fresh Slice 65 - All Rights Reserved");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblFooter.setForeground(new Color(108, 117, 125));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setBounds(50, 490, 400, 20);
        mainPanel.add(lblFooter);
        
        // Add hover effects
        addButtonHoverEffect(btnLogin, new Color(40, 167, 69), new Color(34, 142, 58));
        addButtonHoverEffect(btnReset, new Color(108, 117, 125), new Color(90, 98, 104));

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtUsername.setText("");
                txtPassword.setText("");
                txtUsername.requestFocus();
            }
        });

        // Allow Enter key to trigger login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLoginActionPerformed(null);
                }
            }
        });
        
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                }
            }
        });
        
        setContentPane(mainPanel);
    }
    
    private void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = loginController.authenticate(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Welcome, " + (user.getFullname() != null ? user.getFullname() : user.getUsername()) + "!",
                "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose();
            
            // Open unified management interface for all roles
            new UnifiedManagementFrame(user);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password. Please try again.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    // Main entry point
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFramej().setVisible(true);
            }
        });
    }
}
