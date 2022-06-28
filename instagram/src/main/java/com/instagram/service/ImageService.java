package com.instagram.service;

import com.instagram.config.auth.PrincipalDetails;
import com.instagram.dto.image.ImageUploadDto;
import com.instagram.entity.Image;
import com.instagram.entity.User;
import com.instagram.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    @Value("${file.path}")
    private String uploadDir;

    @Transactional
    public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails){
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename();

        Path filePath = Paths.get(uploadDir + fileName);
        // 런타임 예외 발생할 수 있기 때문에 예외 처리
        try{
            Files.write(filePath, imageUploadDto.getFile().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        //DB 저장
        Image image = imageUploadDto.toImage(fileName, principalDetails.getUser());
        imageRepository.save(image);
    }

    /**
     * top 10개까지만 순위를 가져옴.
     * @param principal
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Image> imageStory(User principal, Pageable pageable){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Image> imageByPaging = imageRepository.getImageByPaging(principal, pageRequest);

        imageByPaging.forEach(i -> {
            i.setLikeCnt(i.getLikes().size());
            i.getLikes().forEach(like -> {
                if(like.getUser().getId() == principal.getId()){ // entity 간 비교를 위해 기본키 활용 항상 영속 상태여야 한다는 전제가 필요함.
                    i.setLikeState(true);
                }
            });
        });
        return imageByPaging;
    }

    @Transactional(readOnly = true)
    public List<Image> getPopularImages(){
        return imageRepository.getImagesByPopularDesc();
    }


}
