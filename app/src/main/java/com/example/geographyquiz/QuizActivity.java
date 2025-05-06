package com.example.geographyquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    // UI Elements
    private TextView questionText, timerText, scoreText;
    private Button option1, option2, option3, option4;

    // Game Logic
    private String correctAnswer;
    private int score = 0;
    private int currentQuestion = 0;
    private CountDownTimer timer;

    // Settings
    private SharedPreferences prefs;
    private MediaPlayer correctSound, wrongSound;
    private Vibrator vibrator;
    // Add with your other class variables
    private FirebaseAuth mAuth;
    private DatabaseReference scoresRef;

    // Questions
    private final String[][] questions = {
            {"What is the capital of France?", "Paris", "London", "Berlin", "Madrid"},
            {"Which country has this flag?", "Japan", "China", "Thailand", "South Korea"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mAuth = FirebaseAuth.getInstance();
        scoresRef = FirebaseDatabase.getInstance().getReference("scores");

        // Initialize settings
        prefs = getSharedPreferences("QuizSettings", MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Load sounds
        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);

        // Initialize UI
        questionText = findViewById(R.id.question_text);
        timerText = findViewById(R.id.timer_text);
        scoreText = findViewById(R.id.score_text);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestion < questions.length) {
            // Reset buttons
            resetButtons();

            // Set question
            String[] q = questions[currentQuestion];
            questionText.setText(q[0]);
            correctAnswer = q[1];

            // Shuffle options
            List<String> options = Arrays.asList(q[1], q[2], q[3], q[4]);
            Collections.shuffle(options);

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));

            startTimer();
        } else {
            endQuiz();
        }
    }

    private void startTimer() {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisLeft) {
                timerText.setText("Time: " + millisLeft / 1000 + "s");
            }

            @Override
            public void onFinish() {
                handleTimeOut();
            }
        }.start();
    }

    public void onAnswerClick(View view) {
        Button selected = (Button) view;
        String answer = selected.getText().toString();

        // Cancel timer
        timer.cancel();

        // Disable all buttons
        disableButtons();

        // Check answer
        if (answer.equals(correctAnswer)) {
            handleCorrectAnswer(selected);
        } else {
            handleWrongAnswer(selected);
        }

        // Next question after delay
        new Handler().postDelayed(() -> {
            currentQuestion++;
            loadQuestion();
        }, 2000);
    }

    private void handleCorrectAnswer(Button button) {
        // Visual feedback
        button.setBackgroundColor(Color.GREEN);
        score++;
        scoreText.setText("Score: " + score);

        // Sound feedback
        if (prefs.getBoolean("sound_enabled", true)) {
            correctSound.seekTo(0);
            correctSound.start();
        }

        // Vibration feedback
        if (prefs.getBoolean("vibration", true)) {
            vibrator.vibrate(100);
        }
    }

    private void handleWrongAnswer(Button button) {
        // Visual feedback
        button.setBackgroundColor(Color.RED);
        highlightCorrectAnswer();

        // Sound feedback
        if (prefs.getBoolean("sound_enabled", true)) {
            wrongSound.seekTo(0);
            wrongSound.start();
        }

        // Vibration feedback
        if (prefs.getBoolean("vibration", true)) {
            vibrator.vibrate(new long[]{0, 100, 50, 100}, -1);
        }
    }

    private void handleTimeOut() {
        disableButtons();
        highlightCorrectAnswer();

        new Handler().postDelayed(() -> {
            currentQuestion++;
            loadQuestion();
        }, 2000);
    }

    private void highlightCorrectAnswer() {
        if (option1.getText().equals(correctAnswer)) option1.setBackgroundColor(Color.GREEN);
        if (option2.getText().equals(correctAnswer)) option2.setBackgroundColor(Color.GREEN);
        if (option3.getText().equals(correctAnswer)) option3.setBackgroundColor(Color.GREEN);
        if (option4.getText().equals(correctAnswer)) option4.setBackgroundColor(Color.GREEN);
    }

    private void disableButtons() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    private void resetButtons() {
        int defaultColor = Color.parseColor("#6200EE"); // Purple color

        option1.setBackgroundColor(defaultColor);
        option2.setBackgroundColor(defaultColor);
        option3.setBackgroundColor(defaultColor);
        option4.setBackgroundColor(defaultColor);

        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    private void endQuiz() {
        // Save score to Firebase
        if (mAuth.getCurrentUser() != null) {
            scoresRef.child(mAuth.getCurrentUser().getUid())
                    .child("score")
                    .setValue(score);
        }

        // Show results
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", questions.length);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (correctSound != null) correctSound.release();
        if (wrongSound != null) wrongSound.release();
    }
}