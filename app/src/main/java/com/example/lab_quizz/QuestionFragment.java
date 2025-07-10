package com.example.lab_quizz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class QuestionFragment extends Fragment {

    private static final String ARG_TEMA = "tema";
    private String tema;

    private List<Pregunta> listaPreguntas;
    private int indicePregunta = 0;
    private int puntaje = 0;

    private TextView tvPregunta;
    private RadioGroup rgOpciones;
    private Button btnSiguiente;

    public QuestionFragment() {
        // Constructor público vacío requerido
    }

    public static QuestionFragment newInstance(String tema) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEMA, tema);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener argumentos
        if (getArguments() != null) {
            tema = getArguments().getString(ARG_TEMA);
        }

        // Validación del tema
        if (tema == null || tema.isEmpty()) {
            tema = "General"; // tema por defecto
        }

        try {
            // Cargar preguntas del banco
            listaPreguntas = BancoPreguntas.obtenerPreguntasPorTema(tema);

            // Verificar que hay preguntas disponibles
            if (listaPreguntas == null || listaPreguntas.isEmpty()) {
                Toast.makeText(getContext(), "No hay preguntas disponibles para: " + tema, Toast.LENGTH_LONG).show();
                if (getActivity() != null) {
                    getActivity().finish();
                }
                return;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al cargar preguntas: " + e.getMessage(), Toast.LENGTH_LONG).show();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // Inicializar vistas
        tvPregunta = view.findViewById(R.id.tvPregunta);
        rgOpciones = view.findViewById(R.id.rgOpciones);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);

        // Verificar que las vistas existen
        if (tvPregunta == null || rgOpciones == null || btnSiguiente == null) {
            Toast.makeText(getContext(), "Error: Layout del fragmento incompleto", Toast.LENGTH_LONG).show();
            if (getActivity() != null) {
                getActivity().finish();
            }
            return view;
        }

        // Cargar la primera pregunta
        cargarPregunta();

        // Configurar listeners
        rgOpciones.setOnCheckedChangeListener((group, checkedId) -> {
            btnSiguiente.setEnabled(true);
        });

        btnSiguiente.setOnClickListener(v -> {
            verificarRespuesta();
            indicePregunta++;

            if (indicePregunta < listaPreguntas.size()) {
                cargarPregunta();
                btnSiguiente.setEnabled(false);
                rgOpciones.clearCheck();
            } else {
                mostrarResultado();
            }
        });

        return view;
    }

    private void cargarPregunta() {
        // Verificar que hay preguntas y el índice es válido
        if (listaPreguntas == null || indicePregunta >= listaPreguntas.size()) {
            return;
        }

        try {
            Pregunta preguntaActual = listaPreguntas.get(indicePregunta);

            // Mostrar número de pregunta y enunciado
            String textoPregunta = "Pregunta " + (indicePregunta + 1) + " de " + listaPreguntas.size() +
                    "\n\n" + preguntaActual.getEnunciado();
            tvPregunta.setText(textoPregunta);

            // Limpiar opciones anteriores
            rgOpciones.removeAllViews();

            // Agregar nuevas opciones
            List<String> opciones = preguntaActual.getOpciones();
            if (opciones != null) {
                for (String opcion : opciones) {
                    RadioButton rb = new RadioButton(getContext());
                    rb.setText(opcion);
                    rb.setTextSize(16f);
                    rb.setPadding(16, 8, 16, 8);
                    rgOpciones.addView(rb);
                }
            }

            // Deshabilitar el botón hasta que se seleccione una opción
            btnSiguiente.setEnabled(false);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al cargar pregunta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarRespuesta() {
        try {
            int idSeleccionado = rgOpciones.getCheckedRadioButtonId();
            if (idSeleccionado == -1) {
                return; // No hay selección
            }

            RadioButton rbSeleccionado = rgOpciones.findViewById(idSeleccionado);
            if (rbSeleccionado == null) {
                return;
            }

            String respuesta = rbSeleccionado.getText().toString();

            if (listaPreguntas.get(indicePregunta).esCorrecta(respuesta)) {
                puntaje += 2;
            } else {
                puntaje -= 1; // Penalización menor por respuesta incorrecta
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al verificar respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResultado() {
        try {
            ResultFragment resultFragment = ResultFragment.newInstance(puntaje);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.quiz_fragment_container, resultFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al mostrar resultado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}