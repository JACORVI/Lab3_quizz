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

public class QuizActivity extends AppCompatActivity implements NetworkUtils.FetchQuestionsTask.OnTaskCompleted {

    private TextView timerTextView;
    private TextView questionCountTextView;
    private TextView categoryTextView;
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private RadioButton trueRadioButton;
    private RadioButton falseRadioButton;
    private Button nextButton;

    private int categoryId;
    private String categoryName;
    private int quantity;
    private String difficulty;
    private int totalSeconds;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize UI components
        timerTextView = findViewById(R.id.timerTextView);
        questionCountTextView = findViewById(R.id.questionCountTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        trueRadioButton = findViewById(R.id.trueRadioButton);
        falseRadioButton = findViewById(R.id.falseRadioButton);
        nextButton = findViewById(R.id.nextButton);

        // Get extras from intent
        categoryId = getIntent().getIntExtra("CATEGORY_ID", 9);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        quantity = getIntent().getIntExtra("QUANTITY", 5);
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        totalSeconds = getIntent().getIntExtra("TOTAL_SECONDS", 50);

        // Set category text
        categoryTextView.setText("Categoría: " + categoryName);

        // Setup next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answersRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(QuizActivity.this, "Debes seleccionar una respuesta", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveAnswer();
                moveToNextQuestion();
            }
        });

        // Fetch questions from API
        fetchQuestions();
    }

    private void fetchQuestions() {
        // Show loading state
        questionTextView.setText("Cargando preguntas...");
        answersRadioGroup.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(false);

        // Fetch questions
        NetworkUtils.FetchQuestionsTask task = new NetworkUtils.FetchQuestionsTask(this);
        task.execute(String.valueOf(quantity), String.valueOf(categoryId), difficulty);
    }

    @Override
    public void onTaskCompleted(List<Question> questionList) {
        if (questionList != null && !questionList.isEmpty()) {
            questions = questionList;

            // Start the quiz
            startTimer();
            showQuestion(0);

            // Show question UI
            answersRadioGroup.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
        } else {
            Toast.makeText(this, "No se pudieron cargar las preguntas. Intente nuevamente.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timerTextView.setText(String.format("Tiempo: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Tiempo: 00:00");
                endQuiz();
            }
        };

        countDownTimer.start();
        timerRunning = true;
    }

    private void showQuestion(int index) {
        if (index < questions.size()) {
            Question question = questions.get(index);

            // Update question count text
            questionCountTextView.setText("Pregunta " + (index + 1) + " de " + questions.size());

            // Update question text (use Html.fromHtml to decode HTML entities)
            questionTextView.setText(Html.fromHtml(question.getQuestion()));

            // Clear radio selection
            answersRadioGroup.clearCheck();

            // If the question was already answered, select the user's answer
            if (question.isAnswered()) {
                if (question.isUserAnswer()) {
                    trueRadioButton.setChecked(true);
                } else {
                    falseRadioButton.setChecked(true);
                }
            }
        }
    }

    private void saveAnswer() {
        if (currentQuestionIndex < questions.size()) {
            boolean answer = trueRadioButton.isChecked();
            questions.get(currentQuestionIndex).setUserAnswer(answer);
        }
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            // Show next question
            showQuestion(currentQuestionIndex);
        } else {
            // End of quiz
            endQuiz();
        }
    }

    private void endQuiz() {
        if (timerRunning && countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }

        // Count correct, incorrect, and unanswered questions
        int correct = 0;
        int incorrect = 0;
        int unanswered = 0;

        for (Question question : questions) {
            if (!question.isAnswered()) {
                unanswered++;
            } else if (question.isCorrect()) {
                correct++;
            } else {
                incorrect++;
            }
        }

        // Start stats activity
        Intent intent = new Intent(QuizActivity.this, StatsActivity.class);
        intent.putExtra("CORRECT", correct);
        intent.putExtra("INCORRECT", incorrect);
        intent.putExtra("UNANSWERED", unanswered);
        intent.putExtra("TOTAL", questions.size());
        startActivity(intent);

        // Finish this activity
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Do not cancel timer when app goes to background
    }


}