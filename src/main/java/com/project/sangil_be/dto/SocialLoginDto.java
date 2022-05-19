package com.project.sangil_be.dto;

import lombok.Getter;

@Getter
public class SocialLoginDto {
    private String username;
    private String nickname;
    private String socialId;

    public SocialLoginDto(String username, String nickname, String socialId) {
        this.username = username;
        this.nickname = nickname;
        this.socialId = socialId;
    }
}
