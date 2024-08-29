package com.example.dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadButton;
    private ImageView imageView;
    private Uri imageUri;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        uploadButton = findViewById(R.id.uploadButton);
        imageView = findViewById(R.id.imageView);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        imageView.setOnClickListener(view -> openFileChooser());
        uploadButton.setOnClickListener(view -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String uploadId = databaseRef.push().getKey();
                databaseRef.child(uploadId).setValue(uri.toString());
                Toast.makeText(AdminActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
            })).addOnFailureListener(e -> Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}