package com.reco.applock.ui.managers;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;

import java.util.ArrayList;
import java.util.List;

public class LockedAppsAdapter extends RecyclerView.Adapter<LockedAppsAdapter.AppViewHolder> {

    private static final String TAG = "LockedAppsAdapter";

    private final List<String> appList;
    private final PackageManager packageManager;
    private final LockedAppsManager lockedAppsManager;
    private final boolean usePatternProtection; // Flag to determine locking method

    // Constructor for LockedAppsAdapter
    public LockedAppsAdapter(List<ApplicationInfo> appInfoList, PackageManager packageManager, LockedAppsManager lockedAppsManager, boolean usePatternProtection) {
        this.appList = new ArrayList<>();
        for (ApplicationInfo appInfo : appInfoList) {
            this.appList.add(appInfo.packageName); // Store package names
        }
        this.packageManager = packageManager;
        this.lockedAppsManager = lockedAppsManager;
        this.usePatternProtection = usePatternProtection; // Use pattern protection if true
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        String packageName = appList.get(position);

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            holder.appName.setText(packageManager.getApplicationLabel(appInfo));
            holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo));

            // Determine if the app is locked (PIN or Pattern)
            boolean isLocked = usePatternProtection
                    ? lockedAppsManager.isAppPatternProtected(packageName)
                    : lockedAppsManager.isAppLocked(packageName);

            holder.lockToggle.setChecked(isLocked);

            // Add locking functionality based on the selected method
            holder.lockToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (usePatternProtection) {
                        lockedAppsManager.addPatternProtectedApp(packageName);
                        Log.d(TAG, "Pattern-protected app: " + packageName);
                    } else {
                        lockedAppsManager.addLockedApp(packageName);
                        Log.d(TAG, "Locked app: " + packageName);
                    }
                } else {
                    lockedAppsManager.removeLockedApp(packageName);
                    Log.d(TAG, "Unlocked app: " + packageName);
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "App not found: " + packageName, e);
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        SwitchCompat lockToggle;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            lockToggle = itemView.findViewById(R.id.lockToggle);
        }
    }
}
