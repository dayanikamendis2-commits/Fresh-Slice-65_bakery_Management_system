package view;

import controller.ProductController;
import controller.CategoryController;
import model.Product;
import model.Category;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Counter Assistant Frame
 * Features: View product details, Add new products, Search items
 */
public class AssistantFrame extends JFrame {

    private JTextField txtSearch, txtName, txtPrice;
    private JComboBox<Category> cmbCategory;
    private JButton btnSearch, btnClear, btnAdd, btnViewDetails, btnLogout;
    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private JLabel lblWelcome;

    private ProductController productController;
    private CategoryController categoryController;
    private User currentUser;

    public AssistantFrame(User user) {
        this.currentUser = user;
        this.productController = new ProductController();
        this.categoryController = new CategoryController();
        initComponents();
        loadCategories();
        loadProducts();
    }

    private void initComponents() {
        setTitle("Fresh Slice 65 - Counter Assistant");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Fresh Slice 65 - Counter Assistant");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        String welcomeText = "Welcome, " + (currentUser.getFullname() != null ? currentUser.getFullname() : currentUser.getUsername());
        lblWelcome = new JLabel(welcomeText);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Products"));
        
        searchPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(25);
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchProducts());
        searchPanel.add(btnSearch);
        
        btnClear = new JButton("Show All");
        btnClear.addActionListener(e -> loadProducts());
        searchPanel.add(btnClear);

        // Add enter key listener to search field
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }
        });

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Products Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Product Name", "Category", "Price (Rs.)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        tblProducts.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblProducts.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblProducts.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblProducts.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblProducts);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Right Panel - Add Product Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Product"));
        formPanel.setPreferredSize(new Dimension(250, 300));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        txtName = new JTextField(15);
        namePanel.add(txtName);
        formPanel.add(namePanel);

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>();
        cmbCategory.setPreferredSize(new Dimension(150, 25));
        categoryPanel.add(cmbCategory);
        formPanel.add(categoryPanel);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.add(new JLabel("Price:"));
        txtPrice = new JTextField(10);
        pricePanel.add(txtPrice);
        formPanel.add(pricePanel);

        formPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("Add Product");
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addProduct());
        buttonPanel.add(btnAdd);
        formPanel.add(buttonPanel);

        formPanel.add(Box.createVerticalStrut(20));

        btnViewDetails = new JButton("View Selected Product Details");
        btnViewDetails.addActionListener(e -> viewProductDetails());
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        detailsPanel.add(btnViewDetails);
        formPanel.add(detailsPanel);

        mainPanel.add(formPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        JLabel lblFooter = new JLabel("Fresh Slice 65 Bakery Management System © 2024");
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadCategories() {
        cmbCategory.removeAllItems();
        List<Category> categories = categoryController.getAllCategories();
        for (Category c : categories) {
            cmbCategory.addItem(c);
        }
        cmbCategory.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) {
                    setText(((Category) value).getName());
                }
                return this;
            }
        });
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        txtSearch.setText("");
        List<Product> products = productController.getAllProducts();
        displayProducts(products);
    }

    private void displayProducts(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            Category c = categoryController.getCategoryById(p.getCategoryId());
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                c != null ? c.getName() : "Unknown",
                String.format("%.2f", p.getPrice())
            });
        }
    }

    private void searchProducts() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }
        List<Product> results = productController.searchProducts(searchTerm);
        displayProducts(results);
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No products found matching: " + searchTerm,
                "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addProduct() {
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();

        // Validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter product name!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return;
        }

        if (cmbCategory.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a category!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter product price!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive price!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        // Create and save product
        Product product = new Product();
        product.setName(name);
        product.setCategoryId(((Category) cmbCategory.getSelectedItem()).getId());
        product.setPrice(price);

        if (productController.addProduct(product)) {
            JOptionPane.showMessageDialog(this, "Product added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add product. Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewProductDetails() {
        int selectedRow = tblProducts.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = productController.getProductById(productId);
        
        if (product != null) {
            Category category = categoryController.getCategoryById(product.getCategoryId());
            String details = String.format(
                "Product Details\n\n" +
                "ID: %d\n" +
                "Name: %s\n" +
                "Category: %s\n" +
                "Price: Rs. %.2f",
                product.getId(),
                product.getName(),
                category != null ? category.getName() : "Unknown",
                product.getPrice()
            );
            JOptionPane.showMessageDialog(this, details, 
                "Product Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPrice.setText("");
        if (cmbCategory.getItemCount() > 0) {
            cmbCategory.setSelectedIndex(0);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFramej().setVisible(true);
        }
    }

    // For testing
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        User testUser = new User(1, "assistant", "assist123", "Assistant", "Counter Assistant");
        new AssistantFrame(testUser);
    }
}
