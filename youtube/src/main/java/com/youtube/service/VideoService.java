package com.youtube.service;

import com.youtube.dto.CommentDto;
import com.youtube.dto.UploadVideoResponse;
import com.youtube.dto.VideoDto;
import com.youtube.model.Comment;
import com.youtube.model.Video;
import com.youtube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserService userService;

    
    public UploadVideoResponse uploadVideo(MultipartFile file){
        String videoUrl = s3Service.uploadFile(file);
        Video video = new Video();
        video.setVideoUrl(videoUrl);

        Video savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto){
        Video savedVideo = getVideoById(videoDto.getId());
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId){
        Video savedVideo = getVideoById(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo); // update
        return thumbnailUrl;
    }



    private Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Can not find video bt id " + id));
    }

    public VideoDto getVideoDetails(String videoId){
        Video savedVideo = getVideoById(videoId);
        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId);

        return mapToVideoDto(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDto likeVideo(String videoId) {
        Video savedVideo = getVideoById(videoId);

        if(userService.ifLikedVideo(videoId)){
            savedVideo.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        }else if(userService.ifDisLikedVideo(videoId)){
            savedVideo.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
            savedVideo.incrementLikes();
            userService.addToLikesVideos(videoId);
        }else{
            savedVideo.incrementLikes();
            userService.addToLikesVideos(videoId);
        }

        videoRepository.save(savedVideo);
        return mapToVideoDto(savedVideo);
    }

    public VideoDto disLikeVideo(String videoId){

        Video savedVideo = getVideoById(videoId);
        if(userService.ifDisLikedVideo(videoId)){
            savedVideo.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
        }else if(userService.ifLikedVideo(videoId)){
            savedVideo.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            savedVideo.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }else{
            savedVideo.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }

        videoRepository.save(savedVideo);
        return mapToVideoDto(savedVideo);


    }



    private VideoDto mapToVideoDto(Video savedVideo) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setDislikeCount(savedVideo.getDisLikes().get());
        videoDto.setViewCount(savedVideo.getViewCount().get());
        videoDto.setLikeCount(savedVideo.getLikes().get());
        videoDto.setId(savedVideo.getId());
        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto){
        Video savedVideo = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getContent());
        comment.setAuthorId(commentDto.getAuthorId());

        savedVideo.addComment(comment);
        videoRepository.save(savedVideo);
    }

    public List<CommentDto> getAllComments(String videoId){
        Video savedVideo = getVideoById(videoId);
        List<Comment> commentList = savedVideo.getCommentList();
        return commentList.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorId(commentDto.getAuthorId());
        commentDto.setContent(comment.getText());
        return commentDto;
    }

    public List<VideoDto> getAllVideos(){
        return videoRepository.findAll().stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }


}
