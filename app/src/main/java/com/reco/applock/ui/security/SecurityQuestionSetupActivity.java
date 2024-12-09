package com.reco.applock.ui.security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.reco.applock.R;
import com.reco.applock.permissions.PermissionActivationActivity;

public class SecurityQuestionSetupActivity extends AppCompatActivity {

    private Spinner questionSpinner;
    private EditText answerEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question_setup);

        // Initialize views
        questionSpinner = findViewById(R.id.question_spinner);
        answerEditText = findViewById(R.id.answer_edit_text);
        Button saveButton = findViewById(R.id.save_button);

        // Setup Spinner with a list of security questions
        String[] questions = {"What is your pet's name?", "What is your favorite color?", "What was your first car?", "Where were you born?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpinner.setAdapter(adapter);

        // Get SharedPreferences instance for saving the selected question and answer
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Set the click listener for the save button
        saveButton.setOnClickListener(v -> saveSecurityQuestion());

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button during first-time setup
                Toast.makeText(SecurityQuestionSetupActivity.this, "Back button is disabled during setup.", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void saveSecurityQuestion() {
        String selectedQuestion = questionSpinner.getSelectedItem().toString();
        String answer = answerEditText.getText().toString();

        if (answer.isEmpty()) {
            Toast.makeText(this, "Please provide an answer.", Toast.LENGTH_SHORT).show();
        } else {
            // Save the selected question and answer in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("security_question", selectedQuestion);
            editor.putString("security_answer", answer);
            editor.apply();
            Toast.makeText(this, "Security question saved successfully.", Toast.LENGTH_SHORT).show();

            // Navigate to Permission Activation
            Intent intent = new Intent(this, PermissionActivationActivity.class);
            startActivity(intent);
        }
    }
}
