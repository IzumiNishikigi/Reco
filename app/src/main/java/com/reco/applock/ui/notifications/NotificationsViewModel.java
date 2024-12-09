package com.reco.applock.ui.notifications;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsViewModel extends AndroidViewModel {

    // Non-static field for notifications data
    private final MutableLiveData<Map<String, List<String>>> appNotifications;

    // Constructor initializes appNotifications
    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        this.appNotifications = new MutableLiveData<>(new HashMap<>());
    }

    // Get notifications
    public LiveData<Map<String, List<String>>> getAppNotifications() {
        return appNotifications;
    }

    // Add a notification
    public void addNotification(String packageName, String content) {
        Map<String, List<String>> currentNotifications = appNotifications.getValue();
        if (currentNotifications != null) {
            // Add notification to the corresponding app
            currentNotifications.computeIfAbsent(packageName, k -> new ArrayList<>()).add(content);
            appNotifications.setValue(currentNotifications);
        }
    }

    // Remove a specific notification
    public void removeNotification(String packageName, String content) {
        Map<String, List<String>> currentNotifications = appNotifications.getValue();
        if (currentNotifications != null && currentNotifications.containsKey(packageName)) {
            List<String> notificationsList = currentNotifications.get(packageName);
            if (notificationsList != null) {
                notificationsList.remove(content);
                if (notificationsList.isEmpty()) {
                    currentNotifications.remove(packageName); // Remove app if no notifications remain
                }
                appNotifications.setValue(currentNotifications);
            }
        }
    }

    // Clear all notifications for a specific app
    public void clearAllNotifications(String packageName) {
        Map<String, List<String>> currentNotifications = appNotifications.getValue();
        if (currentNotifications != null && currentNotifications.containsKey(packageName)) {
            currentNotifications.remove(packageName);
            appNotifications.setValue(currentNotifications);
        }
    }
}
