package com.example.dashboard;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REGISTER_REQUEST_CODE = 1;

    private Button btnOpenRegister;
    private Button btnSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenRegister = findViewById(R.id.btnOpenRegister);
        btnSuccess = findViewById(R.id.btnSuccess);

        btnOpenRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open RegisterActivity and wait for a result
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Make the success button visible after successful registration
            btnSuccess.setVisibility(View.VISIBLE);
        }
    }
}
