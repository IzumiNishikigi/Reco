package com.reco.applock.intruderselfie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reco.applock.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class IntruderSelfieAdapter extends RecyclerView.Adapter<IntruderSelfieAdapter.ViewHolder> {

    private final List<File> imageFiles;
    private final OnSelfieClickListener onSelfieClickListener;
    private final Context context;

    // Define a functional interface for selfie click actions
    @FunctionalInterface
    public interface OnSelfieClickListener {
        void onSelfieClick(File file);
    }

    public IntruderSelfieAdapter(List<File> imageFiles, OnSelfieClickListener onSelfieClickListener, Context context) {
        this.imageFiles = imageFiles;
        this.onSelfieClickListener = onSelfieClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intruder_selfie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = imageFiles.get(position);

        // Use Picasso to load the image from file into the ImageView
        Picasso.get()
                .load(file) // Load the image file
                .placeholder(R.drawable.placeholder_image) // Optional placeholder
                .error(R.drawable.error_image) // Optional error image
                .into(holder.imageView);

        // On click: Trigger the listener
        holder.itemView.setOnClickListener(v -> onSelfieClickListener.onSelfieClick(file));

        // Long click to delete the file
        holder.itemView.setOnLongClickListener(v -> {
            boolean deleted = file.delete();
            if (deleted) {
                imageFiles.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Deleted: " + file.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete: " + file.getName(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    // Make ViewHolder class private to prevent external access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
