# 🌱 KhamarBD (খামারবিডি) — স্মার্ট কৃষি ও সমন্বিত খামার ব্যবস্থাপনা প্ল্যাটফর্ম
> **Modern Integrated Smart Agriculture & Farm Management Enterprise Ecosystem**  
> *A unified, multi-role digital agriculture platform empowering rural farmers, wholesale commercial buyers, agro-input suppliers, and veterinary doctors across all 64 districts of Bangladesh.*

---

## 📖 Table of Contents
1. [About KhamarBD & Vision](#-about-khamarbd--vision)
2. [Problem Statement & Solution](#-problem-statement--solution)
3. [The Four Core Stakeholders (User Roles)](#-the-four-core-stakeholders-user-roles)
4. [Comprehensive Module & Feature Breakdown](#-comprehensive-module--feature-breakdown)
   - [1. Farmer Portal (খামারি পোর্টাল)](#1-farmer-portal-খামারি-পোর্টাল)
   - [2. Commercial Buyer Portal (পাইকারি ও প্রাতিষ্ঠানিক ক্রেতা পোর্টাল)](#2-commercial-buyer-portal-পাইকারি-ও-প্রাতিষ্ঠানিক-ক্রেতা-পোর্টাল)
   - [3. Input Supplier Portal (কৃষি উপকরণ সরবরাহকারী পোর্টাল)](#3-input-supplier-portal-কৃষি-উপকরণ-সরবরাহকারী-পোর্টাল)
   - [4. Specialist / Vet Doctor Portal (পশুচিকিৎসক ও কৃষি বিশেষজ্ঞ পোর্টাল)](#4-specialist--vet-doctor-portal-পশুচিকিৎসক-ও-কৃষি-বিশেষজ্ঞ-পোর্টাল)
   - [5. Public Agro Marketplace & 64-District Directory](#5-public-agro-marketplace--64-district-directory)
   - [6. Real-Time Chat & Consultation System](#6-real-time-chat--consultation-system)
   - [7. Financial Accounting & Gross Margin Engine](#7-financial-accounting--gross-margin-engine)
5. [End-to-End Workflow Examples](#-end-to-end-workflow-examples)
6. [Technical Architecture & Spring Boot Structure](#-technical-architecture--spring-boot-structure)
7. [Database Schema & Data Model](#-database-schema--data-model)
8. [Setup, Installation & Running Guide](#-setup-installation--running-guide)
9. [Default Test Accounts](#-default-test-accounts)

---

## 🌟 About KhamarBD & Vision
**KhamarBD (খামারবিডি)** is an enterprise-grade digital agriculture ecosystem designed to modernize and digitize Bangladesh's vast agro-economy. In conventional agricultural practices, smallholder farmers face severe operational hurdles: lack of scientific batch management, unorganized manual bookkeeping, middleman exploitation (*Faria/Dalal*), absence of timely veterinary medical support, and fragmented procurement channels.

KhamarBD unifies every agricultural stakeholder onto a single collaborative digital platform:
- **Farmers** gain automated routine scheduling, unit-level cost accounting, direct digital veterinary telemedicine, and middleman-free selling.
- **Commercial Buyers** (Wholesale Paikars, Hotels, Restaurants, Student Messes/Halls, Catering companies) can source guaranteed farm-fresh produce with live dispatch tracking.
- **Input Suppliers** (Feed mills, Hatcheries, Vet Pharmacies, Fertilizer dealers) can publish catalog items and manage order fulfillment across 64 districts.
- **Specialists** (Veterinarians and Agronomists) can provide telemedicine consultations, inspect health records, and issue legally verifiable digital prescriptions.

---

## 🎯 Problem Statement & Solution

| Traditional Agro Challenge in BD | How KhamarBD Solves It |
|---|---|
| **Middleman Exploitation & Unfair Pricing** | Farmers list active batches directly to the marketplace; wholesale buyers buy directly from farms at fair market prices. |
| **Lack of Animal/Crop Batch Tracking** | Dedicated "Tracking Units" monitor every flock, herd, pond, or field from day 1 to harvest. |
| **No Clear Financial Accounting** | Unit-level double-entry financial ledger automatically tracks cost-per-batch and calculates gross profit margins. |
| **Delayed Veterinary Medical Care** | Digital consultation queue with instant live chat and structured digital prescriptions. |
| **Geographical Isolation** | 64-District central database allows farmers and buyers to collaborate across divisional boundaries. |

---

## 👥 The Four Core Stakeholders (User Roles)

```
                       ┌─────────────────────────────────────────┐
                       │           KhamarBD Platform             │
                       └────────────────────┬────────────────────┘
                                            │
         ┌───────────────────┬──────────────┴───────┬───────────────────┐
         │                   │                      │                   │
         ▼                   ▼                      ▼                   ▼
   🧑‍🌾 Farmer         🛒 Commercial Buyer     🏪 Input Supplier    🩺 Vet Specialist
 (Poultry, Dairy,    (Hotel, Paikar, Mess,   (Feed, Seeds, Meds,  (Doctors, Experts,
 Fisheries, Crops)   Wholesale Sourcing)     Machinery Stores)    Prescriptions)
```

1. **Farmer (খামারি):** Manages farms, animal/crop batches, daily chores, expenses, revenues, vet consultations, and sells produce.
2. **Commercial Buyer (পাইকারি ও প্রাতিষ্ঠানিক ক্রেতা):** Procures live broilers, raw milk, pond fish, and seasonal crops in bulk with customized delivery notes and tracking.
3. **Input Supplier (সরবরাহকারী):** Sells poultry feed, cattle concentrate, aqua feed, seeds, vaccines, and equipment; tracks incoming farmer requests through 6 dispatch stages.
4. **Specialist (পশুচিকিৎসক ও বিশেষজ্ঞ):** Examines farmer cases, provides advisory consultation, and generates structured digital prescriptions with medicine dosage.

---

## 🔍 Comprehensive Module & Feature Breakdown

---

### 1. Farmer Portal (খামারি পোর্টাল)

The Farmer Portal is the central operational workstation for agricultural producers (Poultry, Dairy, Fisheries, Crops):

- **Executive Farm Dashboard (`dashboard.html`):**
  - Instant overview of active animal batches and crop acreages.
  - Urgent alerts for overdue vaccinations, feeding routines, and pending tasks.
  - Real-time gross financial snapshot (total revenue, operational cost, and net margin).
  - Quick action buttons to record expenses, sell produce, and request doctor consultations.

- **Farm Profiles & Multi-Location Management (`farms.html`):**
  - Create and manage multiple physical farm locations (e.g., Bogura Shed-1, Natore Pond Complex).
  - Record geographic metadata: Division, District, Upazila, GPS Coordinates, Farm Type (Poultry, Dairy, Fishery, Agriculture, Mixed).

- **Batch & Tracking Unit Management (`tracking-unit.html`):**
  - Create distinct tracking batches (e.g., `Lot #500: Sonali Broiler Flock`, `Pond #02: Rui/Katla Stock`, `Shed #01: Friesian Milking Herd`).
  - Set start date, initial quantity, target harvest date, area size, and species/breed.
  - Real-time status lifecycle: `ACTIVE` → `HARVEST_READY` → `SOLD` → `CLOSED`.

- **Automated Routine Tasks & Calendar (`routines.html`):**
  - Pre-built scientific routine templates based on sector guidelines (Day-old chick brooding, vaccination schedules, pond liming, water pH testing).
  - Daily checklist showing morning/afternoon/evening chores with 1-click status toggling (`PENDING` → `COMPLETED`).

- **Unit-Level Financial Ledger (`ledger.html`):**
  - Double-entry farm bookkeeping categorizing income (Milk sales, bird harvest, eggs) and expenses (Feed, vaccines, labor, electricity, machinery repair).
  - Automated gross operating margin and profit percentage calculations per batch.
  - Filter by Tracking Unit, Transaction Type (Income/Expense), Search keywords, and 1-click CSV Report Export.

- **Veterinary Prescriptions & Consultations (`prescriptions.html`, `consultations.html`):**
  - View digital medical prescriptions issued by veterinary doctors with medicine dosage, frequency, and treatment duration.
  - Request remote consultations by attaching problem descriptions and specific animal unit IDs.

- **Direct Produce Selling (`add-product.html`):**
  - Publish harvest listings directly from active tracking units to the public marketplace with photo uploads, price per unit, and available batch quantity.

---

### 2. Commercial Buyer Portal (পাইকারি ও প্রাতিষ্ঠানিক ক্রেতা পোর্টাল)

Designed specifically for **Hotels, Restaurants, Student Halls/Messes, Catering Businesses, Supermarkets, and Wholesale Paikars**:

- **Buyer Procurement Dashboard (`buyer/dashboard.html`):**
  - Live summary of total procurement spending, active supply dispatches, and incoming farm shipments.
  - Direct 1-click shortcuts to explore live broiler flocks, fresh milk supplies, fish harvests, and vegetable crops.

- **Bulk Procurement System (`marketplace.html` modal):**
  - Select desired wholesale volume (e.g., 50 kg live broiler, 40 liters raw milk).
  - Automatic pricing calculation based on farmer unit rate.
  - Specify delivery destination (Hotel branch, mess counter, storage depot) and special instructions (e.g., oxygenated packaging, morning delivery by 8 AM).

- **Live 6-Stage Dispatch Tracking (`buyer/orders.html`):**
  - Real-time status progression from order submission to final handover:
    1. *Order Received*
    2. *Payment Confirmed*
    3. *Processing & Packing*
    4. *In Transport (Vehicle info)*
    5. *Out for Delivery*
    6. *Delivered to Venue*
  - View driver phone numbers, transport van license numbers, and fulfillment notes added by farmers.

---

### 3. Input Supplier Portal (কৃষি উপকরণ সরবরাহকারী পোর্টাল)

Built for Feed Mill Dealers, Hatcheries, Veterinary Pharmacies, Seed Distributors, and Equipment Stores:

- **Supplier Command Center (`supplier/dashboard.html`):**
  - High-level inventory statistics, total sales value, and active orders awaiting fulfillment.
  - Direct shortcuts to add new supply batches and view customer messages.

- **Product Inventory & Catalog Management (`supplier/listings.html`, `supplier/add-product.html`):**
  - Add farm inputs categorized under Poultry Feeds, Cattle Concentrates, Aqua Feeds, Seeds, Fertilizers, Antibiotics/Vaccines, and Farm Machinery.
  - Configure brand names, package units (50kg Bag, Liter Vial, Piece), stock quantity, district availability, and product image uploads.

- **Order Fulfillment & Dispatch Dispatcher (`supplier/orders.html`):**
  - Real-time list of all incoming orders placed by farmers across 64 districts.
  - Interactive visual stepper timeline and 1-click stage advancement buttons.
  - Dispatch note updater: Save courier tracking IDs, pickup driver contact info, and delivery details into MySQL in real-time.
  - 1-click direct chat button with the purchasing farmer.

---

### 4. Specialist / Vet Doctor Portal (পশুচিকিৎসক ও কৃষি বিশেষজ্ঞ পোর্টাল)

A digital clinic workstation for registered Veterinary Surgeons (DVM), Livestock Officers, and Agricultural Specialists:

- **Specialist Dashboard (`specialist/dashboard.html`):**
  - Overview of pending consultation inquiries, active treatment cases, and issued digital prescriptions.

- **Consultation Queue & Triage (`specialist/consultations.html`):**
  - Review incoming farmer telemedicine requests with unit details, observed symptoms, and animal breed history.
  - Change case status from `PENDING` → `IN_PROGRESS` → `COMPLETED`.

- **Digital Prescription Generator (`specialist/prescriptions.html`):**
  - Create standardized digital prescriptions linked to the farmer's specific tracking batch.
  - Add medicine names (e.g., Renamycin LA, Electrolyte solution), unit dosage, feeding/injection frequency (e.g., 1-0-1), and duration (e.g., 5 days).
  - Add clinical notes, dietary instructions, and antibiotic withdrawal warnings.

- **Direct Advisory Telemedicine Chat (`specialist/chat.html`):**
  - Live bilateral communication channel with farmers for instant disease diagnostics, photo reviews, and follow-up care.

---

### 5. Public Agro Marketplace & 64-District Directory

A nationwide public commerce portal accessible to all users without mandatory login requirements:

- **Unified Smart Tabs:**
  1. **Farm Supplies (Inputs):** Feeds, seeds, veterinary medicines, fertilizers, fingerlings, and machinery from certified dealers.
  2. **Farm Produce (Direct from Farms):** Live broiler batches, chilled raw cow milk, live pond fish, and seasonal crops sold directly by verified farmers.
  3. **Specialist Directory:** Verified veterinary surgeons and crop specialists available for remote or in-person advisory.
- **64-District Division-Based Central Modal:**
  - Full registry of all 8 administrative divisions (Dhaka, Chittagong, Rajshahi, Khulna, Barisal, Sylhet, Rangpur, Mymensingh) and all 64 districts of Bangladesh.
  - Dynamic cascading filter allowing users to instantly filter listings by local district or view nationwide supplies.
- **Cross-District Collaboration:**
  - Built-in cross-district messaging and procurement: a farmer in Bogura can buy specialized medicine from a Dhaka supplier or sell broilers to a Sylhet hotel buyer.

---

### 6. Real-Time Chat & Consultation System

- Peer-to-peer real-time messaging engine supporting bilateral communication:
  - **Farmer ⇄ Specialist:** Clinical advisory, symptoms discussion, and follow-up.
  - **Farmer ⇄ Supplier:** Input price negotiation, bulk delivery coordination.
  - **Buyer ⇄ Farmer:** Wholesale produce purchase negotiation and dispatch updates.
- Auto-scroll, unread message badges, timestamp formatting, and topic-based message thread linking (`Order #`, `Consultation #`).

---

### 7. Financial Accounting & Gross Margin Engine

- Automated double-entry ledger integration:
  - When an order is completed, the seller's farm ledger automatically records an **INCOME** entry (*Sales Revenue*).
  - The purchasing farmer's ledger automatically records an **EXPENSE** entry (*Marketplace Purchase*).
- Calculates net operating balance:
  $$\text{Net Operating Margin} = \text{Total Recorded Income} - \text{Total Recorded Expenses}$$
- Generates real-time profit percentage ratios to evaluate batch profitability before slaughter or harvesting.

---

## 🔄 End-to-End Workflow Examples

### Workflow A: Poultry Farming & Wholesale Sale
1. **Batch Initialization:** Farmer Rahim logs in and creates Tracking Unit `Sonali Flock Batch-04` (1,000 chicks).
2. **Routine Chores:** Automated tasks populate Rahim's calendar (Day 1: Brooding temp 95°F, Day 5: Gumboro Vaccine).
3. **Input Purchase:** Rahim visits the Marketplace, orders 10 bags of *Sonali Starter Feed* from Supplier Riazul.
4. **Supply Dispatch:** Supplier Riazul receives the order in `supplier/orders.html`, updates status to `In Transport`, and writes driver contact note. Rahim's portal updates in real-time.
5. **Medical Consultation:** On Day 20, birds show lethargy. Rahim requests a consultation with Dr. Anisur. Dr. Anisur chats with Rahim and issues a digital prescription for *Renamycin LA*.
6. **Marketplace Listing:** On Day 60, Rahim lists 1,000 mature birds on the marketplace at BDT 165/kg.
7. **Wholesale Buy:** Kasturi Restaurant logs into the Buyer Portal, places a bulk order for 200 kg.
8. **Ledger Update:** The system automatically logs BDT 33,000 income in Rahim's financial ledger, calculating his exact net profit margin.

---

## 🏛️ Technical Architecture & Spring Boot Structure

The project follows the standard **Spring Boot Layered Architecture**:

```
C:\Users\Student\Downloads\KhamarBD\src\main\java\com\khamarbd\backend
├── 📂 config
│   ├── GlobalCorsConfig.java          → Cross-Origin Resource Sharing configuration
│   ├── GlobalExceptionHandler.java     → Centralized @RestControllerAdvice exception handler
│   └── WebMvcConfig.java              → Static upload resource mapping
│
├── 📂 controller
│   ├── UserController.java            → User authentication, profile, and search
│   ├── FarmController.java            → Farm registration and location management
│   ├── LotOrAnimalController.java     → Animal/crop tracking units lifecycle
│   ├── RoutineTaskController.java     → Daily chores and task schedule management
│   ├── FarmLedgerController.java      → Income/expense double-entry financial ledger
│   ├── MarketplaceListingController.java → Product listings for supplies & farm produce
│   ├── OrderTransactionController.java → Bulk orders and 6-stage dispatch tracking
│   ├── SpecialistConsultationController.java → Telemedicine consultation booking
│   ├── PrescriptionController.java    → Digital veterinary prescriptions
│   ├── ChatMessageController.java     → Real-time peer-to-peer messaging
│   └── FileUploadController.java      → Product & profile photo upload handling
│
├── 📂 dto
│   ├── FarmLedgerRequestDto.java      → Validated ledger entry payload
│   ├── OrderTransactionRequestDto.java→ Validated order & procurement payload
│   ├── LotOrAnimalRequestDto.java     → Validated tracking unit payload
│   ├── SpecialistConsultationRequestDto.java → Teleconsultation request payload
│   └── ChatMessageRequestDto.java     → Validated chat message payload
│
├── 📂 entity
│   ├── User.java                      → User entity with multi-role support
│   ├── Farm.java                      → Farm entity with GPS & district metadata
│   ├── LotOrAnimal.java               → Batch unit entity with status lifecycle
│   ├── RoutineTask.java               → Scheduled task item entity
│   ├── FarmLedger.java                → Accounting ledger transaction entity
│   ├── MarketplaceListing.java        → Product listing entity
│   ├── OrderTransaction.java          → Order entity with 6-stage tracking
│   ├── SpecialistConsultation.java    → Consultation request entity
│   ├── Prescription.java              → Digital prescription entity
│   └── ChatMessage.java               → Chat message entity
│
├── 📂 repository                      → 15 Spring Data JPA Repository Interfaces
└── 📂 service                         → 15 Transactional Business Logic Services
```

---

## 🗄️ Database Schema & Data Model

### Core Entity Relationships (MySQL):
- **User 1 ──── N Farm:** A single user can operate multiple farms across different districts.
- **Farm 1 ──── N LotOrAnimal:** A farm hosts multiple animal batches, fish ponds, or crop fields.
- **LotOrAnimal 1 ──── N RoutineTask:** Each batch is governed by an automated routine task calendar.
- **LotOrAnimal 1 ──── N FarmLedger:** Each batch maintains isolated financial accounting entries.
- **User 1 ──── N MarketplaceListing:** Users can list input supplies or harvested produce.
- **MarketplaceListing 1 ──── N OrderTransaction:** Each listing can receive multiple buyer procurement orders.
- **SpecialistConsultation 1 ──── 1 Prescription:** A doctor consultation can generate an official digital prescription.
- **SpecialistConsultation 1 ──── N ChatMessage:** Each consultation maintains an isolated communication thread.

---

## 💻 Setup, Installation & Running Guide

### 1. Prerequisites
- **Java Development Kit (JDK):** Version 21 (LTS)
- **Database:** MySQL 8.x / MariaDB running on port `3306` or `3307`
- **Build Tool:** Gradle (Wrapper included)

### 2. Database Initialization
Create a MySQL database named `khamarbd2`:
```sql
CREATE DATABASE khamarbd2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Application Properties Configuration
Inspect `src/main/resources/application.properties`:
```properties
spring.application.name=KhamarBD
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/khamarbd2?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

# Hibernate JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### 4. Build and Run Server
Open PowerShell in the project directory:
```powershell
.\gradlew.bat bootRun
```
The server will initialize on: **`http://localhost:8081`**

### 5. Open Web Pages in Browser
- **Homepage & Public Portal:** `http://localhost:8081/index.html`
- **64-District Marketplace:** `http://localhost:8081/pages/public/marketplace.html`
- **User Sign In:** `http://localhost:8081/pages/auth/login.html`
- **User Registration:** `http://localhost:8081/pages/auth/register.html`

---

## 🔐 Default Test Accounts

For demonstration and evaluation purposes, the system includes pre-configured accounts:

| Role | Stakeholder Name | Phone Number | Password | Default Portal |
|---|---|---|---|---|
| **Farmer** | Md. Rahim (Broiler & Dairy Farmer) | `01884768194` | `password` | `/pages/dashboard.html` |
| **Supplier** | Riazul Agro Feeds & Vet | `01615100769` | `password` | `/pages/supplier/dashboard.html` |
| **Commercial Buyer** | Kasturi Hotel & Restaurant (Kabir) | `01711223344` | `password` | `/pages/buyer/dashboard.html` |
| **Specialist (Vet)** | Dr. Anisur Rahman (DVM) | `01711000004` | `password` | `/pages/specialist/dashboard.html` |


---

## 📦 Clean Git Repository Submission Note
Before committing to GitHub, temporary local log files (`hs_err_pid*.log`, `_server.log`, `_port.txt`) and build outputs (`build/`, `.gradle/`) should be excluded via `.gitignore` to keep the repository lightweight and professional.

---
*KhamarBD — Empowering Bangladesh's Agricultural Future through Digital Integration.*
