package com.example.lab_quizz;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    private TextView totalQuestionsTextView;
    private TextView correctAnswersTextView;
    private TextView incorrectAnswersTextView;
    private TextView unansweredTextView;
    private Button playAgainButton;
    private CardView unansweredCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize UI components
        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView);
        unansweredTextView = findViewById(R.id.unansweredTextView);
        unansweredCard = findViewById(R.id.unansweredCard);
        playAgainButton = findViewById(R.id.playAgainButton);

        // Get data from intent
        int correct = getIntent().getIntExtra("CORRECT", 0);
        int incorrect = getIntent().getIntExtra("INCORRECT", 0);
        int unanswered = getIntent().getIntExtra("UNANSWERED", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);

        // Set text values
        totalQuestionsTextView.setText("Total de preguntas: " + total);
        correctAnswersTextView.setText("Respuestas correctas: " + correct);
        incorrectAnswersTextView.setText("Respuestas incorrectas: " + incorrect);

        // Show unanswered card only if there are unanswered questions
        if (unanswered > 0) {
            unansweredTextView.setText("Preguntas sin responder: " + unanswered);
            unansweredCard.setVisibility(View.VISIBLE);
        } else {
            unansweredCard.setVisibility(View.GONE);
        }

        // Setup play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to main activity
                Intent intent = new Intent(StatsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


}