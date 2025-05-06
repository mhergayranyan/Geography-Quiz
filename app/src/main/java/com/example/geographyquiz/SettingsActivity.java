package com.example.geographyquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    // Sound system
    private MediaPlayer soundPlayer;
    private static final String PREFS_NAME = "QuizSettings";
    private SharedPreferences prefs;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference scoresRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        scoresRef = FirebaseDatabase.getInstance().getReference("scores");

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize UI elements
        Switch soundSwitch = findViewById(R.id.sound_switch);
        Switch darkModeSwitch = findViewById(R.id.dark_mode_switch);
        Switch vibrationSwitch = findViewById(R.id.vibration_switch);
        Button logoutButton = findViewById(R.id.logout_button);
        Button resetButton = findViewById(R.id.reset_scores_button);
        Button aboutButton = findViewById(R.id.about_button);

        // Load current settings
        soundSwitch.setChecked(prefs.getBoolean("sound_enabled", true));
        darkModeSwitch.setChecked(prefs.getBoolean("dark_mode", false));
        vibrationSwitch.setChecked(prefs.getBoolean("vibration", true));

        // Initialize sound player
        soundPlayer = MediaPlayer.create(this, R.raw.click);
        soundPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // ===== SOUND SETTINGS =====
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("sound_enabled", isChecked).apply();
            if (isChecked) playTestSound();
        });

        // ===== DARK MODE =====
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
            recreate(); // Restart activity to apply theme
        });

        // ===== VIBRATION =====
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("vibration", isChecked).apply();
        });

        // ===== LOGOUT BUTTON =====
        logoutButton.setOnClickListener(v -> showLogoutConfirmation());

        // ===== RESET SCORES =====
        resetButton.setOnClickListener(v -> showResetConfirmation());

        // ===== ABOUT BUTTON =====
        aboutButton.setOnClickListener(v -> {
            if (prefs.getBoolean("sound_enabled", true)) playTestSound();
            new AlertDialog.Builder(this)
                    .setTitle("About Geography Quiz")
                    .setMessage("Version 1.0\nCreated by You!")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void playTestSound() {
        try {
            if (soundPlayer != null) {
                soundPlayer.seekTo(0);
                soundPlayer.start();
            }
        } catch (Exception e) {
            Log.e("SoundError", "Failed to play sound", e);
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Scores")
                .setMessage("This will delete all your quiz records!")
                .setPositiveButton("Reset", (dialog, which) -> {
                    if (mAuth.getCurrentUser() != null) {
                        scoresRef.child(mAuth.getCurrentUser().getUid()).removeValue();
                        Toast.makeText(this, "Scores reset!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        if (soundPlayer != null) {
            soundPlayer.release();
            soundPlayer = null;
        }
        super.onDestroy();
    }
}