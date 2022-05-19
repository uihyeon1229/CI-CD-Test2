package com.project.sangil_be.dto;

import com.project.sangil_be.model.Attend;
import com.project.sangil_be.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartymemberDto {
    private String nickname;
    private String userImageUrl;
    private String userTitle;


    public PartymemberDto(User user) {
        this.nickname = user.getNickname();
        this.userImageUrl = user.getUserImgUrl();
        this.userTitle = user.getUserTitle();
    }
}
