package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClassInfoActivity extends AppCompatActivity {

    private String CLASS_ID = "CLASS_ID";
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
//                Intent classesIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
//                startActivity(classesIntent);
               String pdf_url = "https://firebasestorage.googleapis.com/v0/b/classmanager-38435.appspot.com/o/Syllabus_Sample.pdf?alt=media&token=3232f7b8-f732-4b75-b984-a1d7357c85d3";
               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
               startActivity(browserIntent);
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
                Intent discussionBoardIntent = new Intent(ClassInfoActivity.this, ChatActivity.class);
                startActivity(discussionBoardIntent);
            }
        });

        final LinearLayout assignmentsButton = findViewById(R.id.assignments);
        assignmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent assignmentsIntent = new Intent(ClassInfoActivity.this, AssignmentActivity.class);
                assignmentsIntent.putExtra("TITLE", mCurrentClass.getName() + " Assignments");
                startActivity(assignmentsIntent);
            }
        });

        final LinearLayout gradesButton = findViewById(R.id.grades);
        gradesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gradesIntent = new Intent(ClassInfoActivity.this, GradesActivity.class);
                gradesIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                startActivity(gradesIntent);
            }
        });

        final LinearLayout studentsButton = findViewById(R.id.students);
        studentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent studentsIntent = new Intent(ClassInfoActivity.this, RosterActivity.class);
                studentsIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                startActivity(studentsIntent);
            }
        });

    }
}
