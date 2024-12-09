package com.reco.applock.ui.Settings;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.reco.applock.R;
import com.reco.applock.services.AppLockAdminReceiver;
import com.reco.applock.ui.security.SecurityQuestionAuthenticationActivity;

public class SettingsFragment extends Fragment {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponentName;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize DevicePolicyManager and ComponentName
        devicePolicyManager = (DevicePolicyManager) requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponentName = AppLockAdminReceiver.getComponentName(requireContext());

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        // Admin Rights Switch
        SwitchCompat adminSwitch = view.findViewById(R.id.admin_rights_switch);
        adminSwitch.setChecked(isDeviceAdminActive());
        adminSwitch.setOnCheckedChangeListener(this::onAdminSwitchToggled);
        settingsViewModel.getAdminRightsEnabled().observe(getViewLifecycleOwner(), adminSwitch::setChecked);

        // Camera Permission Switch
        SwitchCompat cameraPermissionSwitch = view.findViewById(R.id.switch_camera_permission);
        settingsViewModel.getCameraPermissionEnabled().observe(getViewLifecycleOwner(), cameraPermissionSwitch::setChecked);
        cameraPermissionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setCameraPermissionEnabled(isChecked);
            Toast.makeText(requireContext(), isChecked ? "Camera Permission Enabled" : "Camera Permission Disabled", Toast.LENGTH_SHORT).show();
        });

        // Notification Permission Switch
        SwitchCompat notificationPermissionSwitch = view.findViewById(R.id.switch_notification_permission);
        settingsViewModel.getNotificationPermissionEnabled().observe(getViewLifecycleOwner(), notificationPermissionSwitch::setChecked);
        notificationPermissionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setNotificationPermissionEnabled(isChecked);
            if (isChecked) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent);
            }
        });

        // Accessibility Permission Switch
        SwitchCompat accessibilityPermissionSwitch = view.findViewById(R.id.switch_accessibility_permission);
        settingsViewModel.getAccessibilityPermissionEnabled().observe(getViewLifecycleOwner(), accessibilityPermissionSwitch::setChecked);
        accessibilityPermissionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setAccessibilityPermissionEnabled(isChecked);
            if (isChecked) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        // Change Password Button
        view.findViewById(R.id.btn_change_password).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SecurityQuestionAuthenticationActivity.class);
            intent.putExtra("ACTION_TYPE", "CHANGE_PASSWORD");
            startActivity(intent);
        });

        // Change Security Question Button
        view.findViewById(R.id.btn_change_security_question).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SecurityQuestionAuthenticationActivity.class);
            intent.putExtra("ACTION_TYPE", "CHANGE_SECURITY_QUESTION");
            startActivity(intent);
        });

        return view;
    }

    /**
     * Handle admin rights toggle.
     */
    private void onAdminSwitchToggled(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            activateDeviceAdmin();
            settingsViewModel.setAdminRightsEnabled(true);
        } else {
            deactivateDeviceAdmin();
            settingsViewModel.setAdminRightsEnabled(false);
        }
    }

    /**
     * Check if the app has admin rights.
     */
    private boolean isDeviceAdminActive() {
        return devicePolicyManager.isAdminActive(adminComponentName);
    }

    /**
     * Activate device admin rights.
     */
    private void activateDeviceAdmin() {
        if (!isDeviceAdminActive()) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "App Lock requires admin rights to prevent unauthorized uninstallation.");
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Admin rights are already active.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deactivate device admin rights.
     */
    private void deactivateDeviceAdmin() {
        if (isDeviceAdminActive()) {
            devicePolicyManager.removeActiveAdmin(adminComponentName);
            Toast.makeText(requireContext(), "Admin rights removed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Admin rights are not active.", Toast.LENGTH_SHORT).show();
        }
    }
}
