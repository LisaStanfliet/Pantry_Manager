# Pantry Manager

## Overview
Pantry Manager is an Android application designed to help users manage the ingredients they have available at home. It allows users to keep track of pantry items and their quantities, record expiry dates, and find recipes based on available ingredients.

## Features
- **Add Items:** Add ingredients to the pantry with their quantities, units, and expiry dates.
- **View Pantry:** View saved pantry items.
- **Update Items:** Edit existing ingredient details.
- **Delete Items:** Remove ingredients that are no longer needed.
- **Recipe Suggestions:** View recipes that can be prepared using available ingredients.
- **Settings:** Configure the application's expiry notification preference.

## Technologies Used
- Java
- Android Studio
- SQLite database
- XML for Android layouts
- Git and GitHub for version control

## Database
The application uses a local SQLite database named `pantry_database.db` to store pantry items, recipes, and recipe ingredients.

## Installation and Setup
1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Android Studio to synchronize the Gradle files.
4. Select an Android emulator or connect a compatible Android device.
5. Run the application.

## Project Structure
- `Database.java` — Creates and manages the SQLite database.
- `Items.java` — Represents pantry items and their properties.
- `Ingredients.java` — Represents ingredients associated with recipes, including their names, quantities, units, and recipe IDs.
- `Recipe.java` — Represents recipes and their associated ingredients.
- `ItemsFormActivity.java` — Handles adding and editing pantry items.
- `PantryActivity.java` — Displays the pantry interface.
- `PantryAdapter.java` — Displays pantry items in the RecyclerView.
- `RecipeFormActivity.java` — Displays matching recipe suggestions.
- `RecipeAdapter.java` — Displays recipe suggestions in a RecyclerView.
- `RecipeMatcher.java` — Compares recipe ingredients with pantry stock.
- `RecipeViewActivity.java` — Displays recipe details and ingredients.
- `SettingsActivity.java` — Manages application settings.

## Author
Lisa Stanfliet
402413155
Mobile App Development Assignment

## Repository
https://github.com/LisaStanfliet/Pantry_Manager
