package com.reco.applock.ui.security;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reco.applock.R;

public class SecurityQuestionResetActivity extends AppCompatActivity {

    private EditText oldAnswerEditText;
    private Spinner newQuestionSpinner;
    private EditText newAnswerEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question_reset);

        // Initialize views
        oldAnswerEditText = findViewById(R.id.old_answer_edit_text);
        newQuestionSpinner = findViewById(R.id.new_question_spinner);
        newAnswerEditText = findViewById(R.id.new_answer_edit_text);
        Button resetButton = findViewById(R.id.reset_button);

        // Setup Spinner with new list of security questions
        String[] questions = {"What was the name of your first teacher?", "What is your favorite book?", "What is your childhood nickname?", "What is the name of your best friend?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newQuestionSpinner.setAdapter(adapter);

        // Get SharedPreferences instance for saving the selected question and answer
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Set the click listener for the reset button
        resetButton.setOnClickListener(v -> resetSecurityQuestion());
    }

    private void resetSecurityQuestion() {
        String oldAnswer = oldAnswerEditText.getText().toString();
        String newQuestion = newQuestionSpinner.getSelectedItem().toString(); // Use the selected question
        String newAnswer = newAnswerEditText.getText().toString();

        if (oldAnswer.isEmpty() || newAnswer.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {
            // Save the new security question and answer in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("security_question", newQuestion);
            editor.putString("security_answer", newAnswer);
            editor.apply();
            Toast.makeText(this, "Security question reset successfully.", Toast.LENGTH_SHORT).show();
        }
    }
}
