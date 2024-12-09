package com.reco.applock.ui.managers;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppUsageMonitor {

    private static final String TAG = "AppUsageMonitor";
    private final UsageStatsManager usageStatsManager;

    public AppUsageMonitor(Context context) {
        usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public String getCurrentAppPackage() {
        long currentTime = System.currentTimeMillis();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - 1000 * 60,
                currentTime
        );

        if (usageStatsList == null || usageStatsList.isEmpty()) {
            Log.w(TAG, "No usage stats available. Ensure Usage Access permission is granted.");
            return null;
        }

        SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
        for (UsageStats usageStats : usageStatsList) {
            sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
        }

        if (!sortedMap.isEmpty()) {
            String currentApp = Objects.requireNonNull(sortedMap.get(sortedMap.lastKey())).getPackageName();
            Log.d(TAG, "Current app in use: " + currentApp);
            return currentApp;
        }

        return null;
    }
}
