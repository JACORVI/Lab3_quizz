package com.example.lab_quizz;

import java.util.Collections;
import java.util.List;

public class Pregunta {
    private String enunciado;
    private List<String> opciones;
    private String respuestaCorrecta;

    public Pregunta(String enunciado, List<String> opciones, String respuestaCorrecta) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        Collections.shuffle(this.opciones); // Mezclar opciones al crear
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public boolean esCorrecta(String seleccion) {
        return respuestaCorrecta.equals(seleccion);
    }
}
