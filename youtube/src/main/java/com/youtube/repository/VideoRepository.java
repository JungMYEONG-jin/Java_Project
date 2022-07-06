package com.youtube.repository;

import com.youtube.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {
}
