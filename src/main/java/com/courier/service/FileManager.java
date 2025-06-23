package com.courier.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String BASE_DIR = System.getProperty("user.home") + File.separator + "CourierMS";
    private static final String ACCOUNTS_DIR = BASE_DIR + File.separator + "accounts";
    private static final String ORDERS_DIR = BASE_DIR + File.separator + "orders";
    private static final String ADMIN_DIR = BASE_DIR + File.separator + "admin";
    private static final String FINANCE_DIR = BASE_DIR + File.separator + "finance";

    static {
        createDirectories();
    }

    private static void createDirectories() {
        try {
            Files.createDirectories(Paths.get(ACCOUNTS_DIR));
            Files.createDirectories(Paths.get(ORDERS_DIR));
            Files.createDirectories(Paths.get(ADMIN_DIR));
            Files.createDirectories(Paths.get(FINANCE_DIR));
            
            // Create default admin file if it doesn't exist
            Path adminFile = Paths.get(ADMIN_DIR, "admin.txt");
            if (!Files.exists(adminFile)) {
                Files.write(adminFile, "admin,admin123".getBytes());
            }
            
            // Create default finance file if it doesn't exist
            Path financeFile = Paths.get(FINANCE_DIR, "finance.txt");
            if (!Files.exists(financeFile)) {
                Files.write(financeFile, "".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    public static List<String> listFiles(String directory) {
        List<String> files = new ArrayList<>();
        try {
            Files.list(Paths.get(directory))
                 .filter(Files::isRegularFile)
                 .forEach(path -> files.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static String getAccountsDir() { return ACCOUNTS_DIR; }
    public static String getOrdersDir() { return ORDERS_DIR; }
    public static String getAdminDir() { return ADMIN_DIR; }
    public static String getFinanceDir() { return FINANCE_DIR; }
}
