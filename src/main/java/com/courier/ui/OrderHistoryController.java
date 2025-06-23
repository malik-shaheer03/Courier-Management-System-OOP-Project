package com.courier.ui;

import com.courier.model.User;
import com.courier.model.Order;
import com.courier.service.OrderService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class OrderHistoryController extends JFrame {
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

    public OrderHistoryController(JFrame parentFrame, User currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        this.orderService = new OrderService();
        
        initializeUI();
        setupOrderHistoryView();
    }

    private void initializeUI() {
        setTitle("Order History - Courier Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
    }

    private void setupOrderHistoryView() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Order History - " + currentUser.getFullName(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        List<Order> orders = orderService.getUserOrders(currentUser.getUsername());
        
        if (orders.isEmpty()) {
            JLabel noOrdersLabel = new JLabel("No orders found.", JLabel.CENTER);
            noOrdersLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noOrdersLabel.setForeground(Color.GRAY);
            contentPanel.add(noOrdersLabel, BorderLayout.CENTER);
        } else {
            // Create table
            String[] columnNames = {"Tracking ID", "Receiver", "City", "Status", "Amount", "Actions"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // Only Actions column is editable
                }
            };
            
            // Populate table
            for (Order order : orders) {
                tableModel.addRow(new Object[]{
                    order.getTrackingId(),
                    order.getReceiverName(),
                    order.getReceiverCity(),
                    order.getStatus().getDisplayName(),
                    "Rs. " + order.getRate(),
                    "View Details"
                });
            }
            
            JTable orderTable = new JTable(tableModel);
            orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
            orderTable.setRowHeight(30);
            orderTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            orderTable.getTableHeader().setBackground(SECONDARY_COLOR);
            orderTable.getTableHeader().setForeground(Color.WHITE);
            orderTable.setSelectionBackground(SECONDARY_COLOR);
            orderTable.setSelectionForeground(Color.WHITE);
            orderTable.setGridColor(LIGHT_GRAY);
            orderTable.setShowGrid(true);
            orderTable.setIntercellSpacing(new Dimension(1, 1));
            
            // Center align table cells
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < orderTable.getColumnCount() - 1; i++) {
                orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            
            // Custom renderer for status column
            orderTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(JLabel.CENTER);
                    
                    if (!isSelected) {
                        String status = (String) value;
                        if ("In process".equals(status)) {
                            setForeground(WARNING_COLOR);
                        } else if ("Shipped".equals(status)) {
                            setForeground(SECONDARY_COLOR);
                        } else if ("Delivered".equals(status)) {
                            setForeground(SUCCESS_COLOR);
                        }
                    }
                    return c;
                }
            });
            
            // Add mouse listener for row clicks
            orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = orderTable.rowAtPoint(evt.getPoint());
                    int col = orderTable.columnAtPoint(evt.getPoint());
                    
                    if (row >= 0) {
                        String trackingId = (String) tableModel.getValueAt(row, 0);
                        Order order = orderService.getOrder(currentUser.getUsername(), trackingId);
                        
                        if (col == 5 || evt.getClickCount() == 2) { // Actions column or double click
                            showOrderDetails(order);
                        }
                    }
                }
            });
            
            JScrollPane scrollPane = new JScrollPane(orderTable);
            scrollPane.setPreferredSize(new Dimension(850, 400));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Summary panel
            JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            summaryPanel.setBackground(Color.WHITE);
            summaryPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            double totalAmount = orders.stream().mapToDouble(Order::getRate).sum();
            JLabel summaryLabel = new JLabel("Total Orders: " + orders.size() + " | Total Amount: Rs. " + totalAmount);
            summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            summaryLabel.setForeground(PRIMARY_COLOR);
            summaryPanel.add(summaryLabel);
            
            contentPanel.add(summaryPanel, BorderLayout.SOUTH);
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        
        JButton refreshButton = createStyledButton("Refresh", SECONDARY_COLOR, 120, 40);
        JButton exportButton = createStyledButton("Export", SUCCESS_COLOR, 120, 40);
        JButton closeButton = createStyledButton("Close", DANGER_COLOR, 120, 40);
        
        refreshButton.addActionListener(e -> {
            dispose();
            new OrderHistoryController(parentFrame, currentUser).setVisible(true);
        });
        
        exportButton.addActionListener(e -> exportOrderHistory(orders));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(exportButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void showOrderDetails(Order order) {
        if (order == null) return;
        
        JDialog detailDialog = new JDialog(this, "Order Details", true);
        detailDialog.setSize(600, 500);
        detailDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order #" + order.getTrackingId(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Content
        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        StringBuilder details = new StringBuilder();
        details.append("ORDER DETAILS\n");
        details.append("=".repeat(50)).append("\n\n");
        details.append("Tracking ID: ").append(order.getTrackingId()).append("\n");
        details.append("Status: ").append(order.getStatus().getDisplayName()).append("\n");
        details.append("Amount: Rs. ").append(order.getRate()).append("\n\n");
        
        details.append("SENDER INFORMATION\n");
        details.append("-".repeat(30)).append("\n");
        details.append("Name: ").append(order.getSenderName()).append("\n");
        details.append("Contact: ").append(order.getSenderContact()).append("\n");
        details.append("City: ").append(order.getSenderCity()).append("\n\n");
        
        details.append("RECEIVER INFORMATION\n");
        details.append("-".repeat(30)).append("\n");
        details.append("Name: ").append(order.getReceiverName()).append("\n");
        details.append("Contact: ").append(order.getReceiverContact()).append("\n");
        details.append("Address: ").append(order.getReceiverAddress()).append("\n");
        details.append("City: ").append(order.getReceiverCity()).append("\n");
        
        detailArea.setText(details.toString());
        
        JScrollPane scrollPane = new JScrollPane(detailArea);
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

    private void exportOrderHistory(List<Order> orders) {
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders to export.", "Export", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder export = new StringBuilder();
        export.append("ORDER HISTORY REPORT\n");
        export.append("Customer: ").append(currentUser.getFullName()).append("\n");
        export.append("Generated: ").append(new java.util.Date()).append("\n");
        export.append("=".repeat(80)).append("\n\n");
        
        double totalAmount = 0;
        for (Order order : orders) {
            export.append("Tracking ID: ").append(order.getTrackingId()).append("\n");
            export.append("Receiver: ").append(order.getReceiverName()).append("\n");
            export.append("City: ").append(order.getReceiverCity()).append("\n");
            export.append("Status: ").append(order.getStatus().getDisplayName()).append("\n");
            export.append("Amount: Rs. ").append(order.getRate()).append("\n");
            export.append("-".repeat(50)).append("\n");
            totalAmount += order.getRate();
        }
        
        export.append("\nSUMMARY\n");
        export.append("Total Orders: ").append(orders.size()).append("\n");
        export.append("Total Amount: Rs. ").append(totalAmount).append("\n");
        
        JTextArea exportArea = new JTextArea(export.toString());
        exportArea.setEditable(false);
        exportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(exportArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Order History Export", JOptionPane.INFORMATION_MESSAGE);
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
