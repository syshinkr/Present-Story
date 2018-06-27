package com.project.admin.present_story.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.admin.present_story.Activity.AddScheduleActivity;
import com.project.admin.present_story.Activity.MainActivity;
import com.project.admin.present_story.FbCode;
import com.project.admin.present_story.Model.ScheduleModel;
import com.project.admin.present_story.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CalendarFragment extends Fragment {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String targetStory;
    ValueEventListener valueEventListener;
    Button btn_add;
    ScheduleAdapter scheduleAdapter;

    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        targetStory = getPref("targetStory");

        btn_add = view.findViewById(R.id.fragment_calendar_btn_add);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_calendar_recyclerview);
        scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        final MaterialCalendarView calendarView = view.findViewById(R.id.fragment_calendar_calendarView);
        calendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat("yyyy / MM")));
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String formattedDate = sdf.format(calendarView.getSelectedDate().getDate());
                Log.i("Calendar", formattedDate);
                //TODO : 날에 따른 데이터 가져오기
                if (scheduleAdapter != null) {
                    scheduleAdapter.getSchedules(formattedDate);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.mainActivity_viewpager, new AddScheduleFragment()).commit();
                Intent intent = new Intent(getContext(), AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    String getPref(String key) {
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ScheduleModel> schedules = new ArrayList<>();

        public ScheduleAdapter() {
        }

        public void getSchedules(final String date) {
            // Present-Story/story/{storyToken}/schedule/
            valueEventListener = FirebaseDatabase.getInstance().getReference().
//                    child(FbCode.STORY).child(targetStory).child(FbCode.SCHEDULE).orderByChild(date).equalTo(true)
                    child(FbCode.STORY).child(targetStory).child(FbCode.SCHEDULE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ScheduleModel scheduleModel;
                            schedules.clear();
                            for (DataSnapshot item : dataSnapshot.getChildren()) { // Present-Story/story/{storyToken}/schedule/push()
                               scheduleModel = item.getValue(ScheduleModel.class);
                               if(Integer.parseInt(scheduleModel.sd) <= Integer.parseInt(date) || Integer.parseInt(date) <= Integer.parseInt(scheduleModel.ed)) {
                                   schedules.add(scheduleModel); // Present-Story/story/{storyToken}/schedule/push()/scheduleModel
                               }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, null);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final ScheduleViewHolder viewHolder = (ScheduleViewHolder) holder;
            final ScheduleModel schedule = schedules.get(position);

            viewHolder.textView_content.setText(schedules.get(position).content);
            //TODO : 캘린더프래그먼트 시간 바인딩
            viewHolder.textView_date.setText(schedule.sd.substring(6, 8));
            viewHolder.textView_time.setText(schedule.st +" ~ " + schedule.et);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.dialog_detail, null);
                    final TextView tv_content = view.findViewById(R.id.detailDialog_tv_content);
                    tv_content.setText(schedule.content);

                    final TextView tv_date = view.findViewById(R.id.detailDialog_tv_date);
                    tv_date.setText(dateFormat(schedule.sd) + " " + schedule.st +" ~ " + dateFormat(schedule.ed) + " " + schedule.et);

                    final TextView tv_detail = view.findViewById(R.id.detailDialog_tv_detail);
                    tv_detail.setText(schedule.detail);

                    builder.setView(view).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        /**
         *
         * @param date 문자열로 된 8자리 숫자
         * @return
         */
        private String dateFormat(String date) {
            String year = date.substring(0,4 );
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            return  year + "-" + month + "-" + day;
        }
    }

    private class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView textView_content;
        TextView textView_time;
        TextView textView_date;

        public ScheduleViewHolder(View view) {
            super(view);
            textView_content = view.findViewById(R.id.scheduleItem_textview_content);
            textView_time = view.findViewById(R.id.scheduleItem_textview_time);
            textView_date = view.findViewById(R.id.scheduleItem_textview_date);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null)
            valueEventListener = null;
    }
}
