package com.reco.applock.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;
import com.reco.applock.ui.managers.LockedAppsAdapter;
import com.reco.applock.ui.managers.LockedAppsManager;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize LockedAppsManager
        LockedAppsManager lockedAppsManager = LockedAppsManager.getInstance(requireContext());

        // Initialize ViewModel
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe LiveData and update RecyclerView
        homeViewModel.getAppList().observe(getViewLifecycleOwner(), appList -> {
            LockedAppsAdapter adapter = new LockedAppsAdapter(appList, requireContext().getPackageManager(), lockedAppsManager, false);
            recyclerView.setAdapter(adapter);
        });

        return root;
    }
}
