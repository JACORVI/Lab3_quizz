package com.example.lab_quizz;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private String temaSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Obtener el tema desde el Intent
        if (getIntent() != null) {
            temaSeleccionado = getIntent().getStringExtra("tema");
        }

        // Validar que el tema no sea null
        if (temaSeleccionado == null || temaSeleccionado.isEmpty()) {
            temaSeleccionado = "General"; // tema por defecto
            Toast.makeText(this, "Tema no especificado, usando tema general", Toast.LENGTH_SHORT).show();
        }

        // Solo crear el fragmento si no existe (evita duplicados en rotación de pantalla)
        if (savedInstanceState == null) {
            try {
                QuestionFragment fragment = QuestionFragment.newInstance(temaSeleccionado);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.quiz_fragment_container, fragment)
                        .commit();
            } catch (Exception e) {
                Toast.makeText(this, "Error al cargar el quiz: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Agregar confirmación antes de salir del quiz
        super.onBackPressed();
        finish();
    }
}