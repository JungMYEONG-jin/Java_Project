package com.instagram.dto.image;

import com.instagram.entity.Image;
import com.instagram.entity.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadDto {

    private MultipartFile file;
    private String caption;

    public Image toImage(String postImageUrl, User user){
        return Image.builder().caption(caption).user(user).postImageUrl(postImageUrl).build();
    }
}
