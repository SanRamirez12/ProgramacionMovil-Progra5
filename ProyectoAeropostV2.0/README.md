# 📦 AeropostApp

**AeropostApp** is an Android mobile application developed in **Kotlin** using **Jetpack Compose**, designed to support the digitalization of logistics operations for small and medium-sized enterprises (SMEs).  
The project is inspired by the Aeropost logistics model and focuses on the integrated management of clients, packages, users, billing, and operational reports through a modern and maintainable mobile architecture.

---

## 🚀 Project Status

🟢 **Version 1.0 – Functional Release**  
📍 Academic Project – *Programming V*  
🏫 Universidad Latina de Costa Rica  

The application is fully functional at a local level, with offline persistence and complete business logic implemented. Future improvements are planned and documented.

---

## 🏗️ System Architecture

AeropostApp follows a **clean MVVM architecture** combined with a **single-activity approach**, ensuring separation of concerns, scalability, and maintainability.

### Layered structure:
- **core/**  
  Design system, security constants, session management, and network providers.
- **data/**  
  Room database entities, DAOs, repositories, mappers, and local/remote data sources.
- **domain/**  
  Business models, enums, interfaces, and core business logic.
- **presentation/**  
  Jetpack Compose screens, ViewModels, navigation graph, and reusable UI components.

This structure prevents direct coupling between UI and persistence layers and allows future extension with external services.

---

## 🧰 Tech Stack

- **Kotlin**
- **Jetpack Compose (Material 3)**
- **Room (SQLite)** – local persistence
- **MVVM + State management**
- **PDF generation**
- **CSV / ZIP report export**
- **SMTP (Gmail) email delivery**
- **Password hashing (PBKDF2 + salt + pepper)**

---

## ✨ Implemented Features (v1.0)

### 🔐 Authentication & Security
- Local login system with hashed passwords (PBKDF2 with per-user salt and system pepper).
- Session validation and basic access control.
- No plaintext credential storage.

### 👥 Client Management
- Full CRUD operations for clients.
- Field validation and duplicate prevention.
- Search and filtering support.

### 📦 Package Management
- Package registration linked to clients.
- Automatic tracking code generation based on store, date, and selected warehouse (casillero).
- Status handling and traceability.

### 🧾 Billing
- Automatic invoice calculation based on:
  - Package weight
  - Declared value
  - Taxes and special-product rules
- PDF invoice generation.
- Email delivery of invoices.

### 📊 Reports
- Client, package, and billing reports.
- Export to CSV and compressed ZIP format.
- Email delivery of generated reports.

---

## 🧪 Testing & Validation

- Functional testing of all modules.
- Integration testing between UI, ViewModels, and Room database.
- Validation of business rules, navigation flow, and document generation.
- Multiple issues related to navigation state, email delivery, and data consistency were identified and corrected.

---

## 🔮 Future Improvements

Planned extensions include:
- Real-time GPS tracking with map visualization.
- Cloud synchronization (Firebase).
- External authentication (Google / Facebook).
- Two-factor authentication (2FA).
- Electronic invoicing integration with government APIs.
- Improved exchange-rate API handling.
- Advanced security algorithms (bcrypt / Argon2).
- AI-assisted analytics and internal chatbot support.

---

## 📄 License

Academic project developed for educational purposes.  
© 2025 – Santiago Ramírez Elizondo
