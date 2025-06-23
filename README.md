# 📦 Courier Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing_GUI-4285F4?style=for-the-badge&logo=java&logoColor=white)
![File System](https://img.shields.io/badge/File_Storage-FF6B6B?style=for-the-badge&logo=files&logoColor=white)

*A modern, professional courier management solution with sleek GUI* ✨

[Features](#-features) • [Installation](#-installation) • [Usage](#-usage) • [Screenshots](#-screenshots)

</div>

---

## 🚀 About

**Courier Management System** is a comprehensive desktop application that transforms courier service operations with an intuitive graphical interface. Originally built as a console application and completely refactored to modern Java Swing, this system demonstrates enterprise-level software architecture and user experience design.

🎯 **Perfect for:** Courier businesses, logistics companies, educational projects, and Java GUI development learning

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 👑 **Admin Dashboard**
- 🔐 **Secure Authentication** - Protected admin portal
- 👥 **User Management** - View, delete customer accounts
- 📊 **Order Management** - Track, update, cancel orders
- 💰 **Finance Reports** - Revenue tracking & analytics
- 🔄 **Status Control** - Real-time order status updates
- 🔑 **Security Settings** - Change admin credentials
- 📈 **Business Analytics** - Comprehensive reporting

</td>
<td width="50%">

### 🛍️ **Customer Portal**
- 📝 **Account Registration** - Quick signup process
- 🔒 **Secure Login** - Personal account protection
- 📦 **Smart Order Placement** - Intelligent rate calculation
- 🗺️ **Live Order Tracking** - Real-time status monitoring
- 📋 **Order History** - Complete shipment records
- ⚙️ **Profile Management** - Update personal information
- 💳 **Rate Calculator** - Instant pricing estimates

</td>
</tr>
</table>

---

## 🧠 Intelligent Pricing Engine

Our smart pricing system calculates rates based on weight and destination:

| Weight Range | Same Province | Different Province |
|-------------|---------------|-------------------|
| 0.1-1.0 KG  | Rs. 300       | Rs. 400          |
| 1.1-3.0 KG  | Rs. 700       | Rs. 900          |
| 3.1-6.0 KG  | Rs. 1,200     | Rs. 1,400        |
| 6.1-10.0 KG | Rs. 1,800     | Rs. 2,000        |
| 10.1-20.0 KG| Rs. 2,800     | Rs. 3,000        |

---

## 🏗️ Technical Architecture

```
📁 Project Structure
├── 🎨 UI Layer (Java Swing)
│   ├── Main Application Window
│   ├── Admin Dashboard
│   ├── Customer Portal
│   └── Specialized Controllers
├── 🏢 Service Layer
│   ├── User Management Service
│   ├── Order Processing Service
│   ├── Admin Operations Service
│   └── File Management Service
├── 📊 Model Layer
│   ├── User Entity
│   ├── Order Entity
│   └── Admin Entity
└── 💾 Data Storage
    ├── User Accounts (/accounts)
    ├── Order Records (/orders)
    ├── Admin Data (/admin)
    └── Financial Records (/finance)
```

**Technology Stack:**
- ☕ **Java 8+** - Core programming language
- 🎨 **Java Swing** - Modern GUI framework
- 📁 **File I/O** - Persistent data storage
- 🎯 **MVC Pattern** - Clean architecture design
- 🛡️ **Input Validation** - Robust error handling
- 🎨 **Custom Styling** - Professional UI design

---

## 🚀 Installation

### Prerequisites
- ☕ **Java JDK 8 or higher**
- 💻 **Any Java IDE** (IntelliJ IDEA, Eclipse, VS Code)
- 🖥️ **Command line interface**

### Quick Start
```bash
# Clone the repository
git clone https://github.com/yourusername/courier-management-system.git

# Navigate to project directory
cd courier-management-system

# Compile all Java files
javac -d bin src/main/java/com/courier/**/*.java

# Run the application
java -cp bin com.courier.ui.CourierManagementApp
```

### Alternative: IDE Setup
1. Import project into your favorite Java IDE
2. Ensure JDK 8+ is configured
3. Run `CourierManagementApp.java`

---

## 💡 Usage Guide

### 🔐 Admin Access
```
Default Admin Credentials:
Username: admin
PIN: admin123
```

**Admin Capabilities:**
- View and manage all customer accounts
- Monitor all orders across the system
- Update order statuses (In Process → Shipped → Delivered)
- Generate comprehensive financial reports
- Cancel orders when necessary
- Change admin password for security

### 👤 Customer Journey

1. **🏠 Main Menu** → Choose between Admin or Customer portal
2. **📝 Registration** → Create account with personal details
3. **🔑 Login** → Access your personal dashboard
4. **📦 Place Order** → Enter package and receiver details
5. **💰 Calculate Rate** → Get instant pricing based on weight/location
6. **📍 Track Orders** → Monitor shipment status in real-time
7. **📋 View History** → Access complete order records
8. **⚙️ Update Profile** → Modify account information

---

## 🎯 Key Highlights

<div align="center">

| 🏆 **Feature** | 📊 **Benefit** |
|:--------------:|:---------------:|
| **Modern GUI** | Intuitive Swing Interface |
| **No Dependencies** | Pure Java Implementation |
| **Data Persistence** | File-based Storage System |
| **Professional Design** | Custom Color Schemes & Styling |
| **Scalable Architecture** | MVC Design Pattern |
| **Cross-Platform** | Runs on Windows, Mac, Linux |
| **Secure Access** | Protected Admin Functions |
| **Real-time Updates** | Live Order Status Tracking |

</div>

---

## 🛠️ Development

### Project Structure
```
src/main/java/com/courier/
├── model/          # Data models (User, Order, Admin)
├── service/        # Business logic services
├── ui/            # Swing GUI controllers
└── util/          # Utility classes
```

### Key Classes
- `CourierManagementApp` - Main application entry point
- `UserService` - User account management
- `OrderService` - Order processing logic
- `AdminService` - Administrative operations
- `FileManager` - Data persistence handling

---

## 🤝 Contributing

Contributions make the open source community amazing! 🌟

1. **Fork** the Project
2. **Create** your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your Changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the Branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add comments for complex logic
- Test thoroughly before submitting
- Update documentation as needed

---

## 📋 System Requirements

- **Operating System:** Windows 7+, macOS 10.10+, Linux
- **Java Version:** JDK 8 or higher
- **Memory:** Minimum 512MB RAM
- **Storage:** 50MB free disk space
- **Display:** 1024x768 minimum resolution

---

## 🐛 Known Issues & Solutions

| Issue | Solution |
|-------|----------|
| Application won't start | Ensure Java 8+ is installed and in PATH |
| Data not persisting | Check write permissions in user home directory |
| UI elements not displaying | Try different look and feel settings |

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Muhammad Shaheer Malik**  
- 🌐 [Portfolio](https://shaheer-portfolio-omega.vercel.app)  
- 💼 [LinkedIn](https://linkedin.com/in/malik-shaheer03)  
- 🐙 [GitHub](https://github.com/malik-shaheer03)  
- 📸 [Instagram](https://instagram.com/malik_shaheer03)  
- 📧 [Email Me](mailto:shaheermalik03@gmail.com)  

---

<div align="center">

**Made with ❤️ and lots of ☕**

*If you found this project helpful, please consider giving it a ⭐*
