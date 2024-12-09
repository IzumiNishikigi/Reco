package com.reco.applock.permissions;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.reco.applock.MainActivity;
import com.reco.applock.services.AppLockAdminReceiver;

public class OnboardingActivity extends AppCompatActivity {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponent;

    // ActivityResultLauncher to handle admin permission request
    private final ActivityResultLauncher<Intent> adminPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (devicePolicyManager.isAdminActive(adminComponent)) {
                    // Admin rights granted
                    Toast.makeText(this, "Admin rights granted.", Toast.LENGTH_SHORT).show();
                    proceedToNextScreen();
                } else {
                    // Admin rights denied
                    Toast.makeText(this, "Admin rights are required to use App Lock.", Toast.LENGTH_LONG).show();
                    finish(); // Exit the app if the user denies the permission
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize DevicePolicyManager and admin component
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = AppLockAdminReceiver.getComponentName(this);

        // Immediately request admin permissions
        requestAdminPermission();
    }

    /**
     * Request admin rights using the default Android admin permission screen.
     */
    private void requestAdminPermission() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "App Lock requires admin rights to prevent unauthorized uninstallation.");
        adminPermissionLauncher.launch(intent);
    }

    /**
     * Navigate to the next screen after admin permission is granted.
     */
    private void proceedToNextScreen() {
        Intent intent = new Intent(this, MainActivity.class); // Replace with your main activity
        startActivity(intent);
        finish();
    }
}
