package com.project.admin.present_story.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.admin.present_story.FbCode;
import com.project.admin.present_story.R;

import java.util.HashMap;
import java.util.Map;

public class CheckStoryActivity extends AppCompatActivity {
    String uid;
    String targetStory; //storyToken
    TextView textView_code;
    Button btn_clipboard;
    Button btn_create;
    ClipboardManager clipboardManager;

    DatabaseReference hasStoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_story);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        textView_code = findViewById(R.id.checkStoryActivity_textview_uid);
        textView_code.setText(uid);

        btn_clipboard = findViewById(R.id.checkStoryActivity_button_clipboard);
        btn_clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", uid);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(CheckStoryActivity.this, "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        btn_create = findViewById(R.id.checkStoryActivity_button_createStory);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckStoryActivity.this, CreateStoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        checkHasStory();
        passPushTokenToServer();
    }

    // 유저가 참여 중인 스토리가 있는지 확인
    private void checkHasStory() {
        hasStoryReference = FirebaseDatabase.getInstance().getReference().child(FbCode.ALL_USERS).child(uid).child("hasStory");
        hasStoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean hasStory = (Boolean) dataSnapshot.getValue();
                if (hasStory) {
                    getTargetStory();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTargetStory() {
        FirebaseDatabase.getInstance().getReference().child(FbCode.STORY).orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 헷갈려서 적어놓는 설명
                // 리턴은  story/????/users/uid가 일치하는 story를 가져온다 (즉, 1개뿐이다 / 여기서 ????은 storyToken을 의미한다.)
                // Map < String "story",  Map<String storyToken, StoryModel> > 형식이어서 getChildren을 한 후, getKey()를 하면 storyToken을 받을 수 있게 되는 것이다.
                // 그렇다고 다짜고짜 제일 먼저 getkey()부터 한다고 story가 들어가는 것은 아니다 --> 이유불명
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    targetStory = item.getKey();
                }

                savePref("targetStory", targetStory);
                Intent intent = new Intent(CheckStoryActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                CheckStoryActivity.this.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        //푸시토큰을 넣어주는 방법은 HashMap 말고 없다
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child(FbCode.ALL_USERS).child(uid).updateChildren(map); //setValue()는 모든 데이터를 덮어써버린다. updateChildren()은 기존데이터에 추가한다.
    }

    // 값 저장하기
    private void savePref(String key, String value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
