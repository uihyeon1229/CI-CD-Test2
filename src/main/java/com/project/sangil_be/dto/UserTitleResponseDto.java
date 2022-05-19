package com.project.sangil_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserTitleResponseDto {
    private List<UserTitleDto> userTitleDtoList;
    private List<TitleDto> titleDtoList;

    public UserTitleResponseDto(List<UserTitleDto> userTitleDtoList, List<TitleDto> titleDtoList) {
        this.userTitleDtoList = userTitleDtoList;
        this.titleDtoList = titleDtoList;
    }
}
