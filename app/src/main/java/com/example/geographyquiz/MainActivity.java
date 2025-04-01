package com.example.geographyquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView flagImage;
    private Button option1, option2, option3, option4;
    private TextView questionText;

    // Example questions (You can expand this)
    private int[] flags = {R.drawable.flag_armenia, R.drawable.flag_france};
    private String[][] options = {{"Armenia", "France", "Italy", "Germany"},
            {"Spain", "France", "Brazil", "Japan"}};
    private int[] correctAnswers = {0, 1}; // Index of the correct option
    private int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flagImage = findViewById(R.id.flagImage);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        questionText = findViewById(R.id.questionText);

        loadQuestion();

        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                int selectedIndex = Integer.parseInt(v.getTag().toString());
                checkAnswer(selectedIndex);
            }
        };

        option1.setOnClickListener(answerListener);
        option2.setOnClickListener(answerListener);
        option3.setOnClickListener(answerListener);
        option4.setOnClickListener(answerListener);
    }

    private void loadQuestion() {
        if (currentQuestion < flags.length) {
            flagImage.setImageResource(flags[currentQuestion]);
            option1.setText(options[currentQuestion][0]);
            option2.setText(options[currentQuestion][1]);
            option3.setText(options[currentQuestion][2]);
            option4.setText(options[currentQuestion][3]);
            option1.setTag("0");
            option2.setTag("1");
            option3.setTag("2");
            option4.setTag("3");
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
        }
    }

    private void checkAnswer(int selectedIndex) {
        if (selectedIndex == correctAnswers[currentQuestion]) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong! Try again.", Toast.LENGTH_SHORT).show();
        }
        currentQuestion++;
        loadQuestion();
    }
}
