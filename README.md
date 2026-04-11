# Advora 📱
### Smart Classifieds for Smart Cities

Advora is a mobile-based classified advertisement platform designed to digitize and enhance traditional newspaper classifieds in cities like Ujjain. It transforms static, hard-to-search listings into a modern, interactive, and user-friendly mobile experience.

---

##  Problem Statement

Traditional newspaper classifieds are:
- Difficult to search
- Not interactive
- Lack images and updates
- Cannot be filtered or saved
- Become outdated quickly

Users often miss important opportunities due to poor accessibility and usability.

---

##  Solution

Advora bridges the gap between **offline classifieds** and the **digital world** by providing:
- Searchable and filterable ads
- Real-time updates
- Image-based listings
- Easy posting and management of ads

---

##  Key Features

- 🔍 Browse & Filter Ads (Jobs, Rentals, Vehicles, Services)
- ➕ Post Your Own Ads
- ❤️ Save Ads for Later
- 🔔 Notification Panel
- 🗺️ Map View for Local Listings
- 📊 Ad Status (Active / Expired)
- 📰 Newspaper Classified Integration
- 🛠️ Admin Moderation System

---

##  Tech Stack

### 📱 Mobile App
- Kotlin (Android)
- XML UI / Jetpack Components

### 🔐 Authentication
- Firebase Authentication (Email/Password)

### 🗄️ Database
- Firebase Firestore (NoSQL)

### 🖼️ Storage
- Firebase Storage (Images)

### 🔔 Notifications
- Firebase Cloud Messaging (FCM)

### ⚙️ Backend Services
- Firebase Cloud Functions

### 🕷️ Web Scraping
- Python + BeautifulSoup

### 🧠 Duplicate Detection
- Levenshtein Algorithm

---

## Project Structure
 - /app or /src → Android source code
 - /docs → Wireframes, diagrams, design system
 - /assets → Images, icons, UI assets
 - README.md → Project documentation
 - CONTRIBUTING.md → Contribution guidelines
 - .gitignore → Ignore unnecessary files
 - LICENSE → Open-source license
## ⚙️ Setup & Installation

### Prerequisites:
- Android Studio installed
- Firebase project setup

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/aashmita/Advora.git
2. Open in Android Studio
3. Connect Firebase:
   Add google-services.json
   Enable Authentication & Firestore
4. Run the app on emulator or device
