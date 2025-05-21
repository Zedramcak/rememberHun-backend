# RememberHun Backend
A Spring Boot application serving as the backend for the RememberHun application, designed to help users manage connections, important dates, preferences, and wishlists.



## 🌟 Features
- **Authentication**: JWT-based authentication with token refresh capabilities
- **User Management**: User registration, profile management
- **Connections**: Create and manage connections between users
- **Important Dates**: Track and manage important dates with notifications
- **Preferences**: Store and retrieve user preferences
- **Wishlists**: Create and manage wishlists
- **Privacy Settings**: Control user data visibility and sharing preferences
- **Reference Data**: Manage common reference data used throughout the application

## 🛠️ Technology Stack
- **Java 21**: Latest LTS version of Java
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring applications
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access using JPA with Hibernate
- **PostgreSQL**: Database for persistent storage
- **Flyway**: Database migration
- **JWT**: JSON Web Tokens for stateless authentication
- **Lombok**: Reduces boilerplate code
- **Swagger/OpenAPI**: API documentation

## 🏗️ Architecture
The application follows a modular architecture:
- **Module-Based Design**: Functionality organized into domain-specific modules
- **Controller Layer**: RESTful API endpoints
- **Service Layer**: Business logic implementation with interfaces for better testability
- **Repository Layer**: Data access
- **Entity Layer**: JPA entities representing database tables
- **DTO Layer**: Data Transfer Objects for API communication
- **Security Layer**: Authentication and authorization

## 🚀 Getting Started
### Prerequisites
- JDK 21
- Maven
- Docker (optional, for containerization)
- PostgreSQL

### Environment Setup
1. Clone the repository:
```bash 
git clone [https://github.com/Zedramcak/rememberHun-backend.git](https://github.com/Zedramcak/rememberHun-backend.git) 
cd rememberHun-backend
```
2. Configure environment variables:
   - Create a file based on the example provided in `.env`
   - Set your database credentials and JWT secret

3. Build the application:
```bash 
./mvnw clean package
```
### Running Locally
#### Using Maven
```bash 
./mvnw spring-boot:run -Dspring.profiles.active=dev
```
#### Using Docker
```bash 
docker-compose -f docker-compose.dev.yml up -d
```
### Deployment
For production deployment:
```bash 
./dev.sh # Script to build and deploy to production
```
Or using Docker:
```bash 
docker-compose -f docker-compose.prod.yml up -d
```
## 📊 API Documentation
API documentation is available via Swagger UI:
- Development: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
## 🧪 Testing
Run tests with:
```bash 
./mvnw test
```
## 📂 Project Structure
```
src 
├── main 
│ ├── java 
│ │ └── cz.adamzrcek 
│ │ ├── modules # Modular organization 
│ │ │ ├── auth # Authentication module 
│ │ │ ├── connection # User connections module 
│ │ │ ├── importantdate # Important dates module 
│ │ │ ├── preference # User preferences module 
│ │ │ ├── privacy # Privacy settings module 
│ │ │ ├── referencedata # Reference data module 
│ │ │ ├── shared # Shared components 
│ │ │ │ ├── config # Configuration classes 
│ │ │ │ ├── controller # Common controllers 
│ │ │ │ ├── dtos # Shared DTOs 
│ │ │ │ ├── exception # Global exception handling 
│ │ │ │ └── startup # Startup initialization 
│ │ │ ├── user # User management module 
│ │ │ └── wishlist # Wishlist module 
│ │ └── RememberHunApplication # Application entry point 
│ └── resources 
│ ├── db.migration # Flyway database migrations 
│ └── application.properties # Application configuration 
└── test # Test classes
```
## 🔒 Security
The application uses JWT tokens for authentication:
- Access tokens for short-term authentication
- Refresh tokens for obtaining new access tokens
- Token blacklisting for logout functionality

## 🛣️ Roadmap
- [ ] Implement email notifications for important dates
- [ ] Implement rate limiting
- [ ] Add metrics and monitoring

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors
- **Adam Zrcek** - _Initial work_

Made with ❤️ by Adam Zrcek
