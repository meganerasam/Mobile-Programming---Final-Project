package com.example.megane.todolistfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mTaskList;
    private DatabaseReference myDatabase;
    private TextView bannerDay, bannerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up the design of the Recycler view
        mTaskList = (RecyclerView) findViewById(R.id.task_list);
        mTaskList.setHasFixedSize(true);
        mTaskList.setLayoutManager(new LinearLayoutManager(this));

        //Fetch dynamically the day and date for the recyclerView Banner
        bannerDay = (TextView) findViewById(R.id.bannerDay);
        bannerDate = (TextView) findViewById(R.id.bannerDate);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        bannerDay.setText(dayOfTheWeek);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdff = new SimpleDateFormat("MMM MM dd, yyy h:mm a");
        String dateString = sdff.format(date);
        bannerDate.setText(dateString);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Task");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_setting){
            return true;
        }
        else if(id == R.id.addTask){
            Intent addIntent = new Intent(MainActivity.this, AddTask.class);
            startActivity(addIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Take elements from class "Task" and set the RecylerView value to the actual value
    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TaskViewHolder (View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView task_name = (TextView) mView.findViewById(R.id.taskName);
            task_name.setText(name);
        }

        public void setTime (String time){
            TextView task_time = (TextView) mView.findViewById(R.id.taskTime);
            task_time.setText(time);
        }
    }

    //Create an onStart method in order to Populate the recyclerView.
    //It works the same way as an arrayAdapter
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Task, TaskViewHolder> fbra = new FirebaseRecyclerAdapter<Task, TaskViewHolder>(
                Task.class, R.layout.task_row, TaskViewHolder.class, myDatabase

        ) {
            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, Task model, int position) {

                final String task_key = getRef(position).getKey().toString();

                viewHolder.setName(model.getName());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewSelectedTask = new Intent(MainActivity.this, SelectedTask.class);
                        viewSelectedTask.putExtra("TaskID", task_key);
                        startActivity(viewSelectedTask);
                    }
                });
            }
        };

        mTaskList.setAdapter(fbra);
    }
}
