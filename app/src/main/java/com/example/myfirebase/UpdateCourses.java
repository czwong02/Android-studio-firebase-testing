package com.example.myfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateCourses extends AppCompatActivity {

    // creating variables for our edit text
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt;

    // creating a strings for storing our values from Edittext fields.
    private String courseName, courseDuration, courseDescription;

    // creating a variable for firebasefirestore.
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        Courses courses = (Courses) getIntent().getSerializableExtra("course");

        // getting our instance from Firebase Firestore.
        db = FirebaseFirestore.getInstance();

        // initializing our edittext and buttons
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);

        // creating variable for button
        Button updateCOurseBtn = findViewById(R.id.btnUpdateCourse);

        courseNameEdt.setText(courses.getCourseName());
        courseDescriptionEdt.setText(courses.getCourseDescription());
        courseDurationEdt.setText(courses.getCourseDuration());


        updateCOurseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName = courseNameEdt.getText().toString();
                courseDescription = courseDescriptionEdt.getText().toString();
                courseDuration = courseDurationEdt.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Please enter Course Duration");
                } else {
                    // calling a method to update our course.
                    // we are passing our object class, course name,
                    // course description and course duration from our edittext field.
                    updateCourses(courses, courseName, courseDescription, courseDuration);
                    Intent intent = new Intent(UpdateCourses.this, CourseDetails.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void updateCourses(Courses courses, String courseName, String courseDescription, String courseDuration) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Courses updatedCourse = new Courses(courseName, courseDescription, courseDuration);

        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        db.collection("Courses").
                // below line is use toset the id of
                // document where we have to perform
                // update operation.
                        document(courses.getId()).

                // after setting our document id we are
                // passing our whole object class to it.
                        set(updatedCourse).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateCourses.this, "Course has been updated..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateCourses.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}