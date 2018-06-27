package com.project.admin.present_story.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.admin.present_story.FbCode;
import com.project.admin.present_story.Model.StoryModel;
import com.project.admin.present_story.Model.UserModel;
import com.project.admin.present_story.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateStoryActivity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_uid;
    Button btn_create;
    Button btn_add;
    String uid;
    RecyclerView recyclerView;
    UidAdapter uidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        editText_name = findViewById(R.id.createStoryActivity_edittext_storyName);
        editText_uid = findViewById(R.id.createStoryActivity_edittext_uid);
        btn_create = findViewById(R.id.createStoryActivity_btn_create);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_name.getText().toString()) || TextUtils.isEmpty(editText_uid.getText().toString())) {
                    Toast.makeText(CreateStoryActivity.this, "스토리명 또는 코드 중 입력되지 않은 항목이 있습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                final StoryModel storyModel = new StoryModel();
                //TODO : 스토리 생성 시 복수의 uid (code) 받을 방법 추가
//                FirebaseDatabase.getInstance().getReference().child(FbCode.ALL_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for(DataSnapshot item : dataSnapshot.getChildren()) {
//                            UserModel userModel = item.getValue(UserModel.class);
//                            if(userModel.uid.equals(editText_uid.getText().toString()) && !userModel.hasStory) {
//                                storyModel.users.put(editText_uid.getText().toString(), true);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                storyModel.users.put("nLMtIl2kvsM5vKJCugoIm8M8nGO2", true);
                storyModel.users.put(uid, true);

                if (storyModel.users.size() < 2) {
                    Toast.makeText(CreateStoryActivity.this, "유효한 코드가 2개 이상이 되지 못하여 Story 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                storyModel.storyName = editText_name.getText().toString();
                storyModel.storyToken = FirebaseInstanceId.getInstance().getToken();

                // 스토리 생성
                FirebaseDatabase.getInstance().getReference().child(FbCode.STORY).child(storyModel.storyToken).setValue(storyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //스토리 참여 유저 hasStory = true
                        for(String uidItem : storyModel.users.keySet()) {
                            FirebaseDatabase.getInstance().getReference().child(FbCode.ALL_USERS).child(uidItem).child("hasStory").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CreateStoryActivity.this.finish();
                                }
                            });
                        }
                    }
                });
            }
        });

        recyclerView = findViewById(R.id.createStoryActivity_recyclerview);

        uidAdapter = new UidAdapter();
        recyclerView.setAdapter(uidAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_add = findViewById(R.id.createStoryActivity_btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uidAdapter.addUid(editText_uid.getText().toString());
            }
        });
    }

    class UidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<String> uids = new ArrayList<>();

        public UidAdapter() {
        }

        public void addUid(String uid) {
            if(TextUtils.isEmpty(uid)) {
                uids.add(uid);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uid, null);
            return new UidViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            UidViewHolder viewHolder = (UidViewHolder)holder;
            viewHolder.textView_uid.setText(uids.get(position));
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    uids.remove(position);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return uids.size();
        }

        private class UidViewHolder extends RecyclerView.ViewHolder {
            TextView textView_uid;
            public UidViewHolder(View view) {
                super(view);
                textView_uid = view.findViewById(R.id.uidItem_textview_uid);
            }
        }
    }


}