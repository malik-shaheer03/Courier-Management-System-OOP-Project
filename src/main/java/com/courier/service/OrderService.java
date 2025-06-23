package com.courier.service;

import com.courier.model.Order;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderService {
    private Random random = new Random();

    public String placeOrder(Order order) {
        try {
            String trackingId = generateTrackingId();
            order.setTrackingId(trackingId);
            
            String userOrderDir = FileManager.getOrdersDir() + "/" + order.getUsername();
            Files.createDirectories(Paths.get(userOrderDir));
            
            String filePath = userOrderDir + "/" + trackingId + ".txt";
            FileManager.writeFile(filePath, order.toString());
            
            // Update finance
            updateFinance(trackingId, order.getRate());
            
            return trackingId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Order getOrder(String username, String trackingId) {
        try {
            String filePath = FileManager.getOrdersDir() + "/" + username + "/" + trackingId + ".txt";
            if (!FileManager.fileExists(filePath)) {
                return null;
            }
            String data = FileManager.readFile(filePath);
            return Order.fromString(data, username);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateOrderStatus(String username, String trackingId, Order.OrderStatus newStatus) {
        try {
            Order order = getOrder(username, trackingId);
            if (order == null) {
                return false;
            }
            order.setStatus(newStatus);
            String filePath = FileManager.getOrdersDir() + "/" + username + "/" + trackingId + ".txt";
            FileManager.writeFile(filePath, order.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelOrder(String username, String trackingId) {
        String filePath = FileManager.getOrdersDir() + "/" + username + "/" + trackingId + ".txt";
        return FileManager.deleteFile(filePath);
    }

    public List<Order> getUserOrders(String username) {
        List<Order> orders = new ArrayList<>();
        String userOrderDir = FileManager.getOrdersDir() + "/" + username;
        List<String> files = FileManager.listFiles(userOrderDir);
        
        for (String file : files) {
            if (file.endsWith(".txt")) {
                String trackingId = file.replace(".txt", "");
                Order order = getOrder(username, trackingId);
                if (order != null) {
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public List<String> getUserTrackingIds(String username) {
        List<String> trackingIds = new ArrayList<>();
        String userOrderDir = FileManager.getOrdersDir() + "/" + username;
        List<String> files = FileManager.listFiles(userOrderDir);
        
        for (String file : files) {
            if (file.endsWith(".txt")) {
                trackingIds.add(file.replace(".txt", ""));
            }
        }
        return trackingIds;
    }

    public double calculateRate(double weight, boolean sameProvince) {
        double rate = 0;
        
        if (sameProvince) {
            if (weight > 0.0 && weight <= 1.0) rate = 300;
            else if (weight > 1.0 && weight <= 3.0) rate = 700;
            else if (weight > 3.0 && weight <= 6.0) rate = 1200;
            else if (weight > 6.0 && weight <= 10.0) rate = 1800;
            else if (weight > 10.0 && weight <= 20.0) rate = 2800;
        } else {
            if (weight > 0.0 && weight <= 1.0) rate = 400;
            else if (weight > 1.0 && weight <= 3.0) rate = 900;
            else if (weight > 3.0 && weight <= 6.0) rate = 1400;
            else if (weight > 6.0 && weight <= 10.0) rate = 2000;
            else if (weight > 10.0 && weight <= 20.0) rate = 3000;
        }
        
        return rate;
    }

    private String generateTrackingId() {
        return String.valueOf(10000 + random.nextInt(90000));
    }

    private void updateFinance(String trackingId, double rate) {
        try {
            String financeFile = FileManager.getFinanceDir() + "/finance.txt";
            String currentData = "";
            if (FileManager.fileExists(financeFile)) {
                currentData = FileManager.readFile(financeFile);
            }
            String newData = currentData + (currentData.isEmpty() ? "" : ",") + trackingId + "," + rate;
            FileManager.writeFile(financeFile, newData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
