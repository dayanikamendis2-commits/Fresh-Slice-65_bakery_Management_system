package view;

import controller.StockController;
import controller.ProductController;
import database.Validator;
import model.Stock;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StockFrame extends JFrame {

    private JTextField txtId, txtQuantity, txtRestockLevel;
    private JComboBox<Product> cmbProduct;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnShowLowStock;
    private JTable tblStock;
    private DefaultTableModel model;

    private StockController stockController;
    private ProductController productController;

    public StockFrame() {
        stockController = new StockController();
        productController = new ProductController();
        initComponents();
        loadProducts();
        loadStock();
    }

    private void initComponents() {
        setTitle("Stock Management - Fresh Slice 65");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Stock Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(280, 10, 200, 30);
        add(lblTitle);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(110, 55, 80, 25);
        txtId.setEditable(false);
        add(txtId);

        JLabel lblProduct = new JLabel("Product:");
        lblProduct.setBounds(20, 90, 80, 25);
        add(lblProduct);

        cmbProduct = new JComboBox<>();
        cmbProduct.setBounds(110, 90, 220, 25);
        add(cmbProduct);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(20, 125, 80, 25);
        add(lblQuantity);

        txtQuantity = new JTextField();
        txtQuantity.setBounds(110, 125, 100, 25);
        add(txtQuantity);

        JLabel lblRestockLevel = new JLabel("Restock At:");
        lblRestockLevel.setBounds(20, 160, 80, 25);
        add(lblRestockLevel);

        txtRestockLevel = new JTextField();
        txtRestockLevel.setBounds(110, 160, 100, 25);
        txtRestockLevel.setText("10"); // Default value
        add(txtRestockLevel);

        // Buttons
        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, 200, 80, 30);
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(110, 200, 80, 30);
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(200, 200, 80, 30);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setBounds(290, 200, 80, 30);
        add(btnClear);

        btnShowLowStock = new JButton("⚠️ Show Low Stock");
        btnShowLowStock.setBounds(500, 55, 180, 30);
        btnShowLowStock.setBackground(new Color(255, 165, 0));
        btnShowLowStock.setForeground(Color.BLACK);
        add(btnShowLowStock);

        JButton btnShowAll = new JButton("Show All Stock");
        btnShowAll.setBounds(500, 95, 180, 30);
        btnShowAll.addActionListener(e -> loadStock());
        add(btnShowAll);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Product", "Quantity", "Restock Level", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblStock = new JTable(model);
        tblStock.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scroll = new JScrollPane(tblStock);
        scroll.setBounds(20, 250, 690, 250);
        add(scroll);

        // Event Listeners
        btnAdd.addActionListener(e -> addStock());
        btnUpdate.addActionListener(e -> updateStock());
        btnDelete.addActionListener(e -> deleteStock());
        btnClear.addActionListener(e -> clearForm());
        btnShowLowStock.addActionListener(e -> loadLowStock());

        tblStock.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblStock.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    selectProductByName(model.getValueAt(row, 1).toString());
                    txtQuantity.setText(model.getValueAt(row, 2).toString());
                    txtRestockLevel.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        setVisible(true);
    }

    private void loadProducts() {
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
    }

    private void selectProductByName(String productName) {
        for (int i = 0; i < cmbProduct.getItemCount(); i++) {
            Product p = cmbProduct.getItemAt(i);
            if (p.getName().equals(productName)) {
                cmbProduct.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadStock() {
        model.setRowCount(0);
        List<Stock> stocks = stockController.getAllStock();
        displayStock(stocks);
    }

    private void loadLowStock() {
        model.setRowCount(0);
        List<Stock> stocks = stockController.getLowStock();
        displayStock(stocks);
        
        if (stocks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No low stock items found. All inventory levels are healthy!",
                "Stock Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayStock(List<Stock> stocks) {
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

    private void addStock() {
        if (!Validator.isInteger(txtQuantity, "Quantity") || !Validator.isInteger(txtRestockLevel, "Restock Level")) {
            return;
        }
        
        if (cmbProduct.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a product!");
            return;
        }

        Stock s = new Stock();
        s.setProductId(((Product)cmbProduct.getSelectedItem()).getId());
        s.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
        s.setRestockLevel(Integer.parseInt(txtRestockLevel.getText().trim()));
        
        if (stockController.addStock(s)) {
            JOptionPane.showMessageDialog(this, "Stock added successfully!");
            clearForm();
            loadStock();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add stock. Product may already have stock entry.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStock() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a stock entry to update!");
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
            JOptionPane.showMessageDialog(this, "Stock updated successfully!");
            clearForm();
            loadStock();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update stock!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStock() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a stock entry to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this stock entry?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText().trim());
            if (stockController.deleteStock(id)) {
                JOptionPane.showMessageDialog(this, "Stock deleted successfully!");
                clearForm();
                loadStock();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete stock!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtQuantity.setText("");
        txtRestockLevel.setText("10");
        if (cmbProduct.getItemCount() > 0) {
            cmbProduct.setSelectedIndex(0);
        }
        tblStock.clearSelection();
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
}
