package com.example.megane.todolistfirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectedTask extends AppCompatActivity {

    private String task_key;
    private TextView selectedTask, selectedTime;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_task);

        task_key = getIntent().getExtras().getString("TaskID");

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Task");

        selectedTask = (TextView) findViewById(R.id.selectedTask);
        selectedTime = (TextView) findViewById(R.id.selectedTime);

        //Retrieve the details of the task with the task_key in the DB
        myDatabase.child(task_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String task_name = (String) dataSnapshot.child("name").getValue();
                String task_time = (String) dataSnapshot.child("time").getValue();

                selectedTask.setText(task_name);
                selectedTime.setText(task_time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
