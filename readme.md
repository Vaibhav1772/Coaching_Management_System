# Coaching Management System

The Coaching Management System is an Android application developed using Java, with a backend powered by Spring Boot, Spring Data JPA and MySQL. The system is designed to manage various coaching institute activities, including student enrollment, attendance tracking, exam management, doubt resolution, and payment processing. It also includes a chatbot for resolving student doubts using the Gemini API.

## Features

- **User Management**: Handle student and admin roles with secure authentication.
- **Attendance Tracking**: Automated attendance tracking for students.
- **Exam Management**: Create and manage exams, and track student performance.
- **Payment Processing**: Manage student payments with a secure transaction system.**(Currently in development)**
- **Doubt Resolution**: Integrated chatbot using Gemini API for doubt resolution. Unresolved doubts are assigned to instructors.
- **Notifications**: Send and manage notifications to users.
- **Microservices Architecture**: The application is designed with a microservices architecture for scalability and maintainability.

## Technology Stack

- **Frontend**: Android (Java)
- **Backend**: Spring Boot,Java
- **Database**: MySQL
- **APIs**: Gemini API for chatbot integration
- **Libraries**:
  - Retrofit for API calls
  - Lombok for cleaner code
  - Spring Data JPA for database interaction
  - RecyclerView for chat interface
  - Firebase for Real-time Storage
  - Picasso Library for Image Manipulation
  - Bcrypt for Password-Hashing
  - AnyChart for Charts
- **Editor** : `IntelliJ Idea Ultimate Edition` for better support to develop Spring proejcts.

## Installation

### Prerequisites

- Java Development Kit (JDK)
- Android Studio
- MySQL
- Spring Boot and Spring Data JPA (Hibernate)

### Backend Setup

1. Clone the repository.
2. Navigate to the Spring Boot backend directory.
3. Configure the `application.properties` with your MySQL database credentials.
4. Use the provided database script in `New Database DDL Script.txt` to create the necessary tables and schema in your MySQL database.
5. - Run the application:
        ```bash
        ./mvnw spring-boot:run
    - `(Prefer this method)` Run the application in IntelliJ directly for better Support.
### Android Application 
1.  Replace the Retrofit URL with your Spring Backend URL.
2.  Get your Gemini API Key from `Google AI Studio` to use the Gemini Support.
3.  Create your Firebase Account to access the Real-time Storage Support for images.
4.  Build the `*.apk` and use it.


### Contribution

This repostiory is open to contribute. Feel free to enhance it and provide quality and security improvements :).

