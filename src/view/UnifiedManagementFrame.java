package view;

import controller.*;
import database.Validator;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Unified Management Frame - All management features in one window with tabs
 */
public class UnifiedManagementFrame extends JFrame {

    private User currentUser;
    private JTabbedPane tabbedPane;

    public UnifiedManagementFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Fresh Slice 65 - Management System");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(139, 69, 19);
                Color color2 = new Color(101, 67, 33);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("■ Fresh Slice 65 - Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        String welcomeText = "● Welcome, " + (currentUser.getFullname() != null ? currentUser.getFullname() : currentUser.getUsername());
        JLabel lblWelcome = new JLabel(welcomeText);
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcome.setForeground(new Color(255, 255, 255, 230));

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(100, 35));
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(lblWelcome);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane with enhanced styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(51, 51, 51));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add tabs
        tabbedPane.addTab("» Products", createProductPanel());
        tabbedPane.addTab("» Categories", createCategoryPanel());
        tabbedPane.addTab("» Stock", createStockPanel());
        
        // Only managers can access user management
        if (currentUser.getRole().equalsIgnoreCase("Manager")) {
            tabbedPane.addTab("» Users", createUserManagementPanel());
            tabbedPane.addTab("» Alerts", createLowStockAlertsPanel());
        }

        add(tabbedPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));
        JLabel lblFooter = new JLabel("Fresh Slice 65 Bakery Management System (c) 2024 | Powered by Java Swing");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooter.setForeground(new Color(108, 117, 125));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ==================== PRODUCT PANEL ====================
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ProductController productController = new ProductController();
        CategoryController categoryController = new CategoryController();

        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtSearch = new JTextField();
        JComboBox<Category> cmbCategory = new JComboBox<>();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblProducts = new JTable(model);

        // Search Panel
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(20, 15, 50, 25);
        panel.add(lblSearch);

        txtSearch.setBounds(75, 15, 200, 25);
        panel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(285, 15, 80, 25);
        panel.add(btnSearch);

        JButton btnShowAll = new JButton("Show All");
        btnShowAll.setBounds(375, 15, 100, 25);
        panel.add(btnShowAll);

        // Form fields
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 50, 25);
        panel.add(lblId);

        txtId.setBounds(90, 55, 100, 25);
        txtId.setEditable(false);
        panel.add(txtId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 90, 70, 25);
        panel.add(lblName);

        txtName.setBounds(90, 90, 200, 25);
        panel.add(txtName);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(20, 125, 70, 25);
        panel.add(lblCategory);

        cmbCategory.setBounds(90, 125, 200, 25);
        panel.add(cmbCategory);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(20, 160, 70, 25);
        panel.add(lblPrice);

        txtPrice.setBounds(90, 160, 120, 25);
        panel.add(txtPrice);

        // Buttons with modern styling
        JButton btnAdd = createStyledButton("+ ADD", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setBounds(20, 205, 90, 35);
        panel.add(btnAdd);

        JButton btnUpdate = createStyledButton("EDIT", new Color(0, 123, 255), Color.WHITE);
        btnUpdate.setBounds(120, 205, 90, 35);
        panel.add(btnUpdate);

        JButton btnDelete = createStyledButton("DELETE", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setBounds(220, 205, 90, 35);
        panel.add(btnDelete);

        JButton btnClear = createStyledButton("CLEAR", new Color(108, 117, 125), Color.WHITE);
        btnClear.setBounds(320, 205, 90, 35);
        panel.add(btnClear);

        // Table with modern styling
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblProducts.setRowHeight(28);
        tblProducts.setGridColor(new Color(222, 226, 230));
        tblProducts.setSelectionBackground(new Color(0, 123, 255, 50));
        tblProducts.setSelectionForeground(new Color(0, 0, 0));
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblProducts.getTableHeader().setBackground(new Color(233, 236, 239));
        tblProducts.getTableHeader().setForeground(new Color(33, 37, 41));
        tblProducts.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 123, 255)));
        
        JScrollPane scroll = new JScrollPane(tblProducts);
        scroll.setBounds(20, 250, 1020, 340);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(scroll);

        // Load categories
        Runnable loadCategories = () -> {
            List<Category> categories = categoryController.getAllCategories();
            cmbCategory.removeAllItems();
            for (Category c : categories) {
                cmbCategory.addItem(c);
            }
            cmbCategory.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Category) {
                        setText(((Category) value).getName());
                    }
                    return this;
                }
            });
        };

        // Load products
        Runnable loadProducts = () -> {
            model.setRowCount(0);
            txtSearch.setText("");
            List<Product> products = productController.getAllProducts();
            for (Product p : products) {
                Category c = categoryController.getCategoryById(p.getCategoryId());
                model.addRow(new Object[]{p.getId(), p.getName(), c != null ? c.getName() : "", String.format("%.2f", p.getPrice())});
            }
        };

        // Search products
        Runnable searchProducts = () -> {
            String searchTerm = txtSearch.getText().trim();
            if (searchTerm.isEmpty()) {
                loadProducts.run();
                return;
            }
            model.setRowCount(0);
            List<Product> results = productController.searchProducts(searchTerm);
            for (Product p : results) {
                Category c = categoryController.getCategoryById(p.getCategoryId());
                model.addRow(new Object[]{p.getId(), p.getName(), c != null ? c.getName() : "", String.format("%.2f", p.getPrice())});
            }
        };

        // Clear form
        Runnable clearForm = () -> {
            txtId.setText("");
            txtName.setText("");
            txtPrice.setText("");
            if (cmbCategory.getItemCount() > 0) {
                cmbCategory.setSelectedIndex(0);
            }
            tblProducts.clearSelection();
        };

        // Event Listeners
        btnSearch.addActionListener(e -> searchProducts.run());
        btnShowAll.addActionListener(e -> loadProducts.run());

        btnAdd.addActionListener(e -> {
            if (!Validator.isNotEmpty(txtName, "Name") || !Validator.isNumeric(txtPrice, "Price")) return;
            
            if (cmbCategory.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Please select a category!");
                return;
            }

            Product p = new Product();
            p.setName(txtName.getText().trim());
            p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
            p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            
            if (productController.addProduct(p)) {
                JOptionPane.showMessageDialog(panel, "Product added successfully!");
                clearForm.run();
                loadProducts.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to add product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a product to update!");
                return;
            }
            if (!Validator.isNotEmpty(txtName, "Name") || !Validator.isNumeric(txtPrice, "Price")) return;

            Product p = new Product();
            p.setId(Integer.parseInt(txtId.getText().trim()));
            p.setName(txtName.getText().trim());
            p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
            p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            
            if (productController.updateProduct(p)) {
                JOptionPane.showMessageDialog(panel, "Product updated successfully!");
                clearForm.run();
                loadProducts.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to update product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a product to delete!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(panel, 
                "Are you sure you want to delete this product?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                if (productController.deleteProduct(id)) {
                    JOptionPane.showMessageDialog(panel, "Product deleted successfully!");
                    clearForm.run();
                    loadProducts.run();
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete product!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm.run());

        tblProducts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblProducts.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    String categoryName = model.getValueAt(row, 2).toString();
                    for (int i = 0; i < cmbCategory.getItemCount(); i++) {
                        Category c = cmbCategory.getItemAt(i);
                        if (c.getName().equals(categoryName)) {
                            cmbCategory.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtPrice.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts.run();
                }
            }
        });

        // Initial load
        loadCategories.run();
        loadProducts.run();

        return panel;
    }

    // ==================== CATEGORY PANEL ====================
    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        CategoryController categoryController = new CategoryController();

        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblCategories = new JTable(model);

        JLabel lblTitle = new JLabel("■ Category Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(52, 58, 64));
        lblTitle.setBounds(170, 10, 300, 35);
        panel.add(lblTitle);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 50, 25);
        panel.add(lblId);

        txtId.setBounds(90, 55, 100, 25);
        txtId.setEditable(false);
        panel.add(txtId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 90, 70, 25);
        panel.add(lblName);

        txtName.setBounds(90, 90, 200, 25);
        panel.add(txtName);

        JButton btnAdd = createStyledButton("+ ADD", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setBounds(20, 130, 90, 35);
        panel.add(btnAdd);

        JButton btnUpdate = createStyledButton("EDIT", new Color(0, 123, 255), Color.WHITE);
        btnUpdate.setBounds(120, 130, 90, 35);
        panel.add(btnUpdate);

        JButton btnDelete = createStyledButton("DELETE", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setBounds(220, 130, 90, 35);
        panel.add(btnDelete);

        JButton btnClear = createStyledButton("CLEAR", new Color(108, 117, 125), Color.WHITE);
        btnClear.setBounds(320, 130, 90, 35);
        panel.add(btnClear);

        tblCategories.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblCategories.setRowHeight(28);
        tblCategories.setGridColor(new Color(222, 226, 230));
        tblCategories.setSelectionBackground(new Color(0, 123, 255, 50));
        tblCategories.setSelectionForeground(new Color(0, 0, 0));
        tblCategories.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblCategories.getTableHeader().setBackground(new Color(233, 236, 239));
        tblCategories.getTableHeader().setForeground(new Color(33, 37, 41));
        tblCategories.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(60, 179, 113)));
        
        JScrollPane scroll = new JScrollPane(tblCategories);
        scroll.setBounds(20, 180, 1020, 410);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(scroll);

        // Load categories
        Runnable loadCategories = () -> {
            model.setRowCount(0);
            List<Category> list = categoryController.getAllCategories();
            for (Category c : list) {
                model.addRow(new Object[]{c.getId(), c.getName()});
            }
        };

        // Clear form
        Runnable clearForm = () -> {
            txtId.setText("");
            txtName.setText("");
            tblCategories.clearSelection();
        };

        // Event Listeners
        btnAdd.addActionListener(e -> {
            if (!Validator.isNotEmpty(txtName, "Category Name")) {
                return;
            }
            
            Category c = new Category();
            c.setName(txtName.getText().trim());
            
            if (categoryController.addCategory(c)) {
                JOptionPane.showMessageDialog(panel, "Category added successfully!");
                clearForm.run();
                loadCategories.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to add category!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a category to update!");
                return;
            }
            if (!Validator.isNotEmpty(txtName, "Category Name")) {
                return;
            }
            
            Category c = new Category();
            c.setId(Integer.parseInt(txtId.getText().trim()));
            c.setName(txtName.getText().trim());
            
            if (categoryController.updateCategory(c)) {
                JOptionPane.showMessageDialog(panel, "Category updated successfully!");
                clearForm.run();
                loadCategories.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to update category!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a category to delete!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to delete this category?\nThis will also delete all products in this category!",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                if (categoryController.deleteCategory(id)) {
                    JOptionPane.showMessageDialog(panel, "Category deleted successfully!");
                    clearForm.run();
                    loadCategories.run();
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete category!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm.run());

        tblCategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblCategories.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                }
            }
        });

        // Initial load
        loadCategories.run();

        return panel;
    }

    // ==================== STOCK PANEL ====================
    private JPanel createStockPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        StockController stockController = new StockController();
        ProductController productController = new ProductController();

        JTextField txtId = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtRestockLevel = new JTextField();
        JComboBox<Product> cmbProduct = new JComboBox<>();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Product", "Quantity", "Restock Level", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblStock = new JTable(model);
        tblStock.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        JLabel lblTitle = new JLabel("■ Stock Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(52, 58, 64));
        lblTitle.setBounds(280, 10, 250, 35);
        panel.add(lblTitle);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 80, 25);
        panel.add(lblId);

        txtId.setBounds(110, 55, 80, 25);
        txtId.setEditable(false);
        panel.add(txtId);

        JLabel lblProduct = new JLabel("Product:");
        lblProduct.setBounds(20, 90, 80, 25);
        panel.add(lblProduct);

        cmbProduct.setBounds(110, 90, 220, 25);
        panel.add(cmbProduct);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(20, 125, 80, 25);
        panel.add(lblQuantity);

        txtQuantity.setBounds(110, 125, 100, 25);
        panel.add(txtQuantity);

        JLabel lblRestockLevel = new JLabel("Restock At:");
        lblRestockLevel.setBounds(20, 160, 80, 25);
        panel.add(lblRestockLevel);

        txtRestockLevel.setBounds(110, 160, 100, 25);
        txtRestockLevel.setText("10");
        panel.add(txtRestockLevel);

        // Buttons
        JButton btnAdd = createStyledButton("+ ADD", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setBounds(20, 200, 90, 35);
        panel.add(btnAdd);

        JButton btnUpdate = createStyledButton("EDIT", new Color(0, 123, 255), Color.WHITE);
        btnUpdate.setBounds(120, 200, 90, 35);
        panel.add(btnUpdate);

        JButton btnDelete = createStyledButton("DELETE", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setBounds(220, 200, 90, 35);
        panel.add(btnDelete);

        JButton btnClear = createStyledButton("CLEAR", new Color(108, 117, 125), Color.WHITE);
        btnClear.setBounds(320, 200, 90, 35);
        panel.add(btnClear);

        JButton btnShowLowStock = createStyledButton("! LOW STOCK", new Color(255, 193, 7), new Color(0, 0, 0));
        btnShowLowStock.setBounds(500, 55, 150, 35);
        panel.add(btnShowLowStock);

        JButton btnShowAll = createStyledButton("SHOW ALL", new Color(23, 162, 184), Color.WHITE);
        btnShowAll.setBounds(660, 55, 130, 35);
        panel.add(btnShowAll);

        // Table
        tblStock.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblStock.setRowHeight(28);
        tblStock.setGridColor(new Color(222, 226, 230));
        tblStock.setSelectionBackground(new Color(0, 123, 255, 50));
        tblStock.setSelectionForeground(new Color(0, 0, 0));
        tblStock.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblStock.getTableHeader().setBackground(new Color(233, 236, 239));
        tblStock.getTableHeader().setForeground(new Color(33, 37, 41));
        tblStock.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 193, 7)));
        
        JScrollPane scroll = new JScrollPane(tblStock);
        scroll.setBounds(20, 250, 1020, 340);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(scroll);

        // Load products
        Runnable loadProducts = () -> {
            List<Product> products = productController.getAllProducts();
            cmbProduct.removeAllItems();
            for (Product p : products) {
                cmbProduct.addItem(p);
            }
            cmbProduct.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Product) {
                        setText(((Product) value).getName());
                    }
                    return this;
                }
            });
        };

        // Display stock helper class
        class StockDisplayHelper {
            void displayStock(List<Stock> stocks) {
                model.setRowCount(0);
                for (Stock s : stocks) {
                    Product p = productController.getProductById(s.getProductId());
                    String status;
                    if (s.getQuantity() == 0) {
                        status = "OUT OF STOCK";
                    } else if (s.getQuantity() <= s.getRestockLevel()) {
                        status = "LOW STOCK";
                    } else {
                        status = "OK";
                    }
                    model.addRow(new Object[]{
                        s.getId(),
                        p != null ? p.getName() : "Unknown",
                        s.getQuantity(),
                        s.getRestockLevel(),
                        status
                    });
                }
            }
        }
        StockDisplayHelper displayHelper = new StockDisplayHelper();

        // Load stock
        Runnable loadStock = () -> {
            List<Stock> stocks = stockController.getAllStock();
            displayHelper.displayStock(stocks);
        };

        // Load low stock
        Runnable loadLowStock = () -> {
            List<Stock> stocks = stockController.getLowStock();
            displayHelper.displayStock(stocks);
            
            if (stocks.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "No low stock items found. All inventory levels are healthy!",
                    "Stock Status", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // Clear form
        Runnable clearForm = () -> {
            txtId.setText("");
            txtQuantity.setText("");
            txtRestockLevel.setText("10");
            if (cmbProduct.getItemCount() > 0) {
                cmbProduct.setSelectedIndex(0);
            }
            tblStock.clearSelection();
        };

        // Event Listeners
        btnAdd.addActionListener(e -> {
            if (!Validator.isInteger(txtQuantity, "Quantity") || !Validator.isInteger(txtRestockLevel, "Restock Level")) {
                return;
            }
            
            if (cmbProduct.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Please select a product!");
                return;
            }

            Stock s = new Stock();
            s.setProductId(((Product)cmbProduct.getSelectedItem()).getId());
            s.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
            s.setRestockLevel(Integer.parseInt(txtRestockLevel.getText().trim()));
            
            if (stockController.addStock(s)) {
                JOptionPane.showMessageDialog(panel, "Stock added successfully!");
                clearForm.run();
                loadStock.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to add stock. Product may already have stock entry.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a stock entry to update!");
                return;
            }
            if (!Validator.isInteger(txtQuantity, "Quantity") || !Validator.isInteger(txtRestockLevel, "Restock Level")) {
                return;
            }

            Stock s = new Stock();
            s.setId(Integer.parseInt(txtId.getText().trim()));
            s.setProductId(((Product)cmbProduct.getSelectedItem()).getId());
            s.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
            s.setRestockLevel(Integer.parseInt(txtRestockLevel.getText().trim()));
            
            if (stockController.updateStock(s)) {
                JOptionPane.showMessageDialog(panel, "Stock updated successfully!");
                clearForm.run();
                loadStock.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to update stock!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a stock entry to delete!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to delete this stock entry?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                if (stockController.deleteStock(id)) {
                    JOptionPane.showMessageDialog(panel, "Stock deleted successfully!");
                    clearForm.run();
                    loadStock.run();
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete stock!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm.run());
        btnShowAll.addActionListener(e -> loadStock.run());
        btnShowLowStock.addActionListener(e -> loadLowStock.run());

        tblStock.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblStock.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    String productName = model.getValueAt(row, 1).toString();
                    for (int i = 0; i < cmbProduct.getItemCount(); i++) {
                        Product p = cmbProduct.getItemAt(i);
                        if (p.getName().equals(productName)) {
                            cmbProduct.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtQuantity.setText(model.getValueAt(row, 2).toString());
                    txtRestockLevel.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        // Initial load
        loadProducts.run();
        loadStock.run();

        return panel;
    }

    // ==================== USER MANAGEMENT PANEL ====================
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        UserController userController = new UserController();

        JTextField txtUsername = new JTextField();
        JTextField txtFullname = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Assistant", "Manager"});
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Full Name", "Username", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblUsers = new JTable(tableModel);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel lblTitle = new JLabel("■ User Management");
        lblTitle.setBounds(320, 15, 350, 35);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(52, 58, 64));
        panel.add(lblTitle);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        formPanel.setBounds(20, 55, 360, 280);
        panel.add(formPanel);

        JLabel lblFullname = new JLabel("Full Name:");
        lblFullname.setBounds(20, 30, 100, 25);
        formPanel.add(lblFullname);

        txtFullname.setBounds(130, 30, 200, 25);
        formPanel.add(txtFullname);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 70, 100, 25);
        formPanel.add(lblUsername);

        txtUsername.setBounds(130, 70, 200, 25);
        formPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 110, 100, 25);
        formPanel.add(lblPassword);

        txtPassword.setBounds(130, 110, 200, 25);
        formPanel.add(txtPassword);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(20, 150, 100, 25);
        formPanel.add(lblRole);

        cmbRole.setBounds(130, 150, 200, 25);
        formPanel.add(cmbRole);

        // Buttons
        JButton btnAdd = createStyledButton("+ ADD USER", new Color(40, 167, 69), Color.WHITE);
        btnAdd.setBounds(20, 200, 100, 35);
        formPanel.add(btnAdd);

        JButton btnUpdate = createStyledButton("EDIT", new Color(0, 123, 255), Color.WHITE);
        btnUpdate.setBounds(130, 200, 100, 35);
        formPanel.add(btnUpdate);

        JButton btnDelete = createStyledButton("DELETE", new Color(220, 53, 69), Color.WHITE);
        btnDelete.setBounds(240, 200, 100, 35);
        formPanel.add(btnDelete);

        JButton btnClear = createStyledButton("CLEAR", new Color(108, 117, 125), Color.WHITE);
        btnClear.setBounds(75, 240, 100, 32);
        formPanel.add(btnClear);

        JButton btnRefresh = createStyledButton("REFRESH", new Color(23, 162, 184), Color.WHITE);
        btnRefresh.setBounds(185, 240, 120, 32);
        formPanel.add(btnRefresh);

        // Table
        tblUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblUsers.setRowHeight(28);
        tblUsers.setGridColor(new Color(222, 226, 230));
        tblUsers.setSelectionBackground(new Color(0, 123, 255, 50));
        tblUsers.setSelectionForeground(new Color(0, 0, 0));
        tblUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblUsers.getTableHeader().setBackground(new Color(233, 236, 239));
        tblUsers.getTableHeader().setForeground(new Color(33, 37, 41));
        tblUsers.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(138, 43, 226)));
        
        JScrollPane tableScroll = new JScrollPane(tblUsers);
        tableScroll.setBounds(400, 55, 640, 535);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(tableScroll);

        // Load user table
        Runnable loadUserTable = () -> {
            tableModel.setRowCount(0);
            List<User> users = userController.getAllUsers();

            for (User user : users) {
                tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getFullname() != null ? user.getFullname() : "",
                    user.getUsername(),
                    user.getRole()
                });
            }
        };

        // Clear fields
        Runnable clearFields = () -> {
            txtFullname.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
            cmbRole.setSelectedIndex(0);
            tblUsers.clearSelection();
        };

        // Event Listeners
        btnAdd.addActionListener(e -> {
            String fullname = txtFullname.getText().trim();
            String username = txtUsername.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());
            String role = (String) cmbRole.getSelectedItem();

            if (!Validator.isNotEmpty(fullname, username, password, role)) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (username.length() < 3) {
                JOptionPane.showMessageDialog(panel, "Username must be at least 3 characters!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }

            if (password.length() < 4) {
                JOptionPane.showMessageDialog(panel, "Password must be at least 4 characters!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return;
            }

            User user = new User(username, password, role, fullname);

            if (userController.insertUser(user)) {
                JOptionPane.showMessageDialog(panel, "User '" + username + "' added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserTable.run();
                clearFields.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Error adding user. Username may already exist.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int selected = tblUsers.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a user to update!", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tblUsers.getValueAt(selected, 0).toString());
            String fullname = txtFullname.getText().trim();
            String username = txtUsername.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());
            String role = (String) cmbRole.getSelectedItem();

            if (!Validator.isNotEmpty(fullname, username, role)) {
                JOptionPane.showMessageDialog(panel, "Please fill all required fields!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.isEmpty()) {
                User existingUser = userController.getUserById(id);
                if (existingUser != null) {
                    password = existingUser.getPassword();
                }
            }

            User user = new User(id, username, password, role, fullname);

            if (userController.updateUser(user)) {
                JOptionPane.showMessageDialog(panel, "User updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserTable.run();
                clearFields.run();
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to update user!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selected = tblUsers.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a user to delete!", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tblUsers.getValueAt(selected, 0).toString());
            String username = tblUsers.getValueAt(selected, 2).toString();

            int confirm = JOptionPane.showConfirmDialog(panel, 
                "Are you sure you want to delete user '" + username + "'?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (userController.deleteUser(id)) {
                    JOptionPane.showMessageDialog(panel, "User deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserTable.run();
                    clearFields.run();
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete user!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearFields.run());
        btnRefresh.addActionListener(e -> loadUserTable.run());

        tblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = tblUsers.getSelectedRow();
                if (selected != -1) {
                    txtFullname.setText(tblUsers.getValueAt(selected, 1).toString());
                    txtUsername.setText(tblUsers.getValueAt(selected, 2).toString());
                    txtPassword.setText("");
                    String role = tblUsers.getValueAt(selected, 3).toString();
                    cmbRole.setSelectedItem(role);
                }
            }
        });

        // Initial load
        loadUserTable.run();

        return panel;
    }

    // ==================== LOW STOCK ALERTS PANEL ====================
    private JPanel createLowStockAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        StockController stockController = new StockController();
        ProductController productController = new ProductController();

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel lblTitle = new JLabel("! LOW STOCK ALERTS - Items Needing Restock");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(220, 53, 69));
        headerPanel.add(lblTitle);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel lowStockModel = new DefaultTableModel(new String[]{"Product", "Current Qty", "Restock Level", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblLowStock = new JTable(lowStockModel);
        tblLowStock.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        tblLowStock.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblLowStock.setRowHeight(30);
        tblLowStock.setGridColor(new Color(222, 226, 230));
        tblLowStock.setSelectionBackground(new Color(255, 193, 7, 50));
        tblLowStock.setSelectionForeground(new Color(0, 0, 0));
        tblLowStock.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblLowStock.getTableHeader().setBackground(new Color(233, 236, 239));
        tblLowStock.getTableHeader().setForeground(new Color(33, 37, 41));
        tblLowStock.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 53, 69)));

        JScrollPane scrollPane = new JScrollPane(tblLowStock);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        JButton btnRefresh = createStyledButton("REFRESH ALERTS", new Color(255, 193, 7), new Color(0, 0, 0));
        btnRefresh.setPreferredSize(new Dimension(160, 40));
        buttonPanel.add(btnRefresh);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load low stock alerts
        Runnable loadLowStockAlerts = () -> {
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
            
            lblTitle.setText("! LOW STOCK ALERTS - " + lowStockItems.size() + " Items Needing Restock");
            
            if (lowStockItems.isEmpty()) {
                lblTitle.setForeground(new Color(40, 167, 69));
                lblTitle.setText("» LOW STOCK ALERTS - All Good! No items need restocking.");
            } else {
                lblTitle.setForeground(new Color(220, 53, 69));
            }
        };

        btnRefresh.addActionListener(e -> loadLowStockAlerts.run());

        // Initial load
        loadLowStockAlerts.run();

        return panel;
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
            } else if (status.equals("LOW STOCK")) {
                c.setBackground(new Color(255, 255, 200));
                c.setForeground(new Color(200, 150, 0));
            } else {
                c.setBackground(new Color(200, 255, 200));
                c.setForeground(new Color(0, 128, 0));
            }
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            return c;
        }
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
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
}
