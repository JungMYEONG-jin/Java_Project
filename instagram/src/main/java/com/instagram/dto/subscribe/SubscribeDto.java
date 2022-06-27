package com.instagram.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscribeDto {

    private Long id;
    private String username;
    private String profileImageUrl;
    private int subscribeState;
    private int equalUser;

}
