package com.hababk.restaurant.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;

/**
 * Created by Tayeb-Ali on 04-12-2017.
 */

public class FirebaseUploader {
    private String storageRef;
    private UploadListener uploadListener;
    private UploadTask uploadTask;
    private AsyncTask<File, Void, String> compressionTask;
    private boolean replace;
    private StorageReference uploadRef;
    private Uri fileUri;

    public FirebaseUploader(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public FirebaseUploader(UploadListener uploadListener, String storageRef) {
        this.uploadListener = uploadListener;
        this.storageRef = storageRef;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public void uploadImage(Context context, File file) {
        compressAndUpload(context, "images", file);
    }

    public void uploadAudio(Context context, File file) {
        compressAndUpload(context, "audios", file);
    }

    public void uploadVideo(Context context, File file) {
        compressAndUpload(context, "videos", file);
    }

    public void uploadOthers(Context context, File file) {
        compressAndUpload(context, "others", file);
    }

    @SuppressLint("StaticFieldLeak")
    private void compressAndUpload(final Context context, final String child, final File file) {
        compressionTask = new AsyncTask<File, Void, String>() {
            @Override
            protected String doInBackground(File... files) {
                String filePathCompressed = null;
                Uri originalFileUri = Uri.fromFile(files[0]);
                File tempFile = new File(context.getCacheDir(), originalFileUri.getLastPathSegment());
                //tempFile = File.createTempFile(originalFileUri.getLastPathSegment(), null, context.getCacheDir());
                if (child.equals("images")) {
                    filePathCompressed = SiliCompressor.with(context).compress(originalFileUri.toString(), tempFile);
                } else {
//                    try {
//                        filePathCompressed = SiliCompressor.with(context).compressVideo(files[0].getPath(), context.getCacheDir().getPath());
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
                }
                if (filePathCompressed == null)
                    filePathCompressed = "";
                return filePathCompressed;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                File compressed = new File(s);
                fileUri = Uri.fromFile(compressed.length() > 0 ? compressed : file);
                if (storageRef == null)
                    storageRef = fileUri.getLastPathSegment();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                uploadRef = storage.getReference().child(child).child(storageRef);

                if (replace) {
                    upload();
                } else {
                    checkIfExists();
                }
            }
        };

        compressionTask.execute(file);
    }

    private void checkIfExists() {
        uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uploadListener.onUploadSuccess(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                upload();
            }
        });
    }

    private void upload() {
        uploadTask = uploadRef.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return uploadRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uploadListener.onUploadSuccess(downloadUri.toString());
                } else {
                    uploadListener.onUploadFail(task.getException().getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadListener.onUploadFail(e.getMessage());
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploadListener.onUploadProgress((int) progress);
            }
        });
    }

    public void cancelUpload() {
        if (compressionTask != null && compressionTask.getStatus() != AsyncTask.Status.FINISHED) {
            compressionTask.cancel(true);
        }
        if (uploadTask != null && uploadTask.isInProgress()) {
            uploadTask.cancel();
            uploadListener.onUploadCancelled();
        }
    }

    public interface UploadListener {
        void onUploadFail(String message);

        void onUploadSuccess(String downloadUrl);

        void onUploadProgress(int progress);

        void onUploadCancelled();
    }
}
