package view;

import controller.ProductController;
import controller.CategoryController;
import database.Validator;
import model.Product;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProductFrame extends JFrame {

    private JTextField txtId, txtName, txtPrice, txtSearch;
    private JComboBox<Category> cmbCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tblProducts;
    private DefaultTableModel model;

    private ProductController productController;
    private CategoryController categoryController;

    public ProductFrame() {
        productController = new ProductController();
        categoryController = new CategoryController();
        initComponents();
        loadCategories();
        loadProducts();
    }

    private void initComponents() {
        setTitle("Product Management - Fresh Slice 65");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        // Search Panel
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(20, 15, 50, 25);
        add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(75, 15, 200, 25);
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(285, 15, 80, 25);
        btnSearch.addActionListener(e -> searchProducts());
        add(btnSearch);

        JButton btnShowAll = new JButton("Show All");
        btnShowAll.setBounds(375, 15, 100, 25);
        btnShowAll.addActionListener(e -> loadProducts());
        add(btnShowAll);

        // Form fields
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 50, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(90, 55, 100, 25);
        txtId.setEditable(false);
        add(txtId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 90, 70, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(90, 90, 200, 25);
        add(txtName);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(20, 125, 70, 25);
        add(lblCategory);

        cmbCategory = new JComboBox<>();
        cmbCategory.setBounds(90, 125, 200, 25);
        add(cmbCategory);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(20, 160, 70, 25);
        add(lblPrice);

        txtPrice = new JTextField();
        txtPrice.setBounds(90, 160, 120, 25);
        add(txtPrice);

        // Buttons
        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, 205, 80, 30);
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(110, 205, 80, 30);
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(200, 205, 80, 30);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setBounds(290, 205, 80, 30);
        add(btnClear);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblProducts);
        scroll.setBounds(20, 250, 740, 250);
        add(scroll);

        // Event Listeners
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearForm());

        tblProducts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblProducts.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    selectCategoryByName(model.getValueAt(row, 2).toString());
                    txtPrice.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }
        });

        setVisible(true);
    }

    private void loadCategories() {
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
    }

    private void selectCategoryByName(String categoryName) {
        for (int i = 0; i < cmbCategory.getItemCount(); i++) {
            Category c = cmbCategory.getItemAt(i);
            if (c.getName().equals(categoryName)) {
                cmbCategory.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadProducts() {
        model.setRowCount(0);
        txtSearch.setText("");
        List<Product> products = productController.getAllProducts();
        displayProducts(products);
    }

    private void displayProducts(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            Category c = categoryController.getCategoryById(p.getCategoryId());
            model.addRow(new Object[]{p.getId(), p.getName(), c != null ? c.getName() : "", String.format("%.2f", p.getPrice())});
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
    }

    private void addProduct() {
        if (!Validator.isNotEmpty(txtName, "Name") || !Validator.isNumeric(txtPrice, "Price")) return;
        
        if (cmbCategory.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a category!");
            return;
        }

        Product p = new Product();
        p.setName(txtName.getText().trim());
        p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
        p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        
        if (productController.addProduct(p)) {
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearForm();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add product!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to update!");
            return;
        }
        if (!Validator.isNotEmpty(txtName, "Name") || !Validator.isNumeric(txtPrice, "Price")) return;

        Product p = new Product();
        p.setId(Integer.parseInt(txtId.getText().trim()));
        p.setName(txtName.getText().trim());
        p.setCategoryId(((Category)cmbCategory.getSelectedItem()).getId());
        p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        
        if (productController.updateProduct(p)) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            clearForm();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update product!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this product?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText().trim());
            if (productController.deleteProduct(id)) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearForm();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        if (cmbCategory.getItemCount() > 0) {
            cmbCategory.setSelectedIndex(0);
        }
        tblProducts.clearSelection();
    }
}