package com.example.lab_quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    private static final String ARG_PUNTAJE = "puntaje";

    public static ResultFragment newInstance(int puntaje) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PUNTAJE, puntaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        TextView tvResultado = view.findViewById(R.id.tvResultado);
        Button btnInicio = view.findViewById(R.id.btnInicio);
        Button btnEstadisticas = view.findViewById(R.id.btnEstadisticas);

        int puntaje = getArguments() != null ? getArguments().getInt(ARG_PUNTAJE) : 0;

        tvResultado.setText("Tu puntaje final es: " + puntaje);
        tvResultado.setTextColor(puntaje > 0 ? Color.GREEN : Color.RED);

        btnInicio.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
