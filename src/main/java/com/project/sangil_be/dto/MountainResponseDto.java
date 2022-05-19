package com.project.sangil_be.dto;

import com.project.sangil_be.model.Mountain;
import lombok.Data;

import java.util.List;

@Data
public class MountainResponseDto {
    private Long mountainId;
    private String mountain;
//    private int weather;
//    private String weatherImgUrl;
    private String mountainImgUrl;
    private String mountainAddress;
    private String mountainInfo;
    private Double height;
    private String starAvr;
    private Boolean bookmark;
    private List<CourseListDto> courseLists;
    private CommentDto commentDto;

    public MountainResponseDto(Mountain mountain, Boolean bookmark ,String starAvr, List<CourseListDto> courseLists, CommentDto commentDto) {
        this.mountainId = mountain.getMountainId();
        this.mountain = mountain.getMountain();
//        this.weather = weatherDto.getWeather();
//        this.weatherImgUrl = weatherDto.getWeatherImageUrl();
        this.mountainImgUrl = mountain.getMountainImgUrl();
        this.mountainAddress = mountain.getMountainAddress();
        this.mountainInfo = mountain.getMountainInfo();
        this.height = mountain.getHeight();
        this.starAvr = starAvr;
        this.bookmark=bookmark;
        this.courseLists = courseLists;
        this.commentDto = commentDto;
    }


}
