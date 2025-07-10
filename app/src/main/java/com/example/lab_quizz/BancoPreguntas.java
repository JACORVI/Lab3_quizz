package com.example.lab_quizz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BancoPreguntas {

    public static List<Pregunta> obtenerPreguntasPorTema(String tema) {
        List<Pregunta> preguntas = new ArrayList<>();

        switch (tema) {
            case "Redes":
                preguntas.add(new Pregunta("¿Qué protocolo se utiliza para cargar páginas web?",
                        Arrays.asList("HTTP", "FTP", "SMTP"), "HTTP"));
                preguntas.add(new Pregunta("¿Cuál de estas es una dirección IP privada?",
                        Arrays.asList("192.168.1.1", "8.8.8.8", "13.107.21.200"), "192.168.1.1"));
                preguntas.add(new Pregunta("¿Qué dispositivo conecta redes diferentes y dirige el tráfico?",
                        Arrays.asList("Router", "Switch", "Hub"), "Router"));
                preguntas.add(new Pregunta("¿Qué significa DNS?",
                        Arrays.asList("Domain Name System", "Data Network Server", "Digital Name Service"), "Domain Name System"));
                preguntas.add(new Pregunta("¿Qué tipo de red cubre un área pequeña, como una oficina?",
                        Arrays.asList("LAN", "WAN", "MAN"), "LAN"));
                break;

            case "Ciberseguridad":
                preguntas.add(new Pregunta("¿Qué es un Ransomware?",
                        Arrays.asList("Malware que cifra datos", "Antivirus", "Herramienta de respaldo"), "Malware que cifra datos"));
                preguntas.add(new Pregunta("¿Cuál es el objetivo del phishing?",
                        Arrays.asList("Robar información personal", "Optimizar redes", "Analizar tráfico"), "Robar información personal"));
                preguntas.add(new Pregunta("¿Qué protocolo cifra las comunicaciones web?",
                        Arrays.asList("HTTPS", "HTTP", "FTP"), "HTTPS"));
                preguntas.add(new Pregunta("¿Qué algoritmo de cifrado es asimétrico y se usa comúnmente para firmas digitales?",
                        Arrays.asList("RSA", "AES", "SHA-256"), "RSA"));
                preguntas.add(new Pregunta("¿Para qué sirve un firewall?",
                        Arrays.asList("Filtrar tráfico de red", "Acelerar internet", "Detectar virus"), "Filtrar tráfico de red"));
                break;

            case "Microondas":
                preguntas.add(new Pregunta("¿En qué rango de frecuencias suelen operar las redes Wi-Fi?",
                        Arrays.asList("2.4 GHz - 5 GHz", "100 MHz", "900 MHz"), "2.4 GHz - 5 GHz"));
                preguntas.add(new Pregunta("¿Qué problema es común en enlaces de microondas?",
                        Arrays.asList("Línea de vista", "Pérdida de datos", "Interferencia acústica"), "Línea de vista"));
                preguntas.add(new Pregunta("¿Qué es la zona de Fresnel en microondas?",
                        Arrays.asList("Región de propagación", "Zona de seguridad", "Área de ruido térmico"), "Región de propagación"));
                preguntas.add(new Pregunta("¿Qué ventaja tienen las comunicaciones por microondas?",
                        Arrays.asList("Alta capacidad", "Costo bajo", "Alto retraso"), "Alta capacidad"));
                preguntas.add(new Pregunta("¿Qué dispositivo se usa para enfocar señales de microondas?",
                        Arrays.asList("Antena parabólica", "Router", "Modem"), "Antena parabólica"));
                break;
        }

        Collections.shuffle(preguntas); // Aleatorizar preguntas
        return preguntas;
    }
}
