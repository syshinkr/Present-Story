package com.project.admin.present_story.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.admin.present_story.Fragment.AddScheduleFragment;
import com.project.admin.present_story.R;

public class AddScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        getSupportFragmentManager().beginTransaction().replace(R.id.addscheduleActivity_frameLayout, new AddScheduleFragment()).commit();
    }
}
