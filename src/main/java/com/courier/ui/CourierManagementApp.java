package com.courier.ui;

import com.courier.model.Admin;
import com.courier.model.Order;
import com.courier.model.User;
import com.courier.service.AdminService;
import com.courier.service.OrderService;
import com.courier.service.UserService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CourierManagementApp extends JFrame {
    private final AdminService adminService;
    private final UserService userService;
    private final OrderService orderService;
    private User currentUser;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public CourierManagementApp() {
        adminService = new AdminService();
        userService = new UserService();
        orderService = new OrderService();
        
        initializeUI();
        showMainMenu();
    }

    private void initializeUI() {
        setTitle("Courier Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set look and feel - simplified version
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system one fails
            System.err.println("Using default look and feel: " + e.getMessage());
        }
    }

    private void showMainMenu() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Courier Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Professional Package Delivery Service", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(LIGHT_GRAY);
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(subtitleLabel);
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton adminButton = createStyledButton("Admin Login", DANGER_COLOR, 280, 60);
        JButton customerButton = createStyledButton("Customer Portal", SECONDARY_COLOR, 280, 60);
        JButton exitButton = createStyledButton("Exit Application", new Color(149, 165, 166), 280, 60);
        
        adminButton.addActionListener(e -> showAdminLogin());
        customerButton.addActionListener(e -> showCustomerOptions());
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        gbc.gridy = 0;
        centerPanel.add(adminButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(customerButton, gbc);
        gbc.gridy = 2;
        centerPanel.add(exitButton, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private JButton createStyledButton(String text, Color backgroundColor, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void showAdminLogin() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Admin Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(40, 60, 40, 60),
            BorderFactory.createRaisedBevelBorder()
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField usernameField = new JTextField(25);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(300, 35));
        
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField pinField = new JPasswordField(25);
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));
        pinField.setPreferredSize(new Dimension(300, 35));
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(pinLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(pinField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JButton loginButton = createStyledButton("Login", SUCCESS_COLOR, 120, 45);
        JButton backButton = createStyledButton("Back", DANGER_COLOR, 120, 45);
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            
            if (username.isEmpty() || pin.isEmpty()) {
                showErrorMessage("Please fill in all fields.");
                return;
            }
            
            Admin admin = adminService.authenticateAdmin(username, pin);
            if (admin != null) {
                showAdminDashboard();
            } else {
                showErrorMessage("Invalid username or PIN.");
            }
        });
        
        backButton.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(backButton);
        
        // Center the form
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(PRIMARY_COLOR);
        centerWrapper.add(formPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void showAdminDashboard() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Manage Users, Orders & System Settings", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(LIGHT_GRAY);
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);
        
        // Dashboard cards
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        cardsPanel.setBackground(PRIMARY_COLOR);
        cardsPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        JButton usersCard = createDashboardCard("User Management", "Manage customer accounts", SECONDARY_COLOR);
        JButton ordersCard = createDashboardCard("Order Management", "Track and manage orders", WARNING_COLOR);
        JButton financeCard = createDashboardCard("Finance Reports", "View revenue and statistics", SUCCESS_COLOR);
        JButton settingsCard = createDashboardCard("Settings", "Change admin password", new Color(155, 89, 182));
        
        usersCard.addActionListener(e -> showUserManagement());
        ordersCard.addActionListener(e -> showOrderManagement());
        financeCard.addActionListener(e -> showFinanceReports());
        settingsCard.addActionListener(e -> showAdminSettings());
        
        cardsPanel.add(usersCard);
        cardsPanel.add(ordersCard);
        cardsPanel.add(financeCard);
        cardsPanel.add(settingsCard);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(PRIMARY_COLOR);
        footerPanel.setBorder(new EmptyBorder(15, 0, 25, 0));
        
        JButton backButton = createStyledButton("Back to Main Menu", DANGER_COLOR, 180, 45);
        backButton.addActionListener(e -> showMainMenu());
        footerPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private JButton createDashboardCard(String title, String description, Color backgroundColor) {
        JButton card = new JButton();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(220, 140));
        card.setBackground(backgroundColor);
        card.setBorderPainted(false);
        card.setFocusPainted(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(LIGHT_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(descLabel);
        card.add(Box.createVerticalGlue());
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(backgroundColor.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(backgroundColor);
            }
        });
        
        return card;
    }

    private void showUserManagement() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("User Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // User list
        List<String> usernames = userService.getAllUsernames();
        String[] columnNames = {"Username", "Full Name", "City", "Contact"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        for (String username : usernames) {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                tableModel.addRow(new Object[]{
                    user.getUsername(),
                    user.getFullName(),
                    user.getCity(),
                    user.getContactNumber()
                });
            }
        }
        
        JTable userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(SECONDARY_COLOR);
        userTable.getTableHeader().setForeground(Color.WHITE);

        // Improve table appearance
        userTable.setSelectionBackground(SECONDARY_COLOR);
        userTable.setSelectionForeground(Color.WHITE);
        userTable.setGridColor(LIGHT_GRAY);
        userTable.setShowGrid(true);
        userTable.setIntercellSpacing(new Dimension(1, 1));

        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        
        JButton viewButton = createStyledButton("View Details", SECONDARY_COLOR, 130, 40);
        JButton deleteButton = createStyledButton("Delete User", DANGER_COLOR, 130, 40);
        JButton backButton = createStyledButton("Back", new Color(149, 165, 166), 130, 40);
        
        viewButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                String username = (String) tableModel.getValueAt(selectedRow, 0);
                showUserDetails(username);
            } else {
                showErrorMessage("Please select a user to view details.");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                String username = (String) tableModel.getValueAt(selectedRow, 0);
                int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user: " + username + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    if (userService.deleteUser(username)) {
                        showSuccessMessage("User deleted successfully!");
                        showUserManagement(); // Refresh the view
                    } else {
                        showErrorMessage("Failed to delete user.");
                    }
                }
            } else {
                showErrorMessage("Please select a user to delete.");
            }
        });
        
        backButton.addActionListener(e -> showAdminDashboard());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void showUserDetails(String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            showErrorMessage("User not found.");
            return;
        }
        
        StringBuilder details = new StringBuilder();
        details.append("User Details\n");
        details.append("=".repeat(30)).append("\n\n");
        details.append("Full Name: ").append(user.getFullName()).append("\n");
        details.append("Username: ").append(user.getUsername()).append("\n");
        details.append("Contact: ").append(user.getContactNumber()).append("\n");
        details.append("Address: ").append(user.getAddress()).append("\n");
        details.append("City: ").append(user.getCity()).append("\n");
        details.append("Province: ").append(user.getProvince()).append("\n");
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        
        JOptionPane.showMessageDialog(this, scrollPane, "User Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showOrderManagement() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Order Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Order list
        String[] columnNames = {"Tracking ID", "Customer", "Receiver", "City", "Status", "Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        List<String> usernames = userService.getAllUsernames();
        for (String username : usernames) {
            List<Order> orders = orderService.getUserOrders(username);
            for (Order order : orders) {
                tableModel.addRow(new Object[]{
                    order.getTrackingId(),
                    username,
                    order.getReceiverName(),
                    order.getReceiverCity(),
                    order.getStatus().getDisplayName(),
                    "Rs. " + order.getRate()
                });
            }
        }
        
        JTable orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        orderTable.getTableHeader().setBackground(WARNING_COLOR);
        orderTable.getTableHeader().setForeground(Color.WHITE);

        // Improve table appearance
        orderTable.setSelectionBackground(WARNING_COLOR);
        orderTable.setSelectionForeground(Color.WHITE);
        orderTable.setGridColor(LIGHT_GRAY);
        orderTable.setShowGrid(true);
        orderTable.setIntercellSpacing(new Dimension(1, 1));

        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < orderTable.getColumnCount(); i++) {
            orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        
        JButton updateStatusButton = createStyledButton("Update Status", SUCCESS_COLOR, 140, 40);
        JButton cancelOrderButton = createStyledButton("Cancel Order", DANGER_COLOR, 140, 40);
        JButton backButton = createStyledButton("Back", new Color(149, 165, 166), 140, 40);
        
        updateStatusButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow >= 0) {
                String trackingId = (String) tableModel.getValueAt(selectedRow, 0);
                String username = (String) tableModel.getValueAt(selectedRow, 1);
                updateOrderStatus(username, trackingId);
            } else {
                showErrorMessage("Please select an order to update status.");
            }
        });
        
        cancelOrderButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow >= 0) {
                String trackingId = (String) tableModel.getValueAt(selectedRow, 0);
                String username = (String) tableModel.getValueAt(selectedRow, 1);
                
                int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel order: " + trackingId + "?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    if (orderService.cancelOrder(username, trackingId)) {
                        showSuccessMessage("Order cancelled successfully!");
                        showOrderManagement(); // Refresh the view
                    } else {
                        showErrorMessage("Failed to cancel order.");
                    }
                }
            } else {
                showErrorMessage("Please select an order to cancel.");
            }
        });
        
        backButton.addActionListener(e -> showAdminDashboard());
        
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelOrderButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void updateOrderStatus(String username, String trackingId) {
        Order order = orderService.getOrder(username, trackingId);
        if (order == null) {
            showErrorMessage("Order not found.");
            return;
        }
        
        Order.OrderStatus currentStatus = order.getStatus();
        Order.OrderStatus newStatus;
        
        switch (currentStatus) {
            case IN_PROCESS:
                newStatus = Order.OrderStatus.SHIPPED;
                break;
            case SHIPPED:
                newStatus = Order.OrderStatus.DELIVERED;
                break;
            default:
                showErrorMessage("Order is already delivered.");
                return;
        }
        
        int result = JOptionPane.showConfirmDialog(this,
            "Update order status from '" + currentStatus.getDisplayName() + 
            "' to '" + newStatus.getDisplayName() + "'?",
            "Confirm Status Update",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (orderService.updateOrderStatus(username, trackingId, newStatus)) {
                showSuccessMessage("Order status updated successfully!");
                showOrderManagement(); // Refresh the view
            } else {
                showErrorMessage("Failed to update order status.");
            }
        }
    }

    private void showFinanceReports() {
        StringBuilder report = new StringBuilder();
        report.append("Finance Report\n");
        report.append("=".repeat(50)).append("\n\n");
        
        List<AdminService.FinanceRecord> records = adminService.getFinanceRecords();
        double total = 0;
        
        if (records.isEmpty()) {
            report.append("No financial records found.\n");
        } else {
            report.append("Order ID        Amount\n");
            report.append("-".repeat(30)).append("\n");
            
            for (AdminService.FinanceRecord record : records) {
                report.append(String.format("%-15s Rs. %.2f\n", 
                    record.getTrackingId(), record.getRate()));
                total += record.getRate();
            }
            
            report.append("-".repeat(30)).append("\n");
            report.append(String.format("Total Revenue:  Rs. %.2f\n", total));
            report.append(String.format("Total Orders:   %d\n", records.size()));
            report.append(String.format("Average Order:  Rs. %.2f\n", 
                !records.isEmpty() ? total / records.size() : 0));
        }
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Finance Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAdminSettings() {
        String oldPin = JOptionPane.showInputDialog(this, "Enter current PIN:", "Change Admin Password", JOptionPane.QUESTION_MESSAGE);
        if (oldPin == null || oldPin.trim().isEmpty()) {
            return;
        }
        
        String newPin = JOptionPane.showInputDialog(this, "Enter new PIN:", "Change Admin Password", JOptionPane.QUESTION_MESSAGE);
        if (newPin == null || newPin.trim().isEmpty()) {
            return;
        }
        
        String confirmPin = JOptionPane.showInputDialog(this, "Confirm new PIN:", "Change Admin Password", JOptionPane.QUESTION_MESSAGE);
        if (confirmPin == null || !newPin.equals(confirmPin)) {
            showErrorMessage("PIN confirmation does not match.");
            return;
        }
        
        if (adminService.changeAdminPassword(oldPin.trim(), newPin.trim())) {
            showSuccessMessage("Admin password changed successfully!");
        } else {
            showErrorMessage("Failed to change password. Please check your current PIN.");
        }
    }

    private void showCustomerOptions() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Customer Portal", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Create Account or Login to Continue", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(LIGHT_GRAY);
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(subtitleLabel);
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton registerButton = createStyledButton("Create New Account", SUCCESS_COLOR, 300, 60);
        JButton loginButton = createStyledButton("Login to Existing Account", SECONDARY_COLOR, 300, 60);
        JButton backButton = createStyledButton("Back to Main Menu", DANGER_COLOR, 300, 60);
        
        registerButton.addActionListener(e -> showCustomerRegistration());
        loginButton.addActionListener(e -> showCustomerLogin());
        backButton.addActionListener(e -> showMainMenu());
        
        gbc.gridy = 0;
        centerPanel.add(registerButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(loginButton, gbc);
        gbc.gridy = 2;
        centerPanel.add(backButton, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void showCustomerRegistration() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Form panel with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField fullNameField = createFormField(25);
        JTextField usernameField = createFormField(25);
        JTextField contactField = createFormField(25);
        JTextField addressField = createFormField(25);
        JTextField cityField = createFormField(25);
        JTextField provinceField = createFormField(25);
        JPasswordField pinField = createPasswordField(25);
        JPasswordField confirmPinField = createPasswordField(25);
        
        // Add fields to form
        addFormField(formPanel, gbc, 0, "Full Name:", fullNameField);
        addFormField(formPanel, gbc, 1, "Username:", usernameField);
        addFormField(formPanel, gbc, 2, "Contact:", contactField);
        addFormField(formPanel, gbc, 3, "Address:", addressField);
        addFormField(formPanel, gbc, 4, "City:", cityField);
        addFormField(formPanel, gbc, 5, "Province:", provinceField);
        addFormField(formPanel, gbc, 6, "PIN:", pinField);
        addFormField(formPanel, gbc, 7, "Confirm PIN:", confirmPinField);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton createButton = createStyledButton("Create Account", SUCCESS_COLOR, 150, 45);
        JButton backButton = createStyledButton("Back", DANGER_COLOR, 150, 45);
        
        createButton.addActionListener(e -> {
            if (validateRegistrationForm(fullNameField, usernameField, contactField, addressField,
                    cityField, provinceField, pinField, confirmPinField)) {
                
                User user = new User(
                    fullNameField.getText().trim(),
                    usernameField.getText().trim(),
                    contactField.getText().trim(),
                    addressField.getText().trim(),
                    cityField.getText().trim(),
                    provinceField.getText().trim(),
                    new String(pinField.getPassword()).trim()
                );
                
                if (userService.createUser(user)) {
                    showSuccessMessage("Account created successfully!");
                    showCustomerOptions();
                } else {
                    showErrorMessage("Username already exists. Please choose a different username.");
                }
            }
        });
        
        backButton.addActionListener(e -> showCustomerOptions());
        
        buttonPanel.add(createButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private JTextField createFormField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 30));
        return field;
    }

    private JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 30));
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(120, 30));
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private boolean validateRegistrationForm(JTextField fullNameField, JTextField usernameField,
                                           JTextField contactField, JTextField addressField,
                                           JTextField cityField, JTextField provinceField,
                                           JPasswordField pinField, JPasswordField confirmPinField) {
        
        if (fullNameField.getText().trim().isEmpty() ||
            usernameField.getText().trim().isEmpty() ||
            contactField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            cityField.getText().trim().isEmpty() ||
            provinceField.getText().trim().isEmpty()) {
            showErrorMessage("Please fill in all fields.");
            return false;
        }
        
        String pin = new String(pinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());
        
        if (pin.isEmpty() || confirmPin.isEmpty()) {
            showErrorMessage("Please fill in all fields.");
            return false;
        }
        
        if (!pin.equals(confirmPin)) {
            showErrorMessage("PIN and Confirm PIN do not match.");
            return false;
        }
        
        return true;
    }

    private void showCustomerLogin() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Customer Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(40, 60, 40, 60),
            BorderFactory.createRaisedBevelBorder()
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField usernameField = new JTextField(25);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(300, 35));
        
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField pinField = new JPasswordField(25);
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));
        pinField.setPreferredSize(new Dimension(300, 35));
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(pinLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(pinField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JButton loginButton = createStyledButton("Login", SUCCESS_COLOR, 120, 45);
        JButton backButton = createStyledButton("Back", DANGER_COLOR, 120, 45);
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            
            if (username.isEmpty() || pin.isEmpty()) {
                showErrorMessage("Please fill in all fields.");
                return;
            }
            
            User user = userService.authenticateUser(username, pin);
            if (user != null) {
                currentUser = user;
                showCustomerDashboard();
            } else {
                showErrorMessage("Invalid username or PIN.");
            }
        });
        
        backButton.addActionListener(e -> showCustomerOptions());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(backButton);
        
        // Center the form
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(PRIMARY_COLOR);
        centerWrapper.add(formPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void showCustomerDashboard() {
        getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Customer Dashboard - Manage Your Shipments", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(LIGHT_GRAY);
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);
        
        // Dashboard cards
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        cardsPanel.setBackground(PRIMARY_COLOR);
        cardsPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        JButton placeOrderCard = createDashboardCard("Place New Order", "Ship packages worldwide", DANGER_COLOR);
        JButton trackOrderCard = createDashboardCard("Track Orders", "Monitor your shipments", WARNING_COLOR);
        JButton orderHistoryCard = createDashboardCard("Order History", "View all your orders", SECONDARY_COLOR);
        JButton profileCard = createDashboardCard("Update Profile", "Manage account details", new Color(155, 89, 182));
        
        placeOrderCard.addActionListener(e -> showPlaceOrder());
        trackOrderCard.addActionListener(e -> showTrackOrder());
        orderHistoryCard.addActionListener(e -> showOrderHistory());
        profileCard.addActionListener(e -> showUpdateProfile());
        
        cardsPanel.add(placeOrderCard);
        cardsPanel.add(trackOrderCard);
        cardsPanel.add(orderHistoryCard);
        cardsPanel.add(profileCard);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(PRIMARY_COLOR);
        footerPanel.setBorder(new EmptyBorder(15, 0, 25, 0));
        
        JButton logoutButton = createStyledButton("Logout", DANGER_COLOR, 120, 45);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showMainMenu();
        });
        footerPanel.add(logoutButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        revalidate();
        repaint();
    }

    private void showPlaceOrder() {
        PlaceOrderController placeOrderController = new PlaceOrderController(this, currentUser);
        placeOrderController.setVisible(true);
    }

    private void showTrackOrder() {
        TrackOrderController trackOrderController = new TrackOrderController(this, currentUser);
        trackOrderController.setVisible(true);
    }

    private void showOrderHistory() {
        OrderHistoryController orderHistoryController = new OrderHistoryController(this, currentUser);
        orderHistoryController.setVisible(true);
    }

    private void showUpdateProfile() {
        UpdateProfileController updateProfileController = new UpdateProfileController(this, currentUser, userService);
        updateProfileController.setVisible(true);
    }

    // Utility methods for showing messages
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                // If cross-platform look and feel fails, use default
                System.err.println("Could not set look and feel: " + e.getMessage());
            }
            new CourierManagementApp().setVisible(true);
        });
    }
}
