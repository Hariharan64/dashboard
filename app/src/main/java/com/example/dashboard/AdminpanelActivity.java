package com.example.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminpanelActivity extends AppCompatActivity {


    private EditText editTextStudentName, editTextStudentID, editTextPhoneNumber, editTextStudentBatches;
    private Button buttonAddStudent, buttonLogout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students");

        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextStudentID = findViewById(R.id.editTextStudentID);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextStudentBatches = findViewById(R.id.editTextStudentBatches);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentDetails();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void addStudentDetails() {
        String name = editTextStudentName.getText().toString().trim();
        String studentID = editTextStudentID.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String batches = editTextStudentBatches.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(studentID) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(batches)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> studentDetails = new HashMap<>();
        studentDetails.put("name", name);
        studentDetails.put("studentID", studentID);
        studentDetails.put("phoneNumber", phoneNumber);
        studentDetails.put("batches", batches);

        databaseReference.child(studentID).setValue(studentDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminpanelActivity.this, "Student details added", Toast.LENGTH_SHORT).show();
                editTextStudentName.setText("");
                editTextStudentID.setText("");
                editTextPhoneNumber.setText("");
                editTextStudentBatches.setText("");
            } else {
                Toast.makeText(AdminpanelActivity.this, "Failed to add student details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        Intent intent = new Intent(AdminpanelActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
