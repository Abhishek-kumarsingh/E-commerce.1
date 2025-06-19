# EcommerceHub - Modern E-commerce Platform

A full-stack e-commerce platform built with React, TypeScript, TailwindCSS (frontend) and Java Spring Boot (backend), featuring PostgreSQL and MongoDB databases.

## 🚀 Features

### Frontend (React + TypeScript + TailwindCSS)
- Modern, responsive UI with TailwindCSS
- TypeScript for type safety
- Clerk authentication integration
- Stripe/Razorpay payment integration
- Product catalog with search and filtering
- Shopping cart and wishlist
- User dashboard and order management
- Admin panel for product and order management

### Backend (Java Spring Boot)
- RESTful API with comprehensive endpoints
- JWT-based authentication and authorization
- Dual database architecture (PostgreSQL + MongoDB)
- Email service with template support
- File upload and management
- Comprehensive security configuration
- API documentation with Swagger/OpenAPI
- Caching and performance optimization

### Databases
- **PostgreSQL**: User data, orders, payments, reviews
- **MongoDB**: Product catalog, categories
- **Redis**: Caching (optional)

## 🛠️ Tech Stack

### Frontend
- React 18
- TypeScript
- TailwindCSS
- Clerk (Authentication)
- Stripe/Razorpay (Payments)
- Axios (HTTP Client)
- React Router (Navigation)

### Backend
- Java 21
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- Spring Data MongoDB
- JWT Authentication
- Maven
- Swagger/OpenAPI

### Databases
- PostgreSQL 15
- MongoDB 7.0
- Redis 7 (optional)

### DevOps
- Docker & Docker Compose
- Nginx (reverse proxy)
- MailHog (email testing)

## 📋 Prerequisites

- Java 21 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- Maven 3.9+
- Git

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ecommercehub.git
cd ecommercehub
```

### 2. Environment Setup

Create environment files:

**Backend (.env)**
```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/ecommerce_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=password
MONGODB_URI=mongodb://localhost:27017/ecommerce_products

# JWT Configuration
JWT_SECRET=myVerySecretKeyThatIsAtLeast256BitsLongForHS512Algorithm
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Mail Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# File Upload
FILE_UPLOAD_DIR=./uploads
FILE_MAX_SIZE=10485760

# Frontend URL
FRONTEND_URL=http://localhost:3000
```

**Frontend (.env)**
```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_CLERK_PUBLISHABLE_KEY=your_clerk_publishable_key
REACT_APP_STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
```

### 3. Using Docker Compose (Recommended)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

This will start:
- PostgreSQL (port 5432)
- MongoDB (port 27017)
- Redis (port 6379)
- MailHog (ports 1025, 8025)
- Backend API (port 8080)
- Frontend (port 3000)
- Nginx (port 80)

### 4. Manual Setup

#### Backend Setup
```bash
cd backend

# Install dependencies and run
mvn clean install
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/ecommerce-backend-1.0.0.jar
```

#### Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm run build
```

#### Database Setup
```bash
# Start PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_DB=ecommerce_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 postgres:15-alpine

# Start MongoDB
docker run -d --name mongodb \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -p 27017:27017 mongo:7.0
```

## 📚 API Documentation

Once the backend is running, access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🔧 Development

### Backend Development
```bash
cd backend

# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Generate test coverage
mvn jacoco:report
```

### Frontend Development
```bash
cd frontend

# Run tests
npm test

# Run tests with coverage
npm run test:coverage

# Lint code
npm run lint

# Format code
npm run format
```

## 🏗️ Project Structure

```
ecommercehub/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/ecommerce/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── repository/  # Data access layer
│   │       ├── entity/      # JPA entities
│   │       ├── dto/         # Data transfer objects
│   │       ├── config/      # Configuration classes
│   │       └── security/    # Security configuration
│   ├── src/main/resources/
│   │   ├── application.yml  # Application configuration
│   │   └── db/migration/    # Database migrations
│   └── pom.xml             # Maven dependencies
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── pages/          # Page components
│   │   ├── hooks/          # Custom hooks
│   │   ├── services/       # API services
│   │   ├── types/          # TypeScript types
│   │   └── utils/          # Utility functions
│   ├── public/             # Static assets
│   └── package.json        # NPM dependencies
├── scripts/                # Setup scripts
├── docker-compose.yml      # Docker services
└── README.md              # This file
```

## 🔐 Authentication

The application uses JWT-based authentication with the following endpoints:

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh-token` - Refresh JWT token
- `POST /api/auth/forgot-password` - Password reset request
- `POST /api/auth/reset-password` - Reset password
- `POST /api/auth/verify-email` - Email verification

## 💳 Payment Integration

### Stripe Integration
```javascript
// Frontend payment processing
const stripe = useStripe();
const elements = useElements();

const handlePayment = async () => {
  const { error, paymentMethod } = await stripe.createPaymentMethod({
    type: 'card',
    card: elements.getElement(CardElement),
  });
  
  if (!error) {
    // Process payment with backend
  }
};
```

### Razorpay Integration
```javascript
// Razorpay payment options
const options = {
  key: process.env.REACT_APP_RAZORPAY_KEY,
  amount: amount * 100, // Amount in paise
  currency: 'INR',
  name: 'EcommerceHub',
  description: 'Order Payment',
  handler: function (response) {
    // Handle successful payment
  }
};
```

## 📧 Email Configuration

Configure email settings in `application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

## 🧪 Testing

### Backend Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

### Frontend Testing
```bash
# Run unit tests
npm test

# Run e2e tests
npm run test:e2e

# Generate coverage report
npm run test:coverage
```

## 🚀 Deployment

### Docker Deployment
```bash
# Build and deploy with Docker Compose
docker-compose -f docker-compose.prod.yml up -d
```

### Manual Deployment
```bash
# Backend
mvn clean package
java -jar target/ecommerce-backend-1.0.0.jar

# Frontend
npm run build
# Serve build folder with nginx or similar
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support, email support@ecommercehub.com or create an issue in the GitHub repository.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the amazing frontend library
- All open source contributors who made this project possible
