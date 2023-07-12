package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextAge, editTextGrade;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGrade = findViewById(R.id.editTextGrade);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Set click listener for the update button
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered student details
                String name = editTextName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String grade = editTextGrade.getText().toString().trim();

                // Validate the entered data
                if (name.isEmpty() || age.isEmpty() || grade.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the student data (replace with your own logic)
                updateStudentData(name, age, grade);
            }
        });
    }

    private void updateStudentData(String name, String age, String grade) {
        // Replace this method with your own logic to update student data
        // You can use a database or any other storage mechanism to update the data
        // For this example, we'll simply display a toast message with the updated details

        String message = "Student data updated:\n" +
                "Name: " + name + "\n" +
                "Age: " + age + "\n" +
                "Grade: " + grade;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

