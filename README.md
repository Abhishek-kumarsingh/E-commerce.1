# EcommerceHub - Modern E-commerce Platform

A full-stack e-commerce application built with React, TypeScript, Spring Boot, and MongoDB.

## 🚀 Quick Start

### Prerequisites

- **Node.js 18+** and npm (see installation instructions below)
- **Java 17+** and Maven
- **MongoDB** (local or cloud)

### Installation

#### 1. Install Node.js and npm (if not installed)

**Option A: Automatic Installation (Windows)**
```bash
# Run the provided installer script
install-nodejs.bat
```

**Option B: Manual Installation**
1. Download Node.js from [https://nodejs.org/](https://nodejs.org/)
2. Install Node.js (includes npm)
3. Restart your terminal/command prompt

**Option C: Using Chocolatey (Windows)**
```bash
choco install nodejs
```

**Option D: Using Homebrew (macOS)**
```bash
brew install node
```

#### 2. Install Frontend Dependencies
```bash
# Navigate to project root
cd /path/to/ecommerce-project

# Install dependencies
npm install
```

#### 3. Install Backend Dependencies
```bash
# Navigate to backend directory
cd backend

# Install Maven dependencies
mvn clean install
```

### Running the Application

#### Frontend Development Server
```bash
# From project root
npm run dev
```

The frontend will be available at: http://localhost:3000

#### Backend Server
```bash
# From backend directory
mvn spring-boot:run
```

The backend API will be available at: http://localhost:8080

#### Using Provided Scripts
```bash
# Run frontend (Windows)
run-frontend.bat

# Run backend (Windows)
run-backend.bat

# Run frontend (PowerShell)
run-frontend.ps1
```

## 🔧 Recent Fixes & Improvements

### ✅ Fixed Issues

#### 1. **Category Page Design Issues**
- **Problem**: Cards were too small and information was getting cut off
- **Solution**: 
  - Improved responsive grid layout (1-5 columns based on screen size)
  - Added list view mode for better information display
  - Enhanced card sizing and spacing
  - Fixed text overflow with proper line clamping
  - Added breadcrumb navigation for category pages

#### 2. **Cart Functionality Issues**
- **Problem**: Cart was not working properly
- **Solution**:
  - Added local storage fallback when API is unavailable
  - Improved error handling and user feedback
  - Fixed cart persistence across sessions
  - Enhanced cart state management

#### 3. **Page Speed Issues**
- **Problem**: Pages were loading slowly
- **Solution**:
  - Reduced artificial delays from 500ms to 100ms
  - Optimized component rendering
  - Added lazy loading for images
  - Improved state management efficiency

#### 4. **npm run dev Not Working**
- **Problem**: Node.js/npm not installed
- **Solution**:
  - Created automatic Node.js installer script
  - Added comprehensive installation instructions
  - Provided multiple installation methods

### 🎨 Design Improvements

#### Enhanced Product Cards
- **Better Responsiveness**: Cards adapt from 1 column (mobile) to 5 columns (2xl screens)
- **Improved Information Display**: 
  - Larger text for better readability
  - Proper text truncation with ellipsis
  - Better spacing and layout
- **Dual View Modes**: Grid and list views for different preferences
- **Enhanced Hover Effects**: Smooth animations and interactions

#### Category Page Features
- **Dynamic Page Titles**: SEO-friendly titles based on category
- **Breadcrumb Navigation**: Easy navigation back to parent pages
- **Category-Specific Content**: Shows category name and product count
- **Sticky Sidebar**: Filter sidebar stays in view while scrolling

#### Performance Optimizations
- **Reduced Loading Times**: Faster page transitions
- **Optimized Animations**: Smoother user experience
- **Better Error Handling**: Graceful fallbacks for API failures
- **Improved Caching**: Better state persistence

## 📁 Project Structure

```
E-commerce.1/
├── src/                    # Frontend source code
│   ├── components/         # Reusable UI components
│   ├── pages/             # Page components
│   ├── store/             # State management (Zustand)
│   ├── services/          # API services
│   ├── types/             # TypeScript type definitions
│   └── data/              # Mock data
├── backend/               # Spring Boot backend
│   ├── src/main/java/     # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml           # Maven dependencies
├── scripts/               # Utility scripts
└── README.md             # This file
```

## 🛠️ Key Technologies

### Frontend
- **React 18** with TypeScript
- **Vite** for fast development
- **Tailwind CSS** for styling
- **Framer Motion** for animations
- **Zustand** for state management
- **React Router** for navigation
- **React Query** for data fetching

### Backend
- **Spring Boot 3** with Java 17
- **Spring Security** with JWT
- **Spring Data MongoDB**
- **Maven** for dependency management

## 🎯 Features

### User Features
- ✅ Product browsing and search
- ✅ Category-based navigation
- ✅ Shopping cart functionality
- ✅ User authentication
- ✅ Order management
- ✅ Wishlist functionality
- ✅ Responsive design

### Admin Features
- ✅ Product management
- ✅ Order management
- ✅ User management
- ✅ Analytics dashboard

## 🚀 Deployment

### Frontend Deployment
```bash
# Build for production
npm run build

# Preview production build
npm run preview
```

### Backend Deployment
```bash
# Build JAR file
mvn clean package

# Run JAR file
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

## 🔧 Troubleshooting

### Common Issues

#### 1. **npm command not found**
```bash
# Install Node.js first
# Windows: Run install-nodejs.bat
# macOS: brew install node
# Linux: sudo apt install nodejs npm
```

#### 2. **Port already in use**
```bash
# Kill process on port 3000 (frontend)
npx kill-port 3000

# Kill process on port 8080 (backend)
npx kill-port 8080
```

#### 3. **MongoDB connection issues**
- Ensure MongoDB is running
- Check connection string in `application.yml`
- Verify network connectivity

#### 4. **Build errors**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install

# For backend
mvn clean install
```

## 📝 Environment Variables

Create a `.env` file in the project root:

```env
# Frontend
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=EcommerceHub

# Backend (in application.yml)
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/ecommerce
  jwt:
    secret: your-jwt-secret-key
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

If you encounter any issues:

1. Check the troubleshooting section above
2. Review the console for error messages
3. Ensure all dependencies are installed
4. Verify your environment setup

For additional help, please open an issue on GitHub.
