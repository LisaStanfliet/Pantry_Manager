package com.example.pantry_manager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ItemViewHolder> {
    private final Context context;
    private final List<Items> items;
    private final Database database;

    public PantryAdapter(Context context, List<Items> items, Database database) {
        this.context = context;
        this.items = items;
        this.database = database;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Items item = items.get(position);
        holder.name.setText(item.getName());
        holder.quantity.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());
        holder.expiryDate.setText(item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "No expiry date" : "Expiry date: " + item.getExpiryDate());

        //Open form to edit pantry item
        holder.edit.setOnClickListener(v -> {
            //Pass the ingredient ID to the form activity
            Intent intent = new Intent(context, ItemsFormActivity.class);
            intent.putExtra("ITEM_ID", item.getId());
            context.startActivity(intent);
        });

        //Delete after confirming
        holder.delete.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setTitle("Delete Item?")
                        .setMessage("Are you sure you want to delete this pantry item?")
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Delete", (dialog, which) -> {
                            boolean deleted = database.deleteItem(item.getId());
                            //Checks if item still has a valid position in the list
                            if (deleted) {
                                int currentPosition = holder.getAdapterPosition();

                                if (currentPosition != RecyclerView.NO_POSITION) {
                                    items.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);
                                }
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error deleting item", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name, quantity, expiryDate;
        Button edit, delete;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            expiryDate = itemView.findViewById(R.id.expiryDate);
            edit = itemView.findViewById(R.id.editBtn);
            delete = itemView.findViewById(R.id.deleteBtn);
        }
    }
}