# 🗨️ Peer-to-Peer Chat Application (Java & JavaFX)

This project implements a **distributed Peer-to-Peer (P2P) chat system** using **Java** and **JavaFX**.  
The system enables direct communication between clients using **TCP sockets**, with a lightweight **Directory Server** handling peer registration and discovery.

---

## 📖 Table of Contents
- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Features](#features)
- [Communication Flow](#communication-flow)
- [Application Protocol](#application-protocol)
- [Screenshots](#screenshots)
- [How to Run](#how-to-run)
- [Technologies Used](#technologies-used)
- [License](#license)

---

## 🧩 Overview

The **P2P Chat Application** allows multiple clients to communicate directly with each other through TCP sockets.  
A **Directory Server** helps clients discover each other by:
- Registering new users.
- Maintaining a list of active users via periodic heartbeat messages.
- Providing IP and port information for peer connection requests.

Once connected, clients communicate **directly**, ensuring efficient and decentralized messaging.

---

## ⚙️ System Architecture

### Components:
- **Client Process**
  - Registers with the Directory Server.
  - Sends periodic “I am alive” messages.
  - Displays active clients.
  - Connects directly with another peer for chat.

- **Directory Server**
  - Registers and tracks clients.
  - Responds to “alive” messages and removes inactive users.
  - Provides IP and port info when clients request to chat.

### Diagram
![Socket Communication Architecture](https://github.com/user-attachments/assets/f01002ab-ada0-42ec-b4f7-db53a29a8e9b)


---

## 💡 Features
- Real-time peer-to-peer chat via TCP.
- Centralized directory for client discovery.
- Periodic heartbeat system to track online users.
- Asynchronous message handling with Java multithreading.
- Simple and clean JavaFX user interface.
- Decentralized direct client communication.

---

## 🔄 Communication Flow

### Logical Flow
![Logical Communication Flow](https://github.com/user-attachments/assets/33b052db-4977-4450-8b7e-7206d34ddd19)

### JavaFX UI Flow
![User Interface Communication Flow](https://github.com/user-attachments/assets/3b4e8d3c-6814-4f90-89e4-30a6788200c7)

---

## 🧠 Application Protocol

### To Directory Server:
| Operation | Message Format |
|------------|----------------|
| Registration | `I want to register,<username>` |
| Heartbeat | `I am alive,<username>,<port>` |
| Chat Request | `I want to talk,<target_username>` |

### To Peers:
| Operation | Message Format |
|------------|----------------|
| Start Chat | `<sender_username>,start chat` |
| Send Message | `<sender_username>,<message>` |

### From Server:
| Response Type | Example |
|----------------|----------|
| Success | `Successful register` |
| Error | `Username already exists` |
| Active Clients | `Ali,Abdallah,Mohammad` |
| Peer Info | `Ali,192.168.1.10,5555` |

---

## 🖼️ Screenshots

### Login Screen
![Client Registration](https://github.com/user-attachments/assets/97987201-cdc6-42d9-88dc-8405763d4b64)

### Active Clients
![Heartbeat Screen](https://github.com/user-attachments/assets/fe637127-4fdf-4ad7-9ee8-f3fe6772d76b)

### Request Chat
![Chat Request](https://github.com/user-attachments/assets/1bc004da-8848-450b-8f8c-9a79c3e5672e)

### Chat Window
![Client Chat Communication](https://github.com/user-attachments/assets/4d04a0b5-7b12-449f-8769-8112425957c8)

---

## 🚀 How to Run

### 🖥️ Requirements
- Java JDK 23 or higher  
- JavaFX SDK  
- IDE such as IntelliJ IDEA / Eclipse / VS Code  

### ⚙️ Steps
1. **Clone this repository**
   ```bash
   git clone https://github.com/3odeh/p2p-chat-javafx.git
   cd p2p-chat-javafx
   ```

2. **Run the Directory Server**
   - Open `DirectoryServer.java`
   - Run the main method  
   *(This will listen for client connections.)*

3. **Run Client Applications**
   - Open multiple instances of `ClientApp.java`
   - Enter unique usernames on login.
   - Select a client and start chatting!

---

## 🛠️ Technologies Used
- **Java** (Sockets, Threads)
- **JavaFX** (UI)
- **TCP/IP Networking**
- **Object-Oriented Design**
- **Multithreading**

---

## 📄 License
This project is licensed under the **MIT License** — free to use and modify for educational purposes.

---

### ⭐ If you found this project helpful, consider giving it a star on GitHub!
