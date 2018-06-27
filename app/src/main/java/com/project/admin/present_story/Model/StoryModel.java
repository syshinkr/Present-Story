package com.project.admin.present_story.Model;

import java.util.HashMap;
import java.util.Map;

public class StoryModel {
    public String storyName;
    public Map<String, Object> users = new HashMap<>(); // 채팅방 유저들
    public Map<String, Comment> comments = new HashMap<>(); //채팅방의 대화내용
    public String storyToken;

    public static class Comment {
        public String uid;
        public String message;
        public Object timestamp;
        public Map<String, Object> readUsers = new HashMap<>();
    }

    public StoryModel() {}
}
