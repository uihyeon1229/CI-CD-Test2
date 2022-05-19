package com.project.sangil_be.dto;

import lombok.Data;

@Data
public class TitleDto {
    private String userTitle;
    private String userTitleImgUrl;

    public TitleDto(String userTitle, String userTitleImgUrl) {
        this.userTitle = userTitle;
        this.userTitleImgUrl = userTitleImgUrl;
    }
}
