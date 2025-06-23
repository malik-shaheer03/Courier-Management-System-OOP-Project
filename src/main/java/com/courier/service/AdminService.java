package com.courier.service;

import com.courier.model.Admin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    
    public Admin authenticateAdmin(String username, String pin) {
        try {
            String filePath = FileManager.getAdminDir() + "/admin.txt";
            if (!FileManager.fileExists(filePath)) {
                return null;
            }
            String data = FileManager.readFile(filePath);
            Admin admin = Admin.fromString(data);
            if (admin != null && admin.getUsername().equals(username) && admin.getPin().equals(pin)) {
                return admin;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeAdminPassword(String oldPin, String newPin) {
        try {
            String filePath = FileManager.getAdminDir() + "/admin.txt";
            String data = FileManager.readFile(filePath);
            Admin admin = Admin.fromString(data);
            
            if (admin != null && admin.getPin().equals(oldPin)) {
                admin.setPin(newPin);
                FileManager.writeFile(filePath, admin.toString());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FinanceRecord> getFinanceRecords() {
        List<FinanceRecord> records = new ArrayList<>();
        try {
            String financeFile = FileManager.getFinanceDir() + "/finance.txt";
            if (!FileManager.fileExists(financeFile)) {
                return records;
            }
            
            String data = FileManager.readFile(financeFile);
            if (data.trim().isEmpty()) {
                return records;
            }
            
            String[] parts = data.split(",");
            for (int i = 0; i < parts.length - 1; i += 2) {
                if (i + 1 < parts.length) {
                    records.add(new FinanceRecord(parts[i], Double.parseDouble(parts[i + 1])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return records;
    }

    public double getTotalRevenue() {
        return getFinanceRecords().stream().mapToDouble(FinanceRecord::getRate).sum();
    }

    public static class FinanceRecord {
        private String trackingId;
        private double rate;

        public FinanceRecord(String trackingId, double rate) {
            this.trackingId = trackingId;
            this.rate = rate;
        }

        public String getTrackingId() { return trackingId; }
        public double getRate() { return rate; }
    }
}
