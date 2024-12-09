package com.reco.applock.ui.Settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for managing app settings.
 */
public class SettingsViewModel extends AndroidViewModel {

    // LiveData for admin rights state
    private final MutableLiveData<Boolean> adminRightsEnabled = new MutableLiveData<>();

    // LiveData for camera permission state
    private final MutableLiveData<Boolean> cameraPermissionEnabled = new MutableLiveData<>();

    // LiveData for notification permission state
    private final MutableLiveData<Boolean> notificationPermissionEnabled = new MutableLiveData<>();

    // LiveData for accessibility permission state
    private final MutableLiveData<Boolean> accessibilityPermissionEnabled = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        // Initialize default values
        adminRightsEnabled.setValue(false);
        cameraPermissionEnabled.setValue(false);
        notificationPermissionEnabled.setValue(false);
        accessibilityPermissionEnabled.setValue(false);
    }

    public MutableLiveData<Boolean> getAdminRightsEnabled() {
        return adminRightsEnabled;
    }

    public void setAdminRightsEnabled(boolean isEnabled) {
        adminRightsEnabled.setValue(isEnabled);
    }

    public MutableLiveData<Boolean> getCameraPermissionEnabled() {
        return cameraPermissionEnabled;
    }

    public void setCameraPermissionEnabled(boolean isEnabled) {
        cameraPermissionEnabled.setValue(isEnabled);
    }

    public MutableLiveData<Boolean> getNotificationPermissionEnabled() {
        return notificationPermissionEnabled;
    }

    public void setNotificationPermissionEnabled(boolean isEnabled) {
        notificationPermissionEnabled.setValue(isEnabled);
    }

    public MutableLiveData<Boolean> getAccessibilityPermissionEnabled() {
        return accessibilityPermissionEnabled;
    }

    public void setAccessibilityPermissionEnabled(boolean isEnabled) {
        accessibilityPermissionEnabled.setValue(isEnabled);
    }
}
