package com.example.lab_quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab_quizz.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Configurar listeners para los botones
        setupButtonListeners();

        return binding.getRoot();
    }

    private void setupButtonListeners() {
        binding.btnRedes.setOnClickListener(v -> startQuiz("Redes"));
        binding.btnCiberseguridad.setOnClickListener(v -> startQuiz("Ciberseguridad"));
        binding.btnMicroondas.setOnClickListener(v -> startQuiz("Microondas"));
    }

    private void startQuiz(String tema) {
        try {
            // Validar que el tema no esté vacío
            if (tema == null || tema.trim().isEmpty()) {
                Toast.makeText(getContext(), "Error: Tema no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar que hay preguntas disponibles para este tema
            if (BancoPreguntas.obtenerPreguntasPorTema(tema) == null ||
                    BancoPreguntas.obtenerPreguntasPorTema(tema).isEmpty()) {
                Toast.makeText(getContext(), "No hay preguntas disponibles para: " + tema, Toast.LENGTH_LONG).show();
                return;
            }

            // Crear intent y pasar el tema
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("tema", tema);

            // Mostrar mensaje de inicio
            Toast.makeText(getContext(), "Iniciando quiz de " + tema, Toast.LENGTH_SHORT).show();

            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al iniciar quiz: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}