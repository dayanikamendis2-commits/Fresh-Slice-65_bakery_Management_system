package view;

import controller.UserController;
import database.Validator;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * User Management Frame for Production Manager
 * Features: Create user accounts, view, update, delete users
 */
public class UserManagementFrame extends JFrame {

    private JTable tblUsers;
    private JTextField txtUsername, txtFullname;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh;
    private DefaultTableModel tableModel;

    private final UserController userController;

    public UserManagementFrame() {
        userController = new UserController();
        initComponents();
        loadUserTable();
    }

    private void initComponents() {
        setTitle("User Management - Fresh Slice 65");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("User Management");
        lblTitle.setBounds(320, 15, 300, 30);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitle);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        formPanel.setBounds(20, 55, 360, 280);
        add(formPanel);

        JLabel lblFullname = new JLabel("Full Name:");
        lblFullname.setBounds(20, 30, 100, 25);
        formPanel.add(lblFullname);

        txtFullname = new JTextField();
        txtFullname.setBounds(130, 30, 200, 25);
        formPanel.add(txtFullname);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 70, 100, 25);
        formPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 70, 200, 25);
        formPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 110, 100, 25);
        formPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 110, 200, 25);
        formPanel.add(txtPassword);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(20, 150, 100, 25);
        formPanel.add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"Assistant", "Manager"});
        cmbRole.setBounds(130, 150, 200, 25);
        formPanel.add(cmbRole);

        // Buttons
        btnAdd = new JButton("Add User");
        btnAdd.setBounds(20, 200, 100, 35);
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        formPanel.add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 200, 100, 35);
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        formPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(240, 200, 100, 35);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        formPanel.add(btnDelete);

        btnClear = new JButton("Clear Form");
        btnClear.setBounds(75, 240, 100, 30);
        formPanel.add(btnClear);

        btnRefresh = new JButton("Refresh Table");
        btnRefresh.setBounds(185, 240, 120, 30);
        formPanel.add(btnRefresh);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Full Name", "Username", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsers = new JTable(tableModel);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane tableScroll = new JScrollPane(tblUsers);
        tableScroll.setBounds(400, 55, 420, 440);
        add(tableScroll);

        // Event Listeners
        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnRefresh.addActionListener(e -> loadUserTable());
        btnClear.addActionListener(e -> clearFields());

        tblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedUserIntoFields();
            }
        });

        setVisible(true);
    }

    private void loadUserTable() {
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
    }

    private void addUser() {
        String fullname = txtFullname.getText().trim();
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();

        // Validation
        if (!Validator.isNotEmpty(fullname, username, password, role)) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        User user = new User(username, password, role, fullname);

        if (userController.insertUser(user)) {
            JOptionPane.showMessageDialog(this, "User '" + username + "' added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding user. Username may already exist.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int selected = tblUsers.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tblUsers.getValueAt(selected, 0).toString());
        String fullname = txtFullname.getText().trim();
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();

        if (!Validator.isNotEmpty(fullname, username, role)) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // If password is empty, keep the old one (get from database)
        if (password.isEmpty()) {
            User existingUser = userController.getUserById(id);
            if (existingUser != null) {
                password = existingUser.getPassword();
            }
        }

        User user = new User(id, username, password, role, fullname);

        if (userController.updateUser(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update user!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selected = tblUsers.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tblUsers.getValueAt(selected, 0).toString());
        String username = tblUsers.getValueAt(selected, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user '" + username + "'?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userController.deleteUser(id)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadSelectedUserIntoFields() {
        int selected = tblUsers.getSelectedRow();
        if (selected != -1) {
            txtFullname.setText(tblUsers.getValueAt(selected, 1).toString());
            txtUsername.setText(tblUsers.getValueAt(selected, 2).toString());
            txtPassword.setText(""); // Don't show password
            String role = tblUsers.getValueAt(selected, 3).toString();
            cmbRole.setSelectedItem(role);
        }
    }

    private void clearFields() {
        txtFullname.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        tblUsers.clearSelection();
    }
}
