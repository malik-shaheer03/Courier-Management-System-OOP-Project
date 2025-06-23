package com.courier.service;

import com.courier.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    
    public boolean createUser(User user) {
        try {
            String filePath = FileManager.getAccountsDir() + "/" + user.getUsername() + ".txt";
            if (FileManager.fileExists(filePath)) {
                return false; // User already exists
            }
            FileManager.writeFile(filePath, user.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String pin) {
        try {
            String filePath = FileManager.getAccountsDir() + "/" + username + ".txt";
            if (!FileManager.fileExists(filePath)) {
                return null;
            }
            String data = FileManager.readFile(filePath);
            User user = User.fromString(data);
            if (user != null && user.getPin().equals(pin)) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        try {
            String filePath = FileManager.getAccountsDir() + "/" + user.getUsername() + ".txt";
            FileManager.writeFile(filePath, user.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String filePath = FileManager.getAccountsDir() + "/" + username + ".txt";
        return FileManager.deleteFile(filePath);
    }

    public User getUserByUsername(String username) {
        try {
            String filePath = FileManager.getAccountsDir() + "/" + username + ".txt";
            if (!FileManager.fileExists(filePath)) {
                return null;
            }
            String data = FileManager.readFile(filePath);
            return User.fromString(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        List<String> files = FileManager.listFiles(FileManager.getAccountsDir());
        for (String file : files) {
            if (file.endsWith(".txt")) {
                usernames.add(file.replace(".txt", ""));
            }
        }
        return usernames;
    }
}
