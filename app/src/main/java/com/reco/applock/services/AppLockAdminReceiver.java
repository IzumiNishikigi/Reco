package com.reco.applock.services;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Device Admin Receiver to enforce app lock uninstallation prevention.
 * This receiver must be activated in device settings for full functionality.
 */
public class AppLockAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        showToast(context, "App Lock admin enabled.");
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        showToast(context, "App Lock admin disabled. Uninstallation prevention is now inactive.");
    }

    /**
     * Utility function to display a toast message.
     */
    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Static method to get the ComponentName of this receiver.
     * Useful for activating or deactivating this receiver programmatically.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), AppLockAdminReceiver.class);
    }
}
