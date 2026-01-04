# AeropostApp V2.0

**AeropostApp V2.0** is an Android mobile application developed in **Kotlin** using **Android Studio** and **Jetpack Compose**, created as the final project for the **Programming 5: Mobile Devices** course.  
The application simulates a real-world logistics management system inspired by the **Aeropost** business model, focusing on modularity, clean architecture, and mobile-first design.

---

## Academic Context

- **Course:** Programming 5 – Mobile Devices  
- **Institution:** Universidad Latina de Costa Rica  
- **Project Type:** Final Academic Project  
- **Development Mode:** Individual  
- **Version:** 2.0 (Functional Academic Release)

This version represents a significant architectural and functional evolution over previous iterations.

---

## Project Objective

The main objective of this project is to design and implement a **robust, scalable, and maintainable Android application** that applies advanced mobile development concepts, including:

- Clean architecture and separation of concerns
- Local data persistence
- Secure user authentication
- Business logic implementation
- Document generation and reporting
- Integration with external services

---

## Application Architecture

AeropostApp V2.0 follows a **Clean MVVM architecture** combined with a **single-activity pattern**, ensuring clear separation between layers and long-term maintainability.

### Architectural Layers

- **core/**  
  Global configuration, security utilities, session handling, constants, and shared components.

- **data/**  
  Room entities, DAOs, repositories, mappers, and local data sources.

- **domain/**  
  Business models, enums, use cases, and core application rules.

- **ui / presentation/**  
  Jetpack Compose screens, ViewModels, navigation graph, and reusable UI components.

This structure avoids direct coupling between UI and data layers and allows future integration with remote services.

---

## Technologies Used

- **Kotlin**
- **Android Studio**
- **Jetpack Compose (Material 3)**
- **Room (SQLite)**
- **MVVM architecture**
- **State management**
- **PDF generation**
- **CSV and ZIP export**
- **SMTP email integration (Gmail)**
- **Secure password hashing (PBKDF2 + salt + pepper)**

---

## Core Features Implemented

### Authentication & Security
- Local authentication system.
- Password hashing using PBKDF2 with per-user salt and system-level pepper.
- Session validation and basic access control.
- No plaintext credential storage.

### Client Management
- Complete CRUD operations for clients.
- Input validation and duplicate prevention.
- Search and filtering capabilities.

### Package Management
- Package registration associated with clients.
- Automatic tracking code generation based on date, store, and warehouse.
- Package status handling and traceability.

### Billing System
- Automatic invoice calculation based on:
  - Package weight
  - Declared value
  - Taxes and special handling rules
- PDF invoice generation.
- Email delivery of invoices.

### Reports
- Client, package, and billing reports.
- Export to CSV format.
- Compression into ZIP files.
- Email delivery of generated reports.

---

## Testing and Validation

- Functional testing of all modules.
- Integration testing between UI, ViewModels, and Room database.
- Validation of business rules and navigation flow.
- Multiple issues related to navigation state, data consistency, and email delivery were identified and resolved during development.

---

## Future Enhancements

Planned improvements include:

- Real-time GPS tracking with map visualization
- Cloud synchronization (Firebase)
- External authentication providers (Google / Facebook)
- Two-factor authentication (2FA)
- Electronic invoicing integration
- Improved exchange-rate API handling
- Advanced security algorithms (bcrypt / Argon2)
- AI-assisted analytics and internal chatbot support

---

## License

This project was developed **exclusively for educational purposes** as part of the **Programming 5: Mobile Devices** course.

© 2025 – Santiago Ramírez Elizondo
