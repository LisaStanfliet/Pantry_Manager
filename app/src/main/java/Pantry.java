import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.*;

import com.example.pantry_manager.R;

//Main screen: it loads the saved pantry and opens other screens
public class Pantry extends AppCompatActivity {
    private RecyclerView recyclerPantry;
    private Database database;
    private TextView pantryHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        database = new Database(this);
        recyclerPantry = findViewById(R.id.recyclerPantry);
        pantryHint = findViewById(R.id.pantryHint);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        //Button and listeners
        Button addBtn = findViewById(R.id.addItemBtn);
        Button suggestBtn = findViewById(R.id.suggestBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        addBtn.setOnClickListener(v -> startActivity(new Intent(this, ItemsFormActivity.class)));
        suggestBtn.setOnClickListener(v -> startActivity(new Intent(this, RecipeFormActivity.class)));
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        //Refresh the pantry on resume
        super.onResume();
        refreshPantry();
    }

    //Refresh the pantry list
    private void refreshPantry() {
        List<Items> items = database.getAllItems();
        pantryHint.setText(items.isEmpty() ? "Your pantry is empty" : items.size() + " items in your pantry");
        recyclerPantry.setAdapter(new PantryAdapter(this,items,database));
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

}
