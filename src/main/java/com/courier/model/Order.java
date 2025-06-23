package com.courier.model;

public class Order {
    private String trackingId;
    private String senderName;
    private String senderContact;
    private String senderCity;
    private String receiverName;
    private String receiverContact;
    private String receiverAddress;
    private String receiverCity;
    private double rate;
    private OrderStatus status;
    private String username;

    public enum OrderStatus {
        IN_PROCESS("In process"),
        SHIPPED("Shipped"),
        DELIVERED("Delivered");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static OrderStatus fromString(String status) {
            for (OrderStatus s : values()) {
                if (s.displayName.equals(status)) {
                    return s;
                }
            }
            return IN_PROCESS;
        }
    }

    public Order() {}

    public Order(String trackingId, String senderName, String senderContact, String senderCity,
                 String receiverName, String receiverContact, String receiverAddress, 
                 String receiverCity, double rate, OrderStatus status, String username) {
        this.trackingId = trackingId;
        this.senderName = senderName;
        this.senderContact = senderContact;
        this.senderCity = senderCity;
        this.receiverName = receiverName;
        this.receiverContact = receiverContact;
        this.receiverAddress = receiverAddress;
        this.receiverCity = receiverCity;
        this.rate = rate;
        this.status = status;
        this.username = username;
    }

    // Getters and Setters
    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderContact() { return senderContact; }
    public void setSenderContact(String senderContact) { this.senderContact = senderContact; }

    public String getSenderCity() { return senderCity; }
    public void setSenderCity(String senderCity) { this.senderCity = senderCity; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getReceiverContact() { return receiverContact; }
    public void setReceiverContact(String receiverContact) { this.receiverContact = receiverContact; }

    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }

    public String getReceiverCity() { return receiverCity; }
    public void setReceiverCity(String receiverCity) { this.receiverCity = receiverCity; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return trackingId + "," + senderName + "," + senderContact + "," + senderCity + "," +
               receiverName + "," + receiverContact + "," + receiverAddress + "," + receiverCity + "," +
               rate + "," + status.getDisplayName();
    }

    public static Order fromString(String data, String username) {
        String[] parts = data.split(",");
        if (parts.length >= 10) {
            return new Order(parts[0], parts[1], parts[2], parts[3], parts[4], 
                           parts[5], parts[6], parts[7], Double.parseDouble(parts[8]), 
                           OrderStatus.fromString(parts[9]), username);
        }
        return null;
    }
}
