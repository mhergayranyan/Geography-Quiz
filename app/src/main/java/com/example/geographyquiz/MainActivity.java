package com.example.geographyquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionText, timerText;
    private Button[] optionButtons = new Button[4];

    private List<Question> questionList;
    private int currentIndex = 0;
    private int correct = 0;
    private int incorrect = 0;
    private CountDownTimer countDownTimer;
    private static final long TIME_PER_QUESTION = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        timerText = findViewById(R.id.timerText);
        optionButtons[0] = findViewById(R.id.option1);
        optionButtons[1] = findViewById(R.id.option2);
        optionButtons[2] = findViewById(R.id.option3);
        optionButtons[3] = findViewById(R.id.option4);

        questionList = getQuestions();
        showNextQuestion();



    }

    private List<Question> getQuestions() {
        return new ArrayList<>(Arrays.asList(
                new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2),
                new Question("Which country has this flag 🇯🇵?", new String[]{"China", "Japan", "South Korea", "Thailand"}, 1),
                new Question("What is the capital of Canada?", new String[]{"Toronto", "Ottawa", "Vancouver", "Montreal"}, 1),
                new Question("Which country has this flag 🇧🇷?", new String[]{"Argentina", "Brazil", "Mexico", "Chile"}, 1),
                new Question("What is the capital of Australia?", new String[]{"Sydney", "Canberra", "Melbourne", "Brisbane"}, 1),
                new Question("Which country has this flag 🇩🇪?", new String[]{"Germany", "Belgium", "Austria", "Switzerland"}, 0),
                new Question("What is the capital of Italy?", new String[]{"Rome", "Milan", "Naples", "Venice"}, 0),
                new Question("Which country has this flag 🇰🇷?", new String[]{"China", "South Korea", "Japan", "Philippines"}, 1),
                new Question("What is the capital of Russia?", new String[]{"Moscow", "St. Petersburg", "Kazan", "Sochi"}, 0),
                new Question("Which country has this flag 🇺🇸?", new String[]{"Canada", "UK", "USA", "Australia"}, 2)
        ));
    }

    private void showNextQuestion() {
        if (currentIndex >= questionList.size()) {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("correct", correct);
            intent.putExtra("incorrect", incorrect);
            startActivity(intent);
            finish();
            return;
        }

        Question currentQuestion = questionList.get(currentIndex);
        questionText.setText(currentQuestion.getQuestionText());
        String[] options = currentQuestion.getOptions();

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[i]);
            int finalI = i;
            optionButtons[i].setOnClickListener(view -> {
                if (finalI == currentQuestion.getCorrectIndex()) {
                    correct++;
                } else {
                    incorrect++;
                }
                countDownTimer.cancel();
                currentIndex++;
                showNextQuestion();
            });
        }

        startTimer();
    }

    private void startTimer() {
        timerText.setText("10");
        countDownTimer = new CountDownTimer(TIME_PER_QUESTION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                incorrect++;
                currentIndex++;
                showNextQuestion();
            }
        }.start();
    }
}
