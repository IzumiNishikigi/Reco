package com.reco.applock.ui.security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reco.applock.R;
import com.reco.applock.ui.password.PasswordSetupActivity;

public class SecurityQuestionAuthenticationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppLockPrefs";
    private static final String KEY_SECURITY_QUESTION = "SECURITY_QUESTION";
    private static final String KEY_SECURITY_ANSWER = "SECURITY_ANSWER";

    private EditText answerEditText;
    private String actionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question_authentication);

        // Get the action type from the intent (e.g., CHANGE_PASSWORD or CHANGE_SECURITY_QUESTION)
        actionType = getIntent().getStringExtra("ACTION_TYPE");

        // Initialize views
        TextView questionTextView = findViewById(R.id.question_text_view);
        answerEditText = findViewById(R.id.answer_edit_text);
        Button verifyButton = findViewById(R.id.verify_button);

        // Retrieve the saved security question and set it to the TextView
        String savedQuestion = getSavedSecurityQuestion();
        if (savedQuestion.isEmpty()) {
            Toast.makeText(this, "Security question not set. Please set it first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        questionTextView.setText(savedQuestion);

        verifyButton.setOnClickListener(v -> verifyAnswer());
    }

    private void verifyAnswer() {
        // Retrieve the saved security answer
        String correctAnswer = getSavedSecurityAnswer();
        String enteredAnswer = answerEditText.getText().toString().trim();

        if (enteredAnswer.equalsIgnoreCase(correctAnswer)) {
            Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show();
            proceedWithAction();
        } else {
            Toast.makeText(this, "Incorrect answer. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void proceedWithAction() {
        if ("CHANGE_PASSWORD".equals(actionType)) {
            // Navigate to Password Setup Activity
            Intent intent = new Intent(this, PasswordSetupActivity.class);
            startActivity(intent);
        } else if ("CHANGE_SECURITY_QUESTION".equals(actionType)) {
            // Navigate to Security Question Setup Activity
            Intent intent = new Intent(this, SecurityQuestionSetupActivity.class);
            startActivity(intent);
        }
        finish();
    }

    /**
     * Retrieve the saved security question from SharedPreferences.
     */
    private String getSavedSecurityQuestion() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_SECURITY_QUESTION, "");
    }

    /**
     * Retrieve the saved security answer from SharedPreferences.
     */
    private String getSavedSecurityAnswer() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_SECURITY_ANSWER, "");
    }
}
