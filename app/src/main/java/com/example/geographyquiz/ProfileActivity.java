package com.example.geographyquiz;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView emailText = findViewById(R.id.profile_email);
        TextView scoresText = findViewById(R.id.profile_scores);

        if (user != null) {
            // Display user email
            emailText.setText("Email: " + user.getEmail());

            // You would typically fetch scores from Firebase Database here
            // For now we'll use placeholder data
            String scores = "Flags Quiz: 8/10\nCapitals Quiz: 7/10";
            scoresText.setText(scores);
        }
    }
}