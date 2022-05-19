package com.project.sangil_be.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class FeedListResponseDto {
    private List<FeedResponseDto> feedList;
    private List<TitleDto> titleDtoList;
    private int totalPage;
    private int currentPage;

    public FeedListResponseDto(Page<FeedResponseDto> page, List<TitleDto> titleDtoList) {
        this.feedList=page.getContent();
        this.titleDtoList = titleDtoList;
        this.totalPage=page.getTotalPages();
        this.currentPage=page.getNumber();
    }

    public FeedListResponseDto(Page<FeedResponseDto> page) {
        this.feedList=page.getContent();
        this.totalPage=page.getTotalPages();
        this.currentPage=page.getNumber();
    }
}
