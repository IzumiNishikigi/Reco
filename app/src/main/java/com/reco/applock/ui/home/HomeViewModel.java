package com.reco.applock.ui.home;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeViewModel";

    private final MutableLiveData<List<ApplicationInfo>> appList = new MutableLiveData<>();
    private final PackageManager packageManager;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        packageManager = application.getPackageManager();
        loadApps();
    }

    // LiveData for the list of apps
    public LiveData<List<ApplicationInfo>> getAppList() {
        return appList;
    }

    /**
     * Loads all installed applications, including system apps.
     * Ensures compatibility with Android 11+ by respecting package visibility rules.
     */
    private void loadApps() {
        List<ApplicationInfo> apps = new ArrayList<>();

        try {
            // Create an intent to query for launchable apps
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            // Get a list of all apps that can be launched
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(mainIntent, 0);

            for (ResolveInfo resolveInfo : resolveInfos) {
                ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
                Log.d(TAG, "Found app: " + appInfo.packageName);
                apps.add(appInfo);
            }

        } catch (Exception e) {
            // Log any errors gracefully
            Log.e(TAG, "Error while loading installed apps", e);
        }

        appList.setValue(apps); // Update LiveData with the app list
    }
}
