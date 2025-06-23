package com.courier.ui;

import com.courier.model.User;
import com.courier.model.Order;
import com.courier.service.OrderService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TrackOrderController extends JFrame {
    private User currentUser;
    private OrderService orderService;
    private JFrame parentFrame;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public TrackOrderController(JFrame parentFrame, User currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        this.orderService = new OrderService();
        
        initializeUI();
        setupTrackOrderForm();
    }

    private void initializeUI() {
        setTitle("Track Order - Courier Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
    }

    private void setupTrackOrderForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Track Your Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        List<String> trackingIds = orderService.getUserTrackingIds(currentUser.getUsername());
        
        if (trackingIds.isEmpty()) {
            JLabel noOrdersLabel = new JLabel("No orders found to track.", JLabel.CENTER);
            noOrdersLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noOrdersLabel.setForeground(Color.GRAY);
            contentPanel.add(noOrdersLabel, BorderLayout.CENTER);
        } else {
            // Instructions
            JLabel instructionLabel = new JLabel("Select a tracking ID to view order details:");
            instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            instructionLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
            contentPanel.add(instructionLabel, BorderLayout.NORTH);
            
            // Tracking ID list
            JPanel listPanel = new JPanel(new GridLayout(0, 1, 0, 10));
            listPanel.setBackground(Color.WHITE);
            
            for (String trackingId : trackingIds) {
                Order order = orderService.getOrder(currentUser.getUsername(), trackingId);
                if (order != null) {
                    JButton orderButton = createOrderButton(order);
                    orderButton.addActionListener(e -> showOrderDetails(order));
                    listPanel.add(orderButton);
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Your Orders"));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton refreshButton = createStyledButton("Refresh", SECONDARY_COLOR, 120, 40);
        JButton closeButton = createStyledButton("Close", DANGER_COLOR, 120, 40);
        
        refreshButton.addActionListener(e -> {
            dispose();
            new TrackOrderController(parentFrame, currentUser).setVisible(true);
        });
        
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JButton createOrderButton(Order order) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(600, 80));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Left panel - Order info
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel trackingLabel = new JLabel("Tracking ID: " + order.getTrackingId());
        trackingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        trackingLabel.setForeground(PRIMARY_COLOR);
        
        JLabel receiverLabel = new JLabel("To: " + order.getReceiverName() + " - " + order.getReceiverCity());
        receiverLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        receiverLabel.setForeground(Color.DARK_GRAY);
        
        leftPanel.add(trackingLabel);
        leftPanel.add(receiverLabel);
        
        // Right panel - Status and amount
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel statusLabel = new JLabel(order.getStatus().getDisplayName());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        // Set status color
        switch (order.getStatus()) {
            case IN_PROCESS:
                statusLabel.setForeground(WARNING_COLOR);
                break;
            case SHIPPED:
                statusLabel.setForeground(SECONDARY_COLOR);
                break;
            case DELIVERED:
                statusLabel.setForeground(SUCCESS_COLOR);
                break;
        }
        
        JLabel amountLabel = new JLabel("Rs. " + order.getRate());
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setForeground(SUCCESS_COLOR);
        amountLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        rightPanel.add(statusLabel);
        rightPanel.add(amountLabel);
        
        button.add(leftPanel, BorderLayout.CENTER);
        button.add(rightPanel, BorderLayout.EAST);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_GRAY);
                leftPanel.setBackground(LIGHT_GRAY);
                rightPanel.setBackground(LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                leftPanel.setBackground(Color.WHITE);
                rightPanel.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }

    private void showOrderDetails(Order order) {
        JDialog detailDialog = new JDialog(this, "Order Details", true);
        detailDialog.setSize(600, 500);
        detailDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Order status with progress indicator
        JPanel statusPanel = createStatusPanel(order.getStatus());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(statusPanel, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Order details
        addDetailRow(contentPanel, gbc, 1, "Tracking ID:", order.getTrackingId());
        addDetailRow(contentPanel, gbc, 2, "Status:", order.getStatus().getDisplayName());
        addDetailRow(contentPanel, gbc, 3, "Amount:", "Rs. " + order.getRate());
        
        // Separator
        JSeparator separator1 = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(separator1, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Sender details
        JLabel senderLabel = new JLabel("Sender Information:");
        senderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        senderLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        contentPanel.add(senderLabel, gbc);
        gbc.gridwidth = 1;
        
        addDetailRow(contentPanel, gbc, 6, "Name:", order.getSenderName());
        addDetailRow(contentPanel, gbc, 7, "Contact:", order.getSenderContact());
        addDetailRow(contentPanel, gbc, 8, "City:", order.getSenderCity());
        
        // Separator
        JSeparator separator2 = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(separator2, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Receiver details
        JLabel receiverLabel = new JLabel("Receiver Information:");
        receiverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        receiverLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        contentPanel.add(receiverLabel, gbc);
        gbc.gridwidth = 1;
        
        addDetailRow(contentPanel, gbc, 11, "Name:", order.getReceiverName());
        addDetailRow(contentPanel, gbc, 12, "Contact:", order.getReceiverContact());
        addDetailRow(contentPanel, gbc, 13, "Address:", order.getReceiverAddress());
        addDetailRow(contentPanel, gbc, 14, "City:", order.getReceiverCity());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        JButton closeButton = createStyledButton("Close", SECONDARY_COLOR, 100, 35);
        closeButton.addActionListener(e -> detailDialog.dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }

    private JPanel createStatusPanel(Order.OrderStatus status) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        // Status indicators
        JLabel processLabel = new JLabel("In Process");
        JLabel shippedLabel = new JLabel("Shipped");
        JLabel deliveredLabel = new JLabel("Delivered");
        
        processLabel.setFont(new Font("Arial", Font.BOLD, 12));
        shippedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        deliveredLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set colors based on current status
        switch (status) {
            case IN_PROCESS:
                processLabel.setForeground(WARNING_COLOR);
                shippedLabel.setForeground(Color.LIGHT_GRAY);
                deliveredLabel.setForeground(Color.LIGHT_GRAY);
                break;
            case SHIPPED:
                processLabel.setForeground(SUCCESS_COLOR);
                shippedLabel.setForeground(SECONDARY_COLOR);
                deliveredLabel.setForeground(Color.LIGHT_GRAY);
                break;
            case DELIVERED:
                processLabel.setForeground(SUCCESS_COLOR);
                shippedLabel.setForeground(SUCCESS_COLOR);
                deliveredLabel.setForeground(SUCCESS_COLOR);
                break;
        }
        
        panel.add(processLabel);
        panel.add(new JLabel(" → "));
        panel.add(shippedLabel);
        panel.add(new JLabel(" → "));
        panel.add(deliveredLabel);
        
        return panel;
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 14));
        labelComp.setPreferredSize(new Dimension(120, 25));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(labelComp, gbc);
        gbc.gridx = 1;
        panel.add(valueComp, gbc);
    }

    private JButton createStyledButton(String text, Color backgroundColor, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 12));
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
}
