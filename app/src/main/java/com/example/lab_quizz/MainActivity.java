package com.example.lab_quizz;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private EditText quantityEditText;
    private RadioGroup difficultyRadioGroup;
    private Button checkConnectionButton;
    private Button startButton;

    private boolean isConnected = false;

    // Category mapping (Name -> ID)
    private Map<String, Integer> categoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        categorySpinner = findViewById(R.id.categorySpinner);
        quantityEditText = findViewById(R.id.quantityEditText);
        difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup);
        checkConnectionButton = findViewById(R.id.checkConnectionButton);
        startButton = findViewById(R.id.startButton);

        // Set up category spinner
        setupCategorySpinner();

        // Setup check connection button
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });

        // Setup start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    startTrivia();
                }
            }
        });
    }

    private void setupCategorySpinner() {
        // Initialize category mapping
        categoryMap.put("Cultura General", 9);  // General Knowledge
        categoryMap.put("Libros", 10);          // Books
        categoryMap.put("Películas", 11);       // Film
        categoryMap.put("Música", 12);          // Music
        categoryMap.put("Computación", 18);     // Computers
        categoryMap.put("Matemática", 19);      // Mathematics
        categoryMap.put("Deportes", 21);        // Sports
        categoryMap.put("Historia", 23);        // History

        // Create adapter
        String[] categories = categoryMap.keySet().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void checkInternetConnection() {
        isConnected = NetworkUtils.isNetworkAvailable(this);

        if (isConnected) {
            Toast.makeText(this, "Conexión a Internet correcta!", Toast.LENGTH_SHORT).show();
            startButton.setEnabled(true);
        } else {
            Toast.makeText(this, "No hay conexión a Internet. Revise su conexión e intente nuevamente.", Toast.LENGTH_SHORT).show();
            startButton.setEnabled(false);
        }
    }

    private boolean validateInputs() {
        // Validate quantity
        String quantityStr = quantityEditText.getText().toString().trim();
        if (TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Debe ingresar una cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "La cantidad debe ser un número positivo", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La cantidad debe ser un número válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate difficulty selection
        int selectedRadioId = difficultyRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {
            Toast.makeText(this, "Debe seleccionar una dificultad", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check internet connection again
        if (!isConnected) {
            Toast.makeText(this, "No hay conexión a Internet. Por favor verifique primero", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void startTrivia() {
        // Get selected category
        String categoryName = categorySpinner.getSelectedItem().toString();
        int categoryId = categoryMap.get(categoryName);

        // Get quantity
        int quantity = Integer.parseInt(quantityEditText.getText().toString().trim());

        // Get difficulty
        String difficulty;
        int selectedRadioId = difficultyRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioId);
        String difficultyLabel = selectedRadioButton.getText().toString();

        if (difficultyLabel.equals("Fácil")) {
            difficulty = "easy";
        } else if (difficultyLabel.equals("Medio")) {
            difficulty = "medium";
        } else {
            difficulty = "hard";
        }

        // Calculate time based on difficulty and quantity
        int secondsPerQuestion;
        if (difficulty.equals("easy")) {
            secondsPerQuestion = 5;
        } else if (difficulty.equals("medium")) {
            secondsPerQuestion = 7;
        } else {
            secondsPerQuestion = 10;
        }

        int totalSeconds = quantity * secondsPerQuestion;

        // Start quiz activity
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("CATEGORY_ID", categoryId);
        intent.putExtra("CATEGORY_NAME", categoryName);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("DIFFICULTY", difficulty);
        intent.putExtra("TOTAL_SECONDS", totalSeconds);
        startActivity(intent);
    }
}