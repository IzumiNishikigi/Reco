package com.reco.applock.intruderselfie;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntruderSelfieActivity extends AppCompatActivity {

    private File selfieDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intruder_selfie);

        // Initialize Views
        SwitchCompat toggleSwitch = findViewById(R.id.toggle_intruder_selfie); // Use SwitchCompat for better compatibility

        // Setup Directory for Selfies
        selfieDirectory = new File(getFilesDir(), "IntruderSelfies");

        // Ensure directory exists and check for failure
        if (!selfieDirectory.exists() && !selfieDirectory.mkdirs()) {
            Toast.makeText(this, "Failed to create selfie directory", Toast.LENGTH_LONG).show();
        }

        // Configure Toggle Switch
        boolean isEnabled = getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("intruder_selfie_enabled", false);
        toggleSwitch.setChecked(isEnabled);
        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("intruder_selfie_enabled", isChecked)
                    .apply();
            String message = isChecked ? "Intruder Selfie Enabled" : "Intruder Selfie Disabled";
            Toast.makeText(IntruderSelfieActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        // Load and display selfies
        loadSelfies();
    }

    private void loadSelfies() {
        RecyclerView recyclerView = findViewById(R.id.selfie_recycler_view);
        
        List<File> selfieList = new ArrayList<>();
        File[] selfies = selfieDirectory.listFiles(file -> file.isFile() && file.getName().endsWith(".jpg"));
        
        if (selfies != null) {
            Collections.addAll(selfieList, selfies);
            // Sort by last modified date, newest first
            selfieList.sort((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        }

        IntruderSelfieAdapter selfieAdapter = new IntruderSelfieAdapter(
                selfieList,
                this::showSelfieDialog,
                this
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(selfieAdapter);
    }

    private void showSelfieDialog(File file) {
        if (!file.exists()) {
            Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView imageView = new ImageView(this);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Picasso.get()
                .load(file)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .fit()
                .centerInside()
                .into(imageView);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Intruder Selfie")
                .setView(imageView)
                .setPositiveButton("Delete", (dialogInterface, which) -> {
                    if (file.delete()) {
                        Toast.makeText(this, "Selfie deleted", Toast.LENGTH_SHORT).show();
                        loadSelfies();
                    } else {
                        Toast.makeText(this, "Failed to delete selfie", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Close", null)
                .create();

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        Picasso.get().cancelTag(this);
        super.onDestroy();
    }
}
