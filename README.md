
# TaskFlow

## Overview

TaskFlow is a comprehensive **Task Management System** built with **Spring Boot** that enables efficient project and task management across teams. The application features role-based access control, email notifications, and a robust REST API for seamless task tracking and collaboration.

## Key Features

### **Role-Based Access Control**
- **Admin**: Full system access, user management, and role assignment
- **Manager**: Project creation, task assignment, and team oversight  
- **Developer**: Task execution, status updates, and personal task management

### **Project Management**
- Create and manage multiple projects
- Track project progress with completion percentages
- View project-specific task assignments
- Get comprehensive project analytics

### **Task Management**
- Create, update, and delete tasks
- Priority levels: **LOW**, **MEDIUM**, **HIGH**
- Status tracking: **OPEN**, **IN_PROGRESS**, **COMPLETED**
- Assign tasks to team members
- Personal task dashboard for users

### **Email Notifications**
- Welcome emails for new users
- Task assignment notifications
- Automated email alerts for task updates

### **Security Features**
- BCrypt password encryption
- Method-level security with `@PreAuthorize`
- Session management and user context tracking

## Technology Stack

### **Backend Framework**
- **Spring Boot 3.5.4** - Core application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Mail** - Email functionality

### **Database**
- **MySQL** - Primary database with full JPA integration
- **Hibernate** - ORM for entity management

### **Documentation & Testing**
- **SpringDoc OpenAPI** - Automatic API documentation
- **Spring Boot Test** - Comprehensive testing framework

### **Build & Deployment**
- **Maven** - Dependency management and build automation
- **Java 17** - Runtime environment

## Quick Start

### Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **MySQL 8.0+**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/chinthan22222/TaskFlow.git
   cd TaskFlow
   ```

2. **Configure Database**
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/task_flow
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Configure Email (Optional)**
   Update email settings in `application.properties`:
   ```properties
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - API Base URL: `http://localhost:8080`
   - API Documentation: `http://localhost:8080/swagger-ui/index.html`

### Default Admin Account
The application automatically creates an admin user on startup:
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@taskflow.local`

## API Documentation

### **User Management**
| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/users/register` | Register new user | ADMIN |
| GET | `/api/users/` | Get all users | ADMIN, MANAGER |
| GET | `/api/users/{id}` | Get user by ID | ADMIN, MANAGER |
| PUT | `/api/users/{id}/role` | Assign user role | ADMIN |
| GET | `/api/users/profile` | Get current user profile | ALL |
| GET | `/api/users/dashboard` | Get user dashboard | ALL |

### **Project Management**
| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/projects/` | Create new project | ADMIN, MANAGER |
| GET | `/api/projects/` | Get all projects | ALL |
| GET | `/api/projects/{id}` | Get project by ID | ALL |
| GET | `/api/projects/{id}/tasks` | Get project tasks | ALL |
| GET | `/api/projects/progress` | Get project progress | ALL |
| PUT | `/api/projects/{id}` | Update project | ADMIN, MANAGER |
| DELETE | `/api/projects/{id}` | Delete project | ADMIN, MANAGER |

### **Task Management**
| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/tasks/` | Create new task | ADMIN, MANAGER |
| GET | `/api/tasks/` | Get all tasks | ALL |
| GET | `/api/tasks/me` | Get my assigned tasks | ALL |
| PUT | `/api/tasks/{taskId}/assign/{userId}` | Assign task to user | ADMIN, MANAGER |
| PUT | `/api/tasks/{taskId}/status` | Update task status | ALL |
| PUT | `/api/tasks/update` | Update task details | ALL |
| DELETE | `/api/tasks/delete/{taskId}` | Delete task | ADMIN, MANAGER |

## Project Architecture

### **Entity Relationships**
```
User (1) ←→ (N) Task
Project (1) ←→ (N) Task
```

### **Core Entities**

#### **User Entity**
- `id` (Primary Key)
- `userName` (Unique)
- `email` (Unique)
- `password` (BCrypt Encoded)
- `role` (ADMIN/MANAGER/DEVELOPER)

#### **Project Entity**
- `id` (Primary Key)
- `name` (Project Name)
- `description` (Project Details)
- `tasks` (One-to-Many Relationship)

#### **Task Entity**
- `id` (Primary Key)
- `title` (Task Title)
- `description` (Task Details)
- `status` (OPEN/IN_PROGRESS/COMPLETED)
- `priority` (LOW/MEDIUM/HIGH)
- `assignee` (User Reference)
- `project` (Project Reference)

## Configuration

### **Database Configuration**
The application uses MySQL with automatic schema generation:
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### **Security Configuration**
- **Password Encoding**: BCrypt with strength 12
- **Authentication**: HTTP Basic Authentication
- **Authorization**: Method-level security with role-based access

### **Email Configuration**
Supports Gmail SMTP with TLS encryption:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Development

### **Data Initialization**
The application includes a `DataInitializer` that creates:
- Sample project with demo tasks
- Default admin user
- Basic application data for testing

### **Exception Handling**
Custom exceptions for better error management:
- `UserNotFoundException`
- `TaskNotFoundException`
- `ProjectNotFoundException`

### **Service Layer Architecture**
- **UserServices**: User management and authentication
- **ProjectServices**: Project lifecycle management
- **TaskServices**: Task operations and assignments
- **EmailService**: Notification system

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request



**TaskFlow** - Streamline your project management and boost team productivity! 

