package com.example.dashboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ViewActivity extends AppCompatActivity {

    private TextView textViewStudentInfo;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        textViewStudentInfo = findViewById(R.id.textViewStudentInfo);

        // Retrieve and display student data
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("students").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student != null) {
                        String info = "Name: " + student.name + "\n"
                                + "Phone: " + student.phone + "\n"
                                + "Course: " + student.course + "\n"
                                + "Book: " + student.book;
                        textViewStudentInfo.setText(info);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    // Define a Student class
    public static class Student {
        public String name;
        public String phone;
        public String course;
        public String book;

        public Student() {
            // Default constructor required for calls to DataSnapshot.getValue(Student.class)
        }

        public Student(String name, String phone, String course, String book) {
            this.name = name;
            this.phone = phone;
            this.course = course;
            this.book = book;
        }
    }
}