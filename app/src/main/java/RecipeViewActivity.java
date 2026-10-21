import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_manager.R;

public class RecipeViewActivity extends AppCompatActivity {
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        database = new Database(this);
        TextView name = findViewById(R.id.recipeName);
        TextView instructions = findViewById(R.id.recipeInstructions);
        TextView ingredients = findViewById(R.id.recipeIngredients);
        Button back = findViewById(R.id.backRecipe);
        back.setOnClickListener(v -> finish());

        //Get the recipe ID from the intent
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Load the recipe details
        Recipe recipe = database.getRecipe(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Display the recipe details
        name.setText(recipe.getName());
        instructions.setText(recipe.getInstructions());
        StringBuilder ingredientList = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientList
                    .append("*")
                    .append(ingredient.getName())
                    .append(" ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" \n");
        }

        if (ingredientList.length() == 0) {
            ingredientList.append("No ingredients");
        } else {
            ingredients.setText(ingredientList.toString());
        }
    }

        @Override
        protected void onDestroy() {
            database.close();
            super.onDestroy();
        }
}
