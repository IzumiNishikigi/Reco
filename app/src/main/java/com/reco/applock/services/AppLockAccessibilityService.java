package com.reco.applock.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.reco.applock.ui.managers.LockedAppsManager;
import com.reco.applock.ui.password.PINInputActivity;

/**
 * Accessibility service for monitoring app usage and locking specified apps.
 */
public class AppLockAccessibilityService extends AccessibilityService {

    private static final String TAG = "AppLockAccessibility";
    private LockedAppsManager lockedAppsManager;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        lockedAppsManager = LockedAppsManager.getInstance(this);
        Log.d(TAG, "Accessibility service connected.");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Ensure the event type matches what is declared in accessibility_service_config.xml
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? event.getPackageName().toString() : null;

            if (packageName != null && lockedAppsManager.isAppLocked(packageName)) {
                Log.d(TAG, "Locked app detected: " + packageName);

                // Launch the PIN input screen for the locked app
                Intent intent = new Intent(this, PINInputActivity.class);
                intent.putExtra("LOCKED_PACKAGE_NAME", packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Accessibility service destroyed.");
    }
}
