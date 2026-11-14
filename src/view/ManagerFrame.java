package view;

import controller.StockController;
import controller.ProductController;
import model.User;
import model.Stock;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Production Manager Frame
 * Features: Create user accounts, Monitor stock, Generate restock alerts
 */
public class ManagerFrame extends JFrame {

    private JButton btnProducts, btnCategories, btnStock, btnUsers, btnRestockAlerts, btnLogout;
    private JTable tblLowStock;
    private DefaultTableModel lowStockModel;
    private JLabel lblAlertCount;
    private User manager;

    private StockController stockController;
    private ProductController productController;

    public ManagerFrame(User manager) {
        this.manager = manager;
        this.stockController = new StockController();
        this.productController = new ProductController();
        initComponents();
        loadLowStockAlerts();
    }

    private void initComponents() {
        setTitle("Fresh Slice 65 - Production Manager Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Fresh Slice 65 - Manager Dashboard");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        String welcomeText = "Welcome, " + (manager.getFullname() != null ? manager.getFullname() : manager.getUsername());
        JLabel lblWelcome = new JLabel(welcomeText);
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));
        lblWelcome.setForeground(Color.WHITE);

        btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(lblWelcome);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel - Navigation Buttons
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createTitledBorder("Management"));
        navPanel.setPreferredSize(new Dimension(200, 400));

        btnProducts = createNavButton("Products", new Color(70, 130, 180));
        btnCategories = createNavButton("Categories", new Color(60, 179, 113));
        btnStock = createNavButton("Stock", new Color(255, 165, 0));
        btnUsers = createNavButton("Users", new Color(138, 43, 226));
        btnRestockAlerts = createNavButton("Restock Alerts", new Color(220, 20, 60));

        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(btnProducts);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(btnCategories);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(btnStock);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(btnUsers);
        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(new JSeparator());
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(btnRestockAlerts);
        navPanel.add(Box.createVerticalGlue());

        mainPanel.add(navPanel, BorderLayout.WEST);

        // Right Panel - Low Stock Alerts Dashboard
        JPanel alertPanel = new JPanel(new BorderLayout(10, 10));
        alertPanel.setBorder(BorderFactory.createTitledBorder("Low Stock Alerts"));

        lblAlertCount = new JLabel("Loading...");
        lblAlertCount.setFont(new Font("Arial", Font.BOLD, 16));
        lblAlertCount.setForeground(Color.RED);
        JPanel alertHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alertHeaderPanel.add(new JLabel("Items needing restock: "));
        alertHeaderPanel.add(lblAlertCount);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadLowStockAlerts());
        alertHeaderPanel.add(btnRefresh);
        
        alertPanel.add(alertHeaderPanel, BorderLayout.NORTH);

        lowStockModel = new DefaultTableModel(new String[]{"Product", "Current Qty", "Restock Level", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLowStock = new JTable(lowStockModel);
        tblLowStock.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblLowStock);
        alertPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(alertPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        JLabel lblFooter = new JLabel("Fresh Slice 65 Bakery Management System © 2024 - Manager Portal");
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        btnProducts.addActionListener(e -> openProductFrame());
        btnCategories.addActionListener(e -> openCategoryFrame());
        btnStock.addActionListener(e -> openStockFrame());
        btnUsers.addActionListener(e -> openUserManagementFrame());
        btnRestockAlerts.addActionListener(e -> showRestockAlertsDialog());

        setVisible(true);
        
        // Show alert on startup if there are low stock items
        checkAndShowAlerts();
    }

    private JButton createNavButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void loadLowStockAlerts() {
        lowStockModel.setRowCount(0);
        List<Stock> lowStockItems = stockController.getLowStock();
        
        for (Stock stock : lowStockItems) {
            Product product = productController.getProductById(stock.getProductId());
            String productName = product != null ? product.getName() : "Unknown Product";
            String status = stock.getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK";
            
            lowStockModel.addRow(new Object[]{
                productName,
                stock.getQuantity(),
                stock.getRestockLevel(),
                status
            });
        }
        
        lblAlertCount.setText(String.valueOf(lowStockItems.size()));
        if (lowStockItems.isEmpty()) {
            lblAlertCount.setForeground(new Color(34, 139, 34));
            lblAlertCount.setText("0 - All Good!");
        } else {
            lblAlertCount.setForeground(Color.RED);
        }
    }

    private void checkAndShowAlerts() {
        List<Stock> lowStockItems = stockController.getLowStock();
        if (!lowStockItems.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                    "ATTENTION: " + lowStockItems.size() + " item(s) need restocking!\n\n" +
                    "Please check the Low Stock Alerts panel or click 'Restock Alerts' for details.",
                    "Low Stock Alert",
                    JOptionPane.WARNING_MESSAGE);
            });
        }
    }

    private void showRestockAlertsDialog() {
        List<Stock> lowStockItems = stockController.getLowStock();
        
        if (lowStockItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All stock levels are healthy!\n\nNo items need restocking at this time.",
                "Restock Status",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("RESTOCK ALERT - ").append(lowStockItems.size()).append(" item(s) need attention:\n\n");
        
        for (Stock stock : lowStockItems) {
            Product product = productController.getProductById(stock.getProductId());
            String productName = product != null ? product.getName() : "Unknown";
            String status = stock.getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK";
            message.append(String.format("• %s: %d units (Min: %d) - %s\n",
                productName, stock.getQuantity(), stock.getRestockLevel(), status));
        }
        
        message.append("\nPlease arrange for restocking immediately.");
        
        JOptionPane.showMessageDialog(this,
            message.toString(),
            "Restock Alerts",
            JOptionPane.WARNING_MESSAGE);
    }

    private void openProductFrame() {
        dispose();
        new UnifiedManagementFrame(manager);
    }

    private void openCategoryFrame() {
        dispose();
        new UnifiedManagementFrame(manager);
    }

    private void openStockFrame() {
        dispose();
        new UnifiedManagementFrame(manager);
    }

    private void openUserManagementFrame() {
        dispose();
        new UnifiedManagementFrame(manager);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFramej().setVisible(true);
        }
    }

    // Custom cell renderer for status column
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String status = value.toString();
            if (status.equals("OUT OF STOCK")) {
                c.setBackground(new Color(255, 200, 200));
                c.setForeground(Color.RED);
            } else {
                c.setBackground(new Color(255, 255, 200));
                c.setForeground(new Color(200, 150, 0));
            }
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            return c;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        User manager = new User(1, "admin", "admin123", "Manager", "System Administrator");
        new ManagerFrame(manager);
    }
}