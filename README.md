# Coaching Management System

A comprehensive system for managing coaching activities, student data, and test results.

## Table of Contents

- [Introduction](#Introduction)
- [Features](#Features)
- [Technologies Used](#Technologies-Used)
- [Database Setup](#Database-setup)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Contributing](#contribution)

## Introduction

The Coaching Management System is designed to streamline coaching-related tasks, including student enrollment, test management, and result tracking. It provides an efficient way to manage coaching activities and track student progress.

## Features

- Student enrollment and management
- Test creation and management
- Result tracking and analysis
- User authentication and access control

## Technologies Used

- Android (Java)
- JDBC (Java Database Connectivity) (5.x version)
- MySQL
- Bcrpyt library, Java Sql library

## Database Setup

Ensure you have MySQL installed. Create a new database and execute the SQL script provided in the `Database Script` folder to set up the necessary tables.
Note:- Fill the teachers and batches tables before hand.

## Getting Started

Follow these steps to set up and run the Coaching Management System on your local machine.

### Prerequisites

- Android Studio
- MySQL

### Installation

1. Clone the repository
   ```bash
   https://github.com/Vaibhav1772/Coaching_Management_System.git              
2. Make changes accordingly in the database connection code in DatabaseManager Class
   ```Java
    private static final String DB_URL = "jdbc:mysql://172.18.57.63:3306/my_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "xyz";
3. Build and Run the Application

### Contribution
Feel free to contribute to this project to make it more efficient!!.
