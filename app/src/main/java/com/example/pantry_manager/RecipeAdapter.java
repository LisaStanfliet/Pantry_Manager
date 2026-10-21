package com.example.pantry_manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    private final Context context;

    private final List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());

        StringBuilder summary = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (summary.length() > 0) {
                summary.append(", ");
            }
            summary.append(ingredient.getName());
        }

        if (summary.length() == 0) {
            holder.summary.setText("No ingredients");
        }else {
            holder.summary.setText("Needs:" + summary);
        }

        holder.viewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeViewActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView summary;
        Button viewBtn;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipeName);
            summary = itemView.findViewById(R.id.recipeSummary);
            viewBtn = itemView.findViewById(R.id.viewBtn);
        }
    }

}
