package com.reco.applock.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntruderCaptureService {

    private static final String TAG = "IntruderCaptureService";
    private final Context context;

    public IntruderCaptureService(Context context) {
        this.context = context;
    }

    public void captureSelfie() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                ImageCapture imageCapture = new ImageCapture.Builder().build();

                cameraProvider.bindToLifecycle(
                        /* lifecycleOwner= */ (androidx.lifecycle.LifecycleOwner) context,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        imageCapture
                );

                // Save the image in the internal storage
                File imageFile = createImageFile();
                ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(imageFile).build();
                imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d(TAG, "Intruder selfie saved: " + imageFile.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Error capturing selfie: ", exception);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Failed to bind camera provider", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private File createImageFile() {
        // Save selfies in internal app storage
        File storageDir = new File(context.getFilesDir(), "IntruderSelfies");
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory for selfies");
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(storageDir, "Intruder_" + timestamp + ".jpg");
    }
}
