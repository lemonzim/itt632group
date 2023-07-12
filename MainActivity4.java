package com.example.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextAge, editTextGrade;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGrade = findViewById(R.id.editTextGrade);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered student details
                String courseName = editTextName.getText().toString().trim();
                String courseDuration = editTextAge.getText().toString().trim();
                String courseDescription = editTextGrade.getText().toString().trim();

                if (courseName.isEmpty() || courseDuration.isEmpty() || courseDescription.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateStudentData(courseName, courseDuration, courseDescription);
            }
        });

        // Make an HTTP request to read.php
        new FetchStudentDataTask().execute();
    }

    private void updateStudentData(String name, String age, String grade) {
        new UpdateStudentDataTask().execute(name, age, grade);
    }

    private class UpdateStudentDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String courseName = params[0];
            String courseDuration = params[1];
            String courseDescription = params[2];

            try {
                // Create a URL with the update.php endpoint
                URL url = new URL("http://206.189.151.246:80/update.php"); // Replace with your actual update.php URL

                // Create a HttpURLConnection and set the request method to POST
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Create the request parameters and write them to the connection's output stream
                String postData = "courseName=" + URLEncoder.encode(courseName, "UTF-8") +
                        "&courseDuration=" + URLEncoder.encode(courseDuration, "UTF-8") +
                        "&courseDescription=" + URLEncoder.encode(courseDescription, "UTF-8");
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                outputStream.close();

                // Get the response from the server
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();

                // Return the response from the server
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Handle the response from the server (e.g., display a success message)
                Toast.makeText(MainActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where the update failed
                Toast.makeText(MainActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class FetchStudentDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://206.189.151.246:80/read.php"); // Replace with your actual URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();

                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject course = jsonArray.getJSONObject(0); // Get the first course object

                        // Extract the course details from the JSON object
                        String courseName = course.getString("courseName");
                        String courseDuration = course.getString("courseDuration");
                        String courseDescription = course.getString("courseDescription");

                        // Populate the EditText fields with the retrieved values
                        editTextName.setText(courseName);
                        editTextAge.setText(courseDuration);
                        editTextGrade.setText(courseDescription);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to retrieve course data", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
