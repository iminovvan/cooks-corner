# CooksCorner
## Overview

The Recipe Management Application is a Spring Boot-based application that allows users to manage and share recipes. Users can register, login, create, like, and save recipes. Additionally, users can follow other users, and all these actions are secured with JWT-based authentication.

## Features

-   **User Authentication**: Users can register, login, and logout.
-   **Recipe Management**: Users can create, update, and delete recipes.
-   **Search and Filter**: Users can search for recipes by title and filter recipes by category.
-   **Recipe Interactions**: Users can like, save, and comment on recipes.
-   **User Interactions**: Users can follow and unfollow other users.
-   **JWT-based Security**: Secure authentication and authorization using JWT tokens.

## Technologies Used

-   **Spring Boot**: For building the backend services.
-   **JWT**: For secure authentication and authorization.
-   **Swagger**: For API documentation.
-   **JPA/Hibernate**: For database interactions.
-   **Cloudinary**: For storing images.
-   **MySQL**: For the relational database.
-   **ModelMapper**: For object mapping.

## Installation

### Prerequisites

-   Java 17
-   Maven
-   MySQL
-   Cloudinary account for image storage

### Steps

1.  Clone the repository:

    `git clone https://github.com/iminovvan/cooks-corner.git
    cd cooks-corner`

2.  Install dependencies:

    `mvn clean install`

3.  Set up the database:

    `CREATE DATABASE cookscorner;`


## Configuration

Configure the application by setting the following environment variables:

-   `DB_URL`: URL of the MySQL database
-   `DB_USERNAME`: Database username
-   `DB_PASSWORD`: Database password
-   `CLOUDINARY_CLOUD_NAME`: Cloudinary cloud name
-   `CLOUDINARY_API_KEY`: Cloudinary API key
-   `CLOUDINARY_API_SECRET`: Cloudinary API secret
-   `SECRET_KEY`: Secret key for JWT

## Usage

To run the application, use the following command:
`mvn spring-boot:run`

The application will start on `http://localhost:8080`.