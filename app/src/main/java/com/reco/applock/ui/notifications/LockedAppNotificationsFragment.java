package com.reco.applock.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;

public class LockedAppNotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locked_app_notifications, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize NotificationsViewModel
        notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        // Observe notifications and update adapter
        notificationsViewModel.getAppNotifications().observe(getViewLifecycleOwner(), appNotifications -> {
            NotificationsAdapter adapter = new NotificationsAdapter(appNotifications);
            recyclerView.setAdapter(adapter);
        });

        // Handle Clear All Button
        Button clearAllButton = view.findViewById(R.id.clearAllButton);
        clearAllButton.setOnClickListener(v -> {
            String packageName = "com.example.lockedapp"; // Placeholder; Replace dynamically
            notificationsViewModel.clearAllNotifications(packageName);
        });

        return view;
    }
}
