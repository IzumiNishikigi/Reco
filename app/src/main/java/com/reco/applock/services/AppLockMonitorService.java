package com.reco.applock.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.reco.applock.R;
import com.reco.applock.ui.managers.AppUsageMonitor;
import com.reco.applock.ui.managers.LockedAppsManager;
import com.reco.applock.ui.password.PINInputActivity;

public class AppLockMonitorService extends Service {

    private static final long CHECK_INTERVAL = 1000; // Check every second
    private static final String CHANNEL_ID = "AppLockMonitorChannel";
    private static final int NOTIFICATION_ID = 1;

    private Handler handler;
    private Runnable checkAppRunnable;

    private AppUsageMonitor appUsageMonitor;
    private LockedAppsManager lockedAppsManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the notification channel
        createNotificationChannel();

        // Initialize AppUsageMonitor and LockedAppsManager
        appUsageMonitor = new AppUsageMonitor(this);
        lockedAppsManager = LockedAppsManager.getInstance(this);

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, createNotification());

        // Use Handler with the main looper to avoid deprecation warnings
        handler = new Handler(Looper.getMainLooper());
        checkAppRunnable = this::checkCurrentApp;
        handler.postDelayed(checkAppRunnable, CHECK_INTERVAL);
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "App Lock Monitor Service",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("App Lock Monitor")
                .setContentText("Monitoring app usage...")
                .setSmallIcon(R.drawable.ic_lock) // Replace with your app's icon
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void checkCurrentApp() {
        String currentApp = appUsageMonitor.getCurrentAppPackage();
        if (currentApp != null && lockedAppsManager.isAppLocked(currentApp)) {
            // Redirect to the password screen
            Intent intent = new Intent(this, PINInputActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("locked_app_package", currentApp);
            startActivity(intent);
        }
        handler.postDelayed(checkAppRunnable, CHECK_INTERVAL);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checkAppRunnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
