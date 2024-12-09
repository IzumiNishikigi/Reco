package com.reco.applock.ui.password;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.reco.applock.R;

import java.util.List;

public class PatternSetupActivity extends AppCompatActivity {

    private PatternLockView patternLockView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setup);

        patternLockView = findViewById(R.id.pattern_lock_view);
        preferences = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String savedPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                preferences.edit().putString("SAVED_PATTERN", savedPattern).apply();
                Toast.makeText(PatternSetupActivity.this, "Pattern Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCleared() {}
        });
    }
}
