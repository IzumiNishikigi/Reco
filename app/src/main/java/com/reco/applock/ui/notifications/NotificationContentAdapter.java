package com.reco.applock.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;

import java.util.List;

public class NotificationContentAdapter extends RecyclerView.Adapter<NotificationContentAdapter.NotificationContentViewHolder> {

    private final List<String> notifications;

    public NotificationContentAdapter(List<String> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_content, parent, false);
        return new NotificationContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationContentViewHolder holder, int position) {
        String content = notifications.get(position);
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationContentViewHolder extends RecyclerView.ViewHolder {

        private final TextView contentTextView;

        public NotificationContentViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.notificationContentTextView);
            Button removeButton = itemView.findViewById(R.id.removeNotificationButton);

            removeButton.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifications.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

        public void bind(String content) {
            contentTextView.setText(content);
        }
    }
}
