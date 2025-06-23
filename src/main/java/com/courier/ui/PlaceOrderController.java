package com.courier.ui;

import com.courier.model.User;
import com.courier.model.Order;
import com.courier.service.OrderService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaceOrderController extends JFrame {
    private User currentUser;
    private OrderService orderService;
    private JFrame parentFrame;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);

    public PlaceOrderController(JFrame parentFrame, User currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        this.orderService = new OrderService();
        
        initializeUI();
        setupPlaceOrderForm();
    }

    private void initializeUI() {
        setTitle("Place New Order - Courier Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
    }

    private void setupPlaceOrderForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Place New Order", JLabel.CENTER);
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
        
        // Sender information (read-only)
        JLabel senderInfoLabel = new JLabel("Sender Information:");
        senderInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        senderInfoLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(senderInfoLabel, gbc);
        gbc.gridwidth = 1;
        
        JTextField senderNameField = createReadOnlyField(currentUser.getFullName());
        JTextField senderContactField = createReadOnlyField(currentUser.getContactNumber());
        JTextField senderCityField = createReadOnlyField(currentUser.getCity());
        
        addFormField(formPanel, gbc, 1, "Sender Name:", senderNameField);
        addFormField(formPanel, gbc, 2, "Sender Contact:", senderContactField);
        addFormField(formPanel, gbc, 3, "Sender City:", senderCityField);
        
        // Separator
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separator, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Receiver information
        JLabel receiverInfoLabel = new JLabel("Receiver Information:");
        receiverInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        receiverInfoLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(receiverInfoLabel, gbc);
        gbc.gridwidth = 1;
        
        JTextField receiverNameField = createFormField();
        JTextField receiverContactField = createFormField();
        JTextField receiverAddressField = createFormField();
        JTextField receiverCityField = createFormField();
        JTextField receiverProvinceField = createFormField();
        
        addFormField(formPanel, gbc, 6, "Receiver Name:", receiverNameField);
        addFormField(formPanel, gbc, 7, "Receiver Contact:", receiverContactField);
        addFormField(formPanel, gbc, 8, "Receiver Address:", receiverAddressField);
        addFormField(formPanel, gbc, 9, "Receiver City:", receiverCityField);
        addFormField(formPanel, gbc, 10, "Receiver Province:", receiverProvinceField);
        
        // Package information
        JSeparator separator2 = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separator2, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        JLabel packageInfoLabel = new JLabel("Package Information:");
        packageInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        packageInfoLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2;
        formPanel.add(packageInfoLabel, gbc);
        gbc.gridwidth = 1;
        
        JTextField weightField = createFormField();
        addFormField(formPanel, gbc, 13, "Package Weight (kg):", weightField);
        
        // Rate display
        JLabel rateLabel = new JLabel("Estimated Rate: Rs. 0");
        rateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rateLabel.setForeground(SUCCESS_COLOR);
        gbc.gridx = 0; gbc.gridy = 14; gbc.gridwidth = 2;
        formPanel.add(rateLabel, gbc);
        
        // Calculate rate button
        JButton calculateButton = createStyledButton("Calculate Rate", SECONDARY_COLOR, 150, 35);
        calculateButton.addActionListener(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                if (weight <= 0 || weight > 20) {
                    showErrorMessage("Weight must be between 0.1 and 20 kg.");
                    return;
                }
                
                String receiverProvince = receiverProvinceField.getText().trim();
                if (receiverProvince.isEmpty()) {
                    showErrorMessage("Please enter receiver province first.");
                    return;
                }
                
                boolean sameProvince = receiverProvince.equalsIgnoreCase(currentUser.getProvince());
                double rate = orderService.calculateRate(weight, sameProvince);
                
                if (rate > 0) {
                    rateLabel.setText("Estimated Rate: Rs. " + rate);
                    rateLabel.setForeground(SUCCESS_COLOR);
                } else {
                    rateLabel.setText("Cannot calculate rate for this weight");
                    rateLabel.setForeground(DANGER_COLOR);
                }
            } catch (NumberFormatException ex) {
                showErrorMessage("Please enter a valid weight.");
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 15; gbc.gridwidth = 2;
        formPanel.add(calculateButton, gbc);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton placeButton = createStyledButton("Place Order", SUCCESS_COLOR, 150, 45);
        JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR, 150, 45);
        
        placeButton.addActionListener(e -> {
            if (validateForm(receiverNameField, receiverContactField, receiverAddressField,
                    receiverCityField, receiverProvinceField, weightField)) {
                
                try {
                    double weight = Double.parseDouble(weightField.getText().trim());
                    String receiverProvince = receiverProvinceField.getText().trim();
                    boolean sameProvince = receiverProvince.equalsIgnoreCase(currentUser.getProvince());
                    double rate = orderService.calculateRate(weight, sameProvince);
                    
                    if (rate <= 0) {
                        showErrorMessage("Invalid weight for shipping.");
                        return;
                    }
                    
                    int result = JOptionPane.showConfirmDialog(this,
                        "Confirm order placement?\n\nReceiver: " + receiverNameField.getText() +
                        "\nCity: " + receiverCityField.getText() +
                        "\nWeight: " + weight + " kg" +
                        "\nTotal Amount: Rs. " + rate,
                        "Confirm Order",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (result == JOptionPane.YES_OPTION) {
                        Order order = new Order();
                        order.setSenderName(currentUser.getFullName());
                        order.setSenderContact(currentUser.getContactNumber());
                        order.setSenderCity(currentUser.getCity());
                        order.setReceiverName(receiverNameField.getText().trim());
                        order.setReceiverContact(receiverContactField.getText().trim());
                        order.setReceiverAddress(receiverAddressField.getText().trim());
                        order.setReceiverCity(receiverCityField.getText().trim());
                        order.setRate(rate);
                        order.setStatus(Order.OrderStatus.IN_PROCESS);
                        order.setUsername(currentUser.getUsername());
                        
                        String trackingId = orderService.placeOrder(order);
                        if (trackingId != null) {
                            JOptionPane.showMessageDialog(this,
                                "Order placed successfully!\n\nTracking ID: " + trackingId +
                                "\nAmount: Rs. " + rate +
                                "\n\nPlease save your tracking ID for future reference.",
                                "Order Placed",
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            showErrorMessage("Failed to place order. Please try again.");
                        }
                    }
                } catch (NumberFormatException ex) {
                    showErrorMessage("Please enter a valid weight.");
                }
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(placeButton);
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

    private JTextField createReadOnlyField(String text) {
        JTextField field = createFormField();
        field.setText(text);
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240));
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

    private boolean validateForm(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showErrorMessage("Please fill in all fields.");
                return false;
            }
        }
        
        // Validate weight
        try {
            double weight = Double.parseDouble(fields[fields.length - 1].getText().trim());
            if (weight <= 0 || weight > 20) {
                showErrorMessage("Weight must be between 0.1 and 20 kg.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid weight.");
            return false;
        }
        
        return true;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
