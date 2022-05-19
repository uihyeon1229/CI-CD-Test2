package com.project.sangil_be.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CommentDto {
    private List<CommentListDto> commentLists;
    private int totalPage;
    private int currentPage;

    public CommentDto(Page<CommentListDto> page) {
        this.commentLists = page.getContent();
        this.totalPage = page.getTotalPages();
        this.currentPage = page.getNumber();
    }
}
