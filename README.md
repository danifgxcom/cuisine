# Cuisine Recipe Quantity Calculator - DRAFT

## Overview
The Cuisine Recipe Quantity Calculator is a Kotlin-based application designed to help you adjust recipe quantities according to the number of diners. This tool is built using Spring Boot, with data stored in MongoDB, and exposes a REST API to interact with the application easily.

## Features
- **Diner-Based Quantity Calculation**: Automatically calculate ingredient quantities for recipes based on the number of diners.
- **RESTful API**: Interact with the application via a well-defined REST API that supports creating, updating, and retrieving recipes.
- **Scalable Storage**: Data is stored in MongoDB, making the application scalable and capable of handling a large volume of recipes.

## Technologies Used
- **Kotlin**: The primary programming language for the application, ensuring concise and expressive code.
- **Spring Boot**: Provides the application framework for dependency injection, REST API capabilities, and ease of integration.
- **MongoDB**: Used as the database for storing recipes and ingredients.
- **REST API**: Exposes endpoints for accessing and managing recipes and ingredient quantities.

## How It Works
1. **Recipes Management**: Users can create and manage recipes, including defining the ingredients and specifying their quantities.
2. **Dynamic Quantity Calculation**: When specifying the number of diners, the application calculates the ingredient quantities dynamically, making it easy to adjust recipes for any number of people.
3. **Enrichment of Ingredients**: The API also enriches ingredient information by retrieving additional details like nutritional values from the database.

## Endpoints
The REST API includes the following endpoints:
- **GET /recipes**: Retrieve all recipes.
- **POST /recipes**: Add a new recipe.
- **GET /recipes/{id}**: Retrieve a specific recipe by its ID.
- **PUT /recipes/{id}**: Update an existing recipe.

## Getting Started
To run the application locally:
1. **Clone the repository**:
   ```sh
   git clone <repository-url>
   ```
2. **Navigate to the project directory**:
   ```sh
   cd cuisine
   ```
3. **Build the project**:
   ```sh
   ./gradlew build
   ```
4. **Run the application**:
   ```sh
   ./gradlew bootRun
   ```

The application will be available at `http://localhost:8080`.

## Prerequisites
- **Java 17 or later**
- **MongoDB**: Make sure MongoDB is installed and running.

## Example Usage
To calculate ingredient quantities for a recipe for 4 diners, make a GET request to:
```sh
GET http://localhost:8080/recipes/full
```

## License
This project is licensed under the MIT License.

## Contact
For more information, please contact [danifgx](https://www.linkedin.com/in/danifgx/).
