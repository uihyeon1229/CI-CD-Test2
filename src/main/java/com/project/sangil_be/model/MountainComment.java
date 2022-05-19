package com.project.sangil_be.model;

import com.project.sangil_be.dto.MCommentRequestDto;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.utils.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MountainComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mountainCommentId;

    @Column(nullable = false)
    private Long mountainId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String mountainComment;

    @Column(nullable = false)
    private Integer star;

    public MountainComment(Long mountainId, UserDetailsImpl userDetails, MCommentRequestDto mCommentRequestDto) {
        this.mountainId = mountainId;
        this.userId = userDetails.getUser().getUserId();
        this.mountainComment = mCommentRequestDto.getMountainComment();
        this.star = mCommentRequestDto.getStar();
    }

    public void update(MountainComment mountainComment, MCommentRequestDto mCommentRequestDto, UserDetailsImpl userDetails) {
        this.mountainId=mountainComment.getMountainId();
        this.mountainComment = mCommentRequestDto.getMountainComment();
        this.star = mCommentRequestDto.getStar();
        this.userId=userDetails.getUser().getUserId();
    }
}