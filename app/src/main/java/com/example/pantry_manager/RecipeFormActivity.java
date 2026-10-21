package com.example.pantry_manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.*;

public class RecipeFormActivity extends AppCompatActivity {
    private Database database;
    private RecyclerView recyclerRecipes;
    private TextView recipeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_form);

        database = new Database(this);
        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        recipeMsg = findViewById(R.id.recipeMsg);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));

        Button back = findViewById(R.id.backPantry);
        back.setOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        List<Recipe> allRecipes = database.getAllRecipes();
        List<Items> items = database.getAllItems();
        List<Recipe> matches = RecipeMatcher.findMatches(allRecipes, items);

        if(matches.isEmpty()){
            recipeMsg.setText("No recipes found");
        }else{
            recipeMsg.setText(matches.size() + " recipes found");
        }
        recyclerRecipes.setAdapter(new RecipeAdapter(this,matches));

    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }


}
