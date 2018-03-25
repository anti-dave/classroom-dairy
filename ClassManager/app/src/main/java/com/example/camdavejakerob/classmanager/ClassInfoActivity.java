package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClassInfoActivity extends AppCompatActivity {

    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        // assigns the currentClass
        Intent i = getIntent();

        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");

        // sets title to selected class name
        setTitle(mCurrentClass.getName());

        final LinearLayout syllabusButton = findViewById(R.id.syllabus);
        syllabusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent classesIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity(classesIntent);
            }
        });

        final LinearLayout attendanceButton = findViewById(R.id.attendance);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent  syllabusIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity( syllabusIntent);
            }
        });

        final LinearLayout discussionBoardButton = findViewById(R.id.discussion_board);
        discussionBoardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent discussionBoardIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity(discussionBoardIntent);
            }
        });

        final LinearLayout assignmentsButton = findViewById(R.id.assignments);
        assignmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent assignmentsIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity(assignmentsIntent);
            }
        });

        final LinearLayout gradesButton = findViewById(R.id.grades);
        gradesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gradesIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity(gradesIntent);
            }
        });

        final LinearLayout studentsButton = findViewById(R.id.students);
        studentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent studentsIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity(studentsIntent);
            }
        });

    }
}
