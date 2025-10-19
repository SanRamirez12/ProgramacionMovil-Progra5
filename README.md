# 📦 AeropostApp

**AeropostApp** es una aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose**, inspirada en la plataforma de logística y envíos **Aeropost**.  
El proyecto busca replicar, optimizar y extender los procesos de **registro de clientes, paquetes y facturación**, además de integrar módulos de **tracking GPS, reportes y seguridad**, bajo una arquitectura moderna **MVVM (Model–View–ViewModel)**.

---

## 🚀 Estado del Proyecto

🟢 En desarrollo activo  
📅 Meta de entrega: **Segunda semana de diciembre 2025**  
📍 Curso: **Programación 5 – Universidad Latina de Costa Rica**  
👨‍💻 Desarrollador principal: **Santiago Ramírez Elizondo**

---

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura limpia y modular** basada en **MVVM**, estructurada en capas:
/data → Repositorios, DAOs y fuentes de datos (Room / Retrofit)
/domain → Modelos, entidades y lógica de negocio
/ui → Pantallas Jetpack Compose, ViewModels y eventos de UI
/navigation → AppNav y rutas con BottomBar
/components → Reutilizables de interfaz (botones, inputs, scaffold)
/theme → Colores, tipografía y estilos globales Aeropost


**Stack técnico principal:**
- **Kotlin + Jetpack Compose**
- **Room Database** (persistencia local)
- **Retrofit + OkHttp** (consumo de APIs externas)
- **Material 3 Design**
- **Android ViewModel / LiveData / StateFlow**
- **WorkManager** (tareas en segundo plano)
- **BiometricPrompt + EncryptedSharedPreferences** (seguridad)
- **MPAndroidChart** (gráficas)
- **iText / PdfDocument** (PDFs)
- **ZXing / ML Kit** (QR Scanner)
- **TensorFlow Lite (opcional)** para IA ligera

---

## ✨ Funcionalidades Principales

| Módulo | Descripción |
|---------|-------------|
| 🔐 **Login y Seguridad** | Inicio de sesión local, hash de contraseña (Argon2/bcrypt), autenticación biométrica, registro de bitácora. |
| 👥 **Clientes** | CRUD completo de clientes (nombre, cédula, correo, teléfono). Validación de identificación. |
| 📦 **Paquetes** | Registro de paquetes asociados a clientes (peso, valor, tracking, tienda). Validaciones numéricas. |
| 🧾 **Facturación** | Cálculo automático (peso × 12 + 13% IVA + 10% especial). Generación y exportación de factura en PDF. |
| 💱 **Tipo de Cambio** | Consulta diaria vía API (Retrofit) y cache local. Muestra totales en CRC/USD. |
| 📍 **Tracking** | Registro de pings GPS, mapa interactivo con ruta y timeline de eventos (Recibido, En Ruta, Entregado). |
| 📊 **Reportes** | Totales mensuales por cliente y tracking, gráficas con MPAndroidChart y PDF de resumen. |
| 🧠 **IA (opcional)** | Predicción de tiempo de entrega (ETA) y clasificación automática de paquetes especiales con TF-Lite. |
| 🪄 **Extras / UX** | Widgets de acceso rápido, escáner QR para tracking, notificaciones de entrega y tema oscuro/claro. |

---

## 🧰 Tecnologías y Librerías

| Categoría | Librerías |
|------------|-----------|
| UI | Jetpack Compose / Material 3 |
| BD local | Room + SQLCipher (opcional) |
| Networking | Retrofit + OkHttp |
| Seguridad | Argon2 / BCrypt / EncryptedSharedPreferences / BiometricPrompt |
| Mapas & GPS | Google Maps SDK / Fused Location Provider |
| PDF | iText / PdfDocument |
| Gráficas | MPAndroidChart |
| Background | WorkManager |
| ML / IA | TensorFlow Lite |
| QR | ZXing / ML Kit |

---

## 📱 Vista General (UI Preview)

- **Pantalla de inicio:** Logo Aeropost + módulos principales  
- **Clientes / Paquetes:** Formularios Compose scrollables y listas con LazyColumn  
- **Facturación:** Totales dinámicos + botón de generar PDF  
- **Tracking:** Mapa con marcadores y ruta histórica  
- **Reportes:** Gráficas interactivas y exportación PDF  

*(Sección con capturas y video demo se agregará al finalizar el desarrollo)*

---

## 📊 Progreso y Planificación

El avance del proyecto se gestiona mediante **Notion** en el siguiente tablero:  

🔗 **Notion Roadmap:** [Abrir tablero de progreso]([https://www.notion.so/TU_LINK_AQUI](https://www.notion.so/291122423059802c9e65ea98ea9b89b8?v=2911224230598043a304000ccefd6ead&source=copy_link))

El tablero incluye:
- Checklist por módulo  
- Estado de cada subproceso (Pendiente / En curso / Completado)  
- Fechas de entrega y prioridad  
- Vistas Kanban y Calendario  

---

## 🧩 Próximas metas

- Implementar tracking GPS en tiempo real  
- Añadir autenticación biométrica completa  
- Integrar gráficas MPAndroidChart y reportes PDF  
- Añadir widget de acceso rápido y QR scanner  
- Completar modelos IA ligeros (estimador de ETA)

---

## 🧾 Licencia

Proyecto académico sin fines comerciales.  
© 2025 – Santiago Ramírez Elizondo. Todos los derechos reservados.

---


