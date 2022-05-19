package com.project.sangil_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class TitleResponseDto {
    private List<TitleDto> titleDtoList;
    private String msg;

    public TitleResponseDto(List<TitleDto> titleDtoList, String msg) {
        this.titleDtoList = titleDtoList;
        this.msg = msg;
    }
}
