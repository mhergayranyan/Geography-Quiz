package com.example.geographyquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button flagsBtn = findViewById(R.id.flags_btn);
        Button capitalsBtn = findViewById(R.id.capitals_btn);
        Button profileBtn = findViewById(R.id.profile_btn);
        Button settingsBtn = findViewById(R.id.settings_btn);

        flagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("flags");
            }
        });

        capitalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("capitals");
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, ProfileActivity.class));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });
    }

    private void startQuiz(String quizType) {
        Intent intent = new Intent(MainMenuActivity.this, QuizActivity.class);
        intent.putExtra("quiz_type", quizType);
        startActivity(intent);
    }
}