package com.reco.applock.ui.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class LockedAppsManager {

    private static final String PREF_NAME = "AppLockPrefs";
    private static final String LOCKED_APPS_KEY = "LockedApps";
    private static final String PATTERN_PROTECTED_APPS_KEY = "PatternProtectedApps";

    private final SharedPreferences sharedPreferences;

    private static LockedAppsManager instance;

    public LockedAppsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized LockedAppsManager getInstance(Context context) {
        if (instance == null) {
            instance = new LockedAppsManager(context.getApplicationContext());
        }
        return instance;
    }

    // Check if an app is locked using PIN
    public boolean isAppLocked(String packageName) {
        Set<String> lockedApps = sharedPreferences.getStringSet(LOCKED_APPS_KEY, new HashSet<>());
        return lockedApps.contains(packageName);
    }

    // Add an app to the locked list using PIN
    public void addLockedApp(String packageName) {
        Set<String> lockedApps = new HashSet<>(sharedPreferences.getStringSet(LOCKED_APPS_KEY, new HashSet<>()));
        lockedApps.add(packageName);
        sharedPreferences.edit().putStringSet(LOCKED_APPS_KEY, lockedApps).apply();
    }

    // Remove an app from the locked list
    public void removeLockedApp(String packageName) {
        Set<String> lockedApps = new HashSet<>(sharedPreferences.getStringSet(LOCKED_APPS_KEY, new HashSet<>()));
        lockedApps.remove(packageName);
        sharedPreferences.edit().putStringSet(LOCKED_APPS_KEY, lockedApps).apply();
    }

    // Add pattern-protected app
    public void addPatternProtectedApp(String packageName) {
        Set<String> patternApps = new HashSet<>(sharedPreferences.getStringSet(PATTERN_PROTECTED_APPS_KEY, new HashSet<>()));
        patternApps.add(packageName);
        sharedPreferences.edit().putStringSet(PATTERN_PROTECTED_APPS_KEY, patternApps).apply();
    }

    // Check if an app is pattern-protected
    public boolean isAppPatternProtected(String packageName) {
        Set<String> patternApps = sharedPreferences.getStringSet(PATTERN_PROTECTED_APPS_KEY, new HashSet<>());
        return patternApps.contains(packageName);
    }
}
