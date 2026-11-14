package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * General Dashboard Frame - fallback for other roles
 */
public class DashboardFrame extends JFrame {

    private JButton btnProducts, btnCategories, btnStock, btnUsers, btnLogout;
    private User user;

    public DashboardFrame(User user) {
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Fresh Slice 65 - Dashboard");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Header
        JLabel lblTitle = new JLabel("Fresh Slice 65", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(139, 69, 19));
        lblTitle.setBounds(175, 15, 300, 40);
        add(lblTitle);

        JLabel lblSubtitle = new JLabel("Bakery Management System", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubtitle.setBounds(175, 50, 300, 25);
        add(lblSubtitle);

        String welcomeText = "Welcome, " + (user.getFullname() != null ? user.getFullname() : user.getUsername());
        JLabel lblWelcome = new JLabel(welcomeText, SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));
        lblWelcome.setBounds(175, 80, 300, 25);
        add(lblWelcome);

        // Navigation Buttons
        btnProducts = new JButton("📦 Products");
        btnProducts.setBounds(75, 130, 200, 50);
        btnProducts.setFont(new Font("Arial", Font.BOLD, 14));
        btnProducts.setBackground(new Color(70, 130, 180));
        btnProducts.setForeground(Color.WHITE);
        add(btnProducts);

        btnCategories = new JButton("📁 Categories");
        btnCategories.setBounds(375, 130, 200, 50);
        btnCategories.setFont(new Font("Arial", Font.BOLD, 14));
        btnCategories.setBackground(new Color(60, 179, 113));
        btnCategories.setForeground(Color.WHITE);
        add(btnCategories);

        btnStock = new JButton("📊 Stock");
        btnStock.setBounds(75, 210, 200, 50);
        btnStock.setFont(new Font("Arial", Font.BOLD, 14));
        btnStock.setBackground(new Color(255, 165, 0));
        btnStock.setForeground(Color.BLACK);
        add(btnStock);

        btnUsers = new JButton("👥 Users");
        btnUsers.setBounds(375, 210, 200, 50);
        btnUsers.setFont(new Font("Arial", Font.BOLD, 14));
        btnUsers.setBackground(new Color(138, 43, 226));
        btnUsers.setForeground(Color.WHITE);
        add(btnUsers);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(250, 330, 150, 45);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnLogout);

        // Role-based access - Only managers can access user management
        if (!user.getRole().equalsIgnoreCase("Manager")) {
            btnUsers.setEnabled(false);
            btnUsers.setToolTipText("Only managers can access user management");
        }

        // Button actions
        btnProducts.addActionListener(e -> {
            dispose();
            new UnifiedManagementFrame(user);
        });
        btnCategories.addActionListener(e -> {
            dispose();
            new UnifiedManagementFrame(user);
        });
        btnStock.addActionListener(e -> {
            dispose();
            new UnifiedManagementFrame(user);
        });
        btnUsers.addActionListener(e -> {
            dispose();
            new UnifiedManagementFrame(user);
        });
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFramej().setVisible(true);
            }
        });

        setVisible(true);
    }
}

