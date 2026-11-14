package view;

import controller.CategoryController;
import database.Validator;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CategoryFrame extends JFrame {

    private JTextField txtId, txtName;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblCategories;
    private DefaultTableModel model;

    private final CategoryController categoryController;

    public CategoryFrame() {
        categoryController = new CategoryController();
        initComponents();
        loadCategories();
    }

    private void initComponents() {
        setTitle("Category Management - Fresh Slice 65");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Category Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(170, 10, 250, 30);
        add(lblTitle);

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

        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, 130, 80, 30);
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(110, 130, 80, 30);
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(200, 130, 80, 30);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setBounds(290, 130, 80, 30);
        add(btnClear);

        model = new DefaultTableModel(new String[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCategories = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblCategories);
        scroll.setBounds(20, 180, 490, 220);
        add(scroll);

        // Event Listeners
        btnAdd.addActionListener(e -> addCategory());
        btnUpdate.addActionListener(e -> updateCategory());
        btnDelete.addActionListener(e -> deleteCategory());
        btnClear.addActionListener(e -> clearForm());

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

        setVisible(true);
    }

    private void loadCategories() {
        model.setRowCount(0);
        List<Category> list = categoryController.getAllCategories();
        for (Category c : list) {
            model.addRow(new Object[]{c.getId(), c.getName()});
        }
    }

    private void addCategory() {
        if (!Validator.isNotEmpty(txtName, "Category Name")) {
            return;
        }
        
        Category c = new Category();
        c.setName(txtName.getText().trim());
        
        if (categoryController.addCategory(c)) {
            JOptionPane.showMessageDialog(this, "Category added successfully!");
            clearForm();
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add category!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category to update!");
            return;
        }
        if (!Validator.isNotEmpty(txtName, "Category Name")) {
            return;
        }
        
        Category c = new Category();
        c.setId(Integer.parseInt(txtId.getText().trim()));
        c.setName(txtName.getText().trim());
        
        if (categoryController.updateCategory(c)) {
            JOptionPane.showMessageDialog(this, "Category updated successfully!");
            clearForm();
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update category!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this category?\nThis will also delete all products in this category!",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText().trim());
            if (categoryController.deleteCategory(id)) {
                JOptionPane.showMessageDialog(this, "Category deleted successfully!");
                clearForm();
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete category!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        tblCategories.clearSelection();
    }
}
