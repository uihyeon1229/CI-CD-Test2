package com.project.sangil_be.dto;

import com.project.sangil_be.model.Completed;
import com.project.sangil_be.model.Mountain;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CompletedListDto {
    private Long completedId;
    private Long mountainId;
    private String mountain;
    private Double lat;
    private Double lng;
    private Double totalDistance;
    private String totalTime;
    private LocalDateTime createAt;

    public CompletedListDto(Completed complete, Mountain mountain) {
        this.completedId = complete.getCompleteId();
        this.mountainId = complete.getMountainId();
        this.mountain = mountain.getMountain();
        this.lat = mountain.getLat();
        this.lng = mountain.getLng();
        this.totalDistance = complete.getTotalDistance();
        this.totalTime = complete.getTotalTime();
        this.createAt = complete.getCreatedAt();
    }
}
