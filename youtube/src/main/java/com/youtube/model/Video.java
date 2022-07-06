package com.youtube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value = "Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    private String id;
    private String title;
    private String description;
    private String userId;
    private AtomicInteger likes = new AtomicInteger(0);
    private AtomicInteger disLikes = new AtomicInteger(0);
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private AtomicInteger viewCount = new AtomicInteger(0);
    private String thumbnailUrl;
    private List<Comment> commentList = new CopyOnWriteArrayList<>(); // 수정할 일 없고 조회할 일이 많은 경우 사용, 쓰레드에 안전하다
    // ArrayList + Synchronized 방식 보다 빠름

    public void incrementLikes(){
        likes.incrementAndGet();
    }

    public void decrementLikes(){
        likes.decrementAndGet();
    }

    public void incrementDisLikes(){
        disLikes.incrementAndGet();
    }

    public void decrementDisLikes(){
        disLikes.decrementAndGet();
    }

    public void incrementViewCount(){
        viewCount.incrementAndGet();
    }

    public void addComment(Comment comment){
        commentList.add(comment);
    }





}
