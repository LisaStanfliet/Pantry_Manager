import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pantry_database.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the items table
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        // Create the recipes table
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, instructions TEXT NOT NULL)");
        // Create the ingredients table
        db.execSQL("CREATE TABLE ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, ingredient_name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");

        //Insert the initial recipes and their ingredients.
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //Insert pantry items, return true when successful
    public boolean addItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);
        return db.insert("items", null, values) != -1;
    }

    //Return a list of all items in the pantry sorted by name
    public List<Items> getAllItems() {
        List<Items> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM items ORDER BY name", null)) {
            while (cursor.moveToNext()) {
                items.add(new Items(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4)));
            }
        }
        return items;
    }

    //Edit an Item by ID
    public Items getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("items", new String[]{"id", "name", "quantity", "unit", "expiry_date"}, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                return new Items(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4));
            }
        }
        return null;
    }

    //Update an Item using the ID
    public boolean updateItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);
        return db.update("items", values, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    //Delete an Item using the ID
    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("items", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    //Get one recipe and all its ingredients using its ID.
    public Recipe getRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name;
        String instructions;
        //Retrieve the recipe details.
        try (Cursor cursor = db.query("recipes", new String[]{"id", "name", "instructions"}, "id=?", new String[]{String.valueOf(recipeId)}, null, null, null)) {
            //Return null if it does not exist.
            if (!cursor.moveToFirst()) {
                return null;
            }
            name = cursor.getString(1);
            instructions = cursor.getString(2);

        }
        //Retrieve the ingredients belonging to the recipe.
        List<Ingredient> ingredients = new ArrayList<>();
        try (Cursor cursor = db.query("ingredients", new String[]{"id","recipe_id", "ingredient_name", "quantity", "unit"}, "recipe_id=?", new String[]{String.valueOf(recipeId)}, null, null, "ingredient_name ASC")) {
            while (cursor.moveToNext()) {
                ingredients.add(new Ingredient(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4)));
            }
        }
        //Return the recipe and its ingredients.
        return new Recipe(recipeId, name, instructions, ingredients);
    }

    //Get all recipes, the ingredients are loaded with the recipe for matching
    public List<Recipe> getAllRecipes() {
        List<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT id FROM recipes", null)) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(0));
            }
        }
        List<Recipe> recipes = new ArrayList<>();
        for (int id : ids) {
            Recipe recipe = getRecipe(id);
            if (recipe != null) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    //Insert a recipe and all its ingredients.
    private void insertRecipes(SQLiteDatabase db, String name, String instructions, String[][] ingredients) {
        //Insert recipe first
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("instructions", instructions);
        long recipeId = db.insert("recipes", null, recipeValues);

        //Stop if inserting the recipe failed.
        if (recipeId == -1) {
            return;
        }

        //Insert ingredients
        for (String[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", ingredient[0]);
            ingredientValues.put("quantity", Double.parseDouble(ingredient[1]));
            ingredientValues.put("unit", ingredient[2]);
            db.insert("ingredients", null, ingredientValues);
        }
    }

    //initial recipe set.
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipes(db, "Pancakes", "1. Mix flour, milk and eggs until smooth. 2. Cook small portions in a lightly greased pan, turning once.",
                new String[][]{{"flour", "200", "g"}, {"milk", "250", "ml"}, {"eggs", "2", "pieces"}});
        insertRecipes(db, "Scrambled Eggs", "1. Beat eggs. 2. Melt butter in a pan. 3. Stir eggs gently until cooked.",
                new String[][]{{"eggs", "2", "pieces"}, {"butter", "10", "g"}});
        insertRecipes(db, "French Toast", "1. Beat egg and milk. 2. Dip bread into mixture. 3. Fry until golden.",
                new String[][]{{"bread", "2", "pieces"}, {"eggs", "1", "pieces"}, {"milk", "60", "ml"}});
        insertRecipes(db, "Omelette", "1. Beat eggs. 2. Pour into a warm pan. 3. Cook until set.",
                new String[][]{{"eggs", "2", "pieces"}, {"salt", "1", "g"}});
        insertRecipes(db, "Tomato Pasta", "1. Cook pasta. 2. Cook chopped tomatoes and garlic. 3. Combine and serve.",
                new String[][]{{"pasta", "100", "g"}, {"tomatoes", "150", "g"}, {"garlic", "1", "pieces"}});
        insertRecipes(db, "Mashed Potatoes", "1. Boil potatoes until soft. 2. Mash with milk and butter.",
                new String[][]{{"potatoes", "300", "g"}, {"milk", "50", "ml"}, {"butter", "15", "g"}});
        insertRecipes(db, "Banana Pancakes", "1. Mash banana. 2. Mix with flour and egg. 3. Cook small pancakes.",
                new String[][]{{"banana", "1", "pieces"}, {"flour", "100", "g"}, {"eggs", "1", "pieces"}});
        insertRecipes(db, "Fried Rice", "1. Cook rice. 2. Scramble egg in a pan. 3. Add rice and carrots and stir-fry.",
                new String[][]{{"rice", "200", "g"}, {"eggs", "1", "pieces"}, {"carrots", "50", "g"}});
        insertRecipes(db, "Garlic Bread", "1. Mix softened butter and garlic. 2. Spread on bread and toast.",
                new String[][]{{"bread", "2", "pieces"}, {"butter", "20", "g"}, {"garlic", "1", "pieces"}});
        insertRecipes(db, "Tomato Soup", "1. Cook tomatoes and onion in stock. 2. Simmer until soft and blend.",
                new String[][]{{"tomatoes", "250", "g"}, {"onion", "50", "g"}, {"vegetable stock", "250", "ml"}});
        insertRecipes(db, "Potato Wedges", "1. Cut potatoes into wedges. 2. Toss with oil and salt. 3. Bake until tender.",
                new String[][]{{"potatoes", "300", "g"}, {"oil", "15", "ml"}, {"salt", "2", "g"}});
        insertRecipes(db, "Cheese Sandwich", "1. Place cheese between bread slices. 2. Serve cold or toast.",
                new String[][]{{"bread", "2", "pieces"}, {"cheese", "40", "g"}});
        insertRecipes(db, "Vegetable Omelette", "1. Beat eggs. 2. Add chopped peppers and onion. 3. Cook until set.",
                new String[][]{{"eggs", "2", "pieces"}, {"peppers", "40", "g"}, {"onion", "30", "g"}});
        insertRecipes(db, "Banana Smoothie", "1. Blend banana, milk and yoghurt until smooth.",
                new String[][]{{"banana", "1", "pieces"}, {"milk", "200", "ml"}, {"yoghurt", "100", "g"}});
        insertRecipes(db, "Apple Porridge", "1. Cook oats in milk. 2. Stir in chopped apple and serve.",
                new String[][]{{"oats", "50", "g"}, {"apple", "1", "pieces"}, {"milk", "200", "ml"}});
        insertRecipes(db, "Egg Salad", "1. Boil and chop eggs. 2. Mix with lettuce and mayonnaise.",
                new String[][]{{"eggs", "2", "pieces"}, {"lettuce", "50", "g"}, {"mayonnaise", "20", "g"}});
        insertRecipes(db, "Rice Pudding", "1. Simmer rice and milk, stirring often. 2. Add sugar and cook until creamy.",
                new String[][]{{"rice", "60", "g"}, {"milk", "300", "ml"}, {"sugar", "20", "g"}});
        insertRecipes(db, "Cheese Pasta", "1. Cook pasta. 2. Stir in milk and cheese over low heat.",
                new String[][]{{"pasta", "100", "g"}, {"cheese", "50", "g"}, {"milk", "50", "ml"}});
        insertRecipes(db, "Vegetable Rice", "1. Cook rice. 2. Cook carrots and peas until tender. 3. Mix together.",
                new String[][]{{"rice", "150", "g"}, {"carrots", "50", "g"}, {"peas", "50", "g"}});
        insertRecipes(db, "Simple Buttered Pasta", "1. Cook pasta. 2. Drain and stir through butter.",
                new String[][]{{"pasta", "100", "g"}, {"butter", "15", "g"}});
    }
}

