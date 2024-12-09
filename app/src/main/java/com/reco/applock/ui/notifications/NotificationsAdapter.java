package com.reco.applock.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private final Map<String, List<String>> appNotifications;

    public NotificationsAdapter(Map<String, List<String>> appNotifications) {
        this.appNotifications = appNotifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String packageName = new ArrayList<>(appNotifications.keySet()).get(position);
        List<String> notifications = appNotifications.get(packageName);
        holder.bind(packageName, notifications);
    }

    @Override
    public int getItemCount() {
        return appNotifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final TextView appNameTextView;
        private final RecyclerView notificationContentRecyclerView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            notificationContentRecyclerView = itemView.findViewById(R.id.notificationContentRecyclerView);
            notificationContentRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void bind(String packageName, List<String> notifications) {
            appNameTextView.setText(packageName);
            NotificationContentAdapter adapter = new NotificationContentAdapter(notifications);
            notificationContentRecyclerView.setAdapter(adapter);
        }
    }
}
