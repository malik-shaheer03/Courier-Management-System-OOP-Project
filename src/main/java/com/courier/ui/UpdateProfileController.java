package com.courier.ui;

import com.courier.model.User;
import com.courier.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UpdateProfileController extends JFrame {
    private User currentUser;
    private UserService userService;
    private JFrame parentFrame;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);

    public UpdateProfileController(JFrame parentFrame, User currentUser, UserService userService) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        this.userService = userService;
        
        initializeUI();
        setupUpdateProfileForm();
    }

    private void initializeUI() {
        setTitle("Update Profile - Courier Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
    }

    private void setupUpdateProfileForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Update Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Current user info label
        JLabel infoLabel = new JLabel("Update your profile information:");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;
        
        // Pre-populate fields with current user data
        JTextField fullNameField = createFormField();
        fullNameField.setText(currentUser.getFullName());
        
        JTextField usernameField = createFormField();
        usernameField.setText(currentUser.getUsername());
        usernameField.setEditable(false); // Username cannot be changed
        usernameField.setBackground(new Color(240, 240, 240));
        
        JTextField contactField = createFormField();
        contactField.setText(currentUser.getContactNumber());
        
        JTextField addressField = createFormField();
        addressField.setText(currentUser.getAddress());
        
        JTextField cityField = createFormField();
        cityField.setText(currentUser.getCity());
        
        JTextField provinceField = createFormField();
        provinceField.setText(currentUser.getProvince());
        
        JPasswordField currentPinField = createPasswordField();
        JPasswordField newPinField = createPasswordField();
        JPasswordField confirmPinField = createPasswordField();
        
        // Add fields to form
        addFormField(formPanel, gbc, 1, "Full Name:", fullNameField);
        addFormField(formPanel, gbc, 2, "Username:", usernameField);
        addFormField(formPanel, gbc, 3, "Contact:", contactField);
        addFormField(formPanel, gbc, 4, "Address:", addressField);
        addFormField(formPanel, gbc, 5, "City:", cityField);
        addFormField(formPanel, gbc, 6, "Province:", provinceField);
        
        // Separator for PIN section
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separator, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        JLabel pinLabel = new JLabel("Change PIN (Optional):");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pinLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        formPanel.add(pinLabel, gbc);
        gbc.gridwidth = 1;
        
        addFormField(formPanel, gbc, 9, "Current PIN:", currentPinField);
        addFormField(formPanel, gbc, 10, "New PIN:", newPinField);
        addFormField(formPanel, gbc, 11, "Confirm New PIN:", confirmPinField);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton updateButton = createStyledButton("Update Profile", SUCCESS_COLOR, 150, 45);
        JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR, 150, 45);
        
        updateButton.addActionListener(e -> {
            if (validateForm(fullNameField, contactField, addressField, cityField, provinceField,
                    currentPinField, newPinField, confirmPinField)) {
                
                // Update user object
                currentUser.setFullName(fullNameField.getText().trim());
                currentUser.setContactNumber(contactField.getText().trim());
                currentUser.setAddress(addressField.getText().trim());
                currentUser.setCity(cityField.getText().trim());
                currentUser.setProvince(provinceField.getText().trim());
                
                if (userService.updateUser(currentUser)) {
                    JOptionPane.showMessageDialog(this,
                        "Profile updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    showErrorMessage("Failed to update profile. Please try again.");
                }
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(cancelButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JTextField createFormField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 30));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(25);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 30));
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(150, 30));
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private boolean validateForm(JTextField fullNameField, JTextField contactField,
                               JTextField addressField, JTextField cityField,
                               JTextField provinceField, JPasswordField currentPinField,
                               JPasswordField newPinField, JPasswordField confirmPinField) {
        
        // Validate required fields
        if (fullNameField.getText().trim().isEmpty() ||
            contactField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            cityField.getText().trim().isEmpty() ||
            provinceField.getText().trim().isEmpty()) {
            showErrorMessage("Please fill in all required fields.");
            return false;
        }
        
        // Check if PIN change is requested
        String currentPin = new String(currentPinField.getPassword()).trim();
        String newPin = new String(newPinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();
        
        if (!currentPin.isEmpty() || !newPin.isEmpty() || !confirmPin.isEmpty()) {
            // PIN change requested
            if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
                showErrorMessage("Please fill in all PIN fields to change your PIN.");
                return false;
            }
            
            if (!currentPin.equals(currentUser.getPin())) {
                showErrorMessage("Current PIN is incorrect.");
                return false;
            }
            
            if (!newPin.equals(confirmPin)) {
                showErrorMessage("New PIN and Confirm PIN do not match.");
                return false;
            }
            
            if (newPin.length() < 4) {
                showErrorMessage("New PIN must be at least 4 characters long.");
                return false;
            }
            
            // Update PIN
            currentUser.setPin(newPin);
        }
        
        return true;
    }

    private JButton createStyledButton(String text, Color backgroundColor, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
