package com.reco.applock.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.reco.applock.R;
import com.reco.applock.ui.managers.LockedAppsManager;
import com.reco.applock.ui.notifications.NotificationsViewModel;
import com.reco.applock.ui.password.PINInputActivity;

public class AppLockNotificationListener extends NotificationListenerService implements ViewModelStoreOwner {

    private static final String TAG = "AppLockNotificationListener";
    private static final int FOREGROUND_NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "AppLockNotificationChannel";

    private LockedAppsManager lockedAppsManager;
    private NotificationsViewModel notificationsViewModel;
    private ViewModelStore viewModelStore;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize LockedAppsManager
        lockedAppsManager = new LockedAppsManager(getApplicationContext());

        // Initialize ViewModelStore
        viewModelStore = new ViewModelStore();
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        // Create notification channel
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "App Lock Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Notifications related to locked apps");
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        // Start the service in the foreground
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_lock)
                .setContentTitle("App Lock")
                .setContentText("Monitoring notifications for locked apps")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if (lockedAppsManager.isAppLocked(packageName)) {
            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                String appName = (String) pm.getApplicationLabel(appInfo);

                // Add a record of the notification to the ViewModel
                notificationsViewModel.addNotification(packageName, appName);

                // Launch PINInputActivity if the app is locked
                launchPasswordInputActivity(packageName);

                // Show a generic hidden notification
                showHiddenNotification(appName);

                // Dismiss the original notification
                cancelNotification(sbn.getKey());

            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Exception while processing locked app notification", e);
            }
        }
    }

    private void launchPasswordInputActivity(String packageName) {
        Intent passwordIntent = new Intent(this, PINInputActivity.class);
        passwordIntent.putExtra("LOCKED_PACKAGE_NAME", packageName);
        passwordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(passwordIntent);
    }

    private void showHiddenNotification(String appName) {
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_lock)
                .setContentTitle("App Lock Notification")
                .setContentText("New notification from " + appName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), notification);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if (lockedAppsManager.isAppLocked(packageName)) {
            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                String appName = (String) pm.getApplicationLabel(appInfo);

                // Remove the notification record from ViewModel
                notificationsViewModel.removeNotification(packageName, appName);

            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Exception while removing locked app notification", e);
            }
        }
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
}
