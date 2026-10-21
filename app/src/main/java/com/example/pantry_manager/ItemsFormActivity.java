package com.example.pantry_manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ItemsFormActivity extends AppCompatActivity {
    private EditText editName,editQuantity, editExpiryDate;
    private Spinner spinnerUnit;
    private Database database;

    //the -1 means that there is a new ingredient being added.
    private int itemId = -1;

    private final String[] units = { "pieces", "g","kg", "ml", "l","cups", "tbsp", "tsp"};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_form);

        //Initialize database
        database = new Database(this);

        //Connect Java to XML
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        TextView title = findViewById(R.id.title);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);

        //Populate the spinner with units
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        //Check if existing ingredient is being edited
        itemId = getIntent().getIntExtra("ITEM_ID", -1);
        if (itemId != -1) {
            title.setText("Edit Item");
            //Load ingredient details
            loadIngredient();
            saveBtn.setText("Update");
        } else {
            title.setText("Add Item");
            saveBtn.setText("Save");
        }

        //Button listeners
        saveBtn.setOnClickListener(v -> saveIngredient());
        cancelBtn.setOnClickListener(v -> finish());
    }

    //Load existing ingredient details
    private void loadIngredient() {
        Items item = database.getItem(itemId);
        if (item == null) {
            Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        editName.setText(item.getName());
        editQuantity.setText(String.valueOf(item.getQuantity()));
        editExpiryDate.setText(item.getExpiryDate()== null ? "" : item.getExpiryDate());

        //Select unit from ingredient
        for (int i = 0; i < units.length; i++) {
            if (units[i].equalsIgnoreCase(item.getUnit())) {
                spinnerUnit.setSelection(i);
                break;
            }

        }
    }

    private void saveIngredient() {
        String name = editName.getText().toString();
        String quantityText = editQuantity.getText().toString();
        String expiryDate = editExpiryDate.getText().toString();
        String unit = spinnerUnit.getSelectedItem().toString();

        //Validation
        if (name.isEmpty()) {
            editName.setError("Ingredient name is required");
            return;
        }
        if (quantityText.isEmpty()) {
            editQuantity.setError("Quantity is required");
            return;
        }

        final double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Enter valid quantity");
            editQuantity.requestFocus();
            return;
        }
        if (quantity <= 0) {
            editQuantity.setError("Quantity must be greater than 0");
            return;
        }
        if (!expiryDate.isEmpty() && !expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            editExpiryDate.setError("Use YYYY-MM-DD format");
            editExpiryDate.requestFocus();
            return;
        }
        if (spinnerUnit.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Please select a unit", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (itemId == -1) {
            success = database.addItem(name, quantity,  unit, expiryDate);
        }else {
            success = database.updateItem(itemId, name, quantity, unit, expiryDate);
        }

        Toast.makeText(this,success ? "Ingredient saved" : "Error saving ingredient", Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (database !=null){
            database.close();
        }

        super.onDestroy();
    }
}
