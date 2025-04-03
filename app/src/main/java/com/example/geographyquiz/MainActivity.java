package com.example.geographyquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView questionText;
    private Button option1, option2, option3, option4;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int currentQuestionIndex = 0;
    private Question[] questions = {
            new Question("What is the capital of France?", "Paris", "Berlin", "Madrid", "Rome", 1),
            new Question("Which country has the largest population?", "India", "USA", "China", "Brazil", 3)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        questionText = findViewById(R.id.questionText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        setOptionsStyle();
        loadNextQuestion();
    }

    private void setOptionsStyle() {
        option1.setBackgroundColor(Color.parseColor("#800080")); // Purple
        option2.setBackgroundColor(Color.WHITE);
        option3.setBackgroundColor(Color.parseColor("#800080")); // Purple
        option4.setBackgroundColor(Color.WHITE);
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            Question q = questions[currentQuestionIndex];
            questionText.setText(q.getQuestion());
            option1.setText(q.getOption1());
            option2.setText(q.getOption2());
            option3.setText(q.getOption3());
            option4.setText(q.getOption4());

            option1.setOnClickListener(v -> checkAnswer(1));
            option2.setOnClickListener(v -> checkAnswer(2));
            option3.setOnClickListener(v -> checkAnswer(3));
            option4.setOnClickListener(v -> checkAnswer(4));
        } else {
            showResults();
        }
    }

    private void checkAnswer(int selectedAnswer) {
        if (selectedAnswer == questions[currentQuestionIndex].getCorrectAnswer()) {
            correctAnswers++;
        } else {
            incorrectAnswers++;
        }
        currentQuestionIndex++;
        loadNextQuestion();
    }

    private void showResults() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        startActivity(intent);
        finish();
    }
}
