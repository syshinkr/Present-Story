package com.project.admin.present_story.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.project.admin.present_story.FbCode;
import com.project.admin.present_story.Model.ScheduleModel;
import com.project.admin.present_story.R;

import static android.content.Context.MODE_PRIVATE;

public class AddScheduleFragment extends Fragment {

    private static final String TAG = "AddScheduleFragment";
    EditText et_content;
    EditText et_detail;
    Button btn_sd, btn_st, btn_ed, btn_et;
    Button btn_cancel, btn_add;
    String targetStory;
    String ed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        targetStory = getPref("targetStory");


        Log.i(TAG, "실행됨");
        View view = inflater.inflate(R.layout.fragment_addscheduled, container, false);

        et_content = view.findViewById(R.id.addscheduleFragment_et_content);
        et_detail = view.findViewById(R.id.addscheduleFragment_et_detail);

        btn_sd = view.findViewById(R.id.addscheduleFragment_btn_sd);
        btn_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getActivity().getFragmentManager(), "startDatePicker");
                dialog.setDialogResult(new DatePickerFragment.DialogResult() {
                    @Override
                    public void result(int year, int month, int dayOfMonth) {
                        btn_sd.setText(year + "-" + formatDate(month) + "-" + formatDate(dayOfMonth));
                    }
                });
            }
        });



        btn_st = view.findViewById(R.id.addscheduleFragment_btn_st);
        btn_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getActivity().getFragmentManager(), "startTimePicker");
                dialog.setDialogResult(new TimePickerFragment.DialogResult() {
                    @Override
                    public void result(String time) {
                        btn_st.setText(time);
                    }
                });
            }
        });

        btn_ed = view.findViewById(R.id.addscheduleFragment_btn_ed);
        btn_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getActivity().getFragmentManager(), "endDatePicker");
                dialog.setDialogResult(new DatePickerFragment.DialogResult() {
                    @Override
                    public void result(int year, int month, int dayOfMonth) {
                        btn_ed.setText(year + "-" + formatDate(month) + "-" + formatDate(dayOfMonth));
                        ed = year + month + formatDate(month) + formatDate(dayOfMonth);
                    }
                });
            }
        });

        btn_et = view.findViewById(R.id.addscheduleFragment_btn_et);
        btn_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getActivity().getFragmentManager(), "endTimePicker");
                dialog.setDialogResult(new TimePickerFragment.DialogResult() {
                    @Override
                    public void result(String time) {
                        btn_et.setText(time);
                    }
                });
            }
        });

        btn_cancel = view.findViewById(R.id.addscheduleFragment_btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().remove(AddScheduleFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });

        btn_add = view.findViewById(R.id.addscheduleFragment_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataVailed()) {
                    ScheduleModel scheduleModel = new ScheduleModel();
                    scheduleModel.content = et_content.getText().toString();
                    scheduleModel.detail = et_detail.getText().toString();
                    scheduleModel.sd = btn_sd.getText().toString().replace("-", "");
                    scheduleModel.st = btn_st.getText().toString();
                    scheduleModel.ed = btn_ed.getText().toString().replace("-", "");
                    scheduleModel.et = btn_et.getText().toString();

                    addSchedules(scheduleModel);
                } else {
                    Toast.makeText(getContext(), "입력되지 않은 항목이 있나 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public void addSchedules(ScheduleModel schedule) {
        FirebaseDatabase.getInstance().getReference().
                child(FbCode.STORY).child(targetStory).child(FbCode.SCHEDULE).push().setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                end();
            }
        });
    }

    String getPref(String key) {
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private boolean isDataVailed() {
        final String defaultDate = "9999-99-99";
        final String defaultTime = "AM 99:99";

        String content = et_content.getText().toString();
        String detail = et_detail.getText().toString();
        String sd = btn_sd.getText().toString();
        String st = btn_st.getText().toString();
        String ed = btn_ed.getText().toString();
        String et = btn_et.getText().toString();

        if(TextUtils.isEmpty(content) || TextUtils.isEmpty(detail) || sd.equals(defaultDate)
                || st.equals(defaultTime) || ed.equals(defaultDate) || et.equals(defaultTime)) {
            return false;
        } else {
            return true;
        }
    }

    private void end() {
        getFragmentManager().beginTransaction().remove(AddScheduleFragment.this).commit();
        getFragmentManager().popBackStack();
        getActivity().finish();
    }

    // 일자 두자리로 포맷
    String formatDate(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }
}
