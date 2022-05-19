package com.project.sangil_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoodCheckResponseDto {
    boolean goodStatus;
    Long goodCnt;

    public GoodCheckResponseDto(Long goodCnt, boolean goodStatus) {
        this.goodCnt=goodCnt;
        this.goodStatus = goodStatus;
    }
}
