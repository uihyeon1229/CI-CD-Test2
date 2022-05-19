package com.project.sangil_be.service;

import com.project.sangil_be.dto.MCommentRequestDto;
import com.project.sangil_be.dto.MCommentResponseDto;
import com.project.sangil_be.dto.TitleDto;
import com.project.sangil_be.model.GetTitle;
import com.project.sangil_be.model.Mountain;
import com.project.sangil_be.model.MountainComment;
import com.project.sangil_be.repository.GetTitleRepository;
import com.project.sangil_be.repository.MountainRepository;
import com.project.sangil_be.repository.MountainCommentRepository;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MCommentService {
    private final MountainRepository mountainRepository;
    private final MountainCommentRepository mountainCommentRepository;
    private final Validator validator;
    private final GetTitleRepository getTitleRepository;

    // 댓글 작성
    public MCommentResponseDto writeComment(Long mountainId, MCommentRequestDto mCommentRequestDto, UserDetailsImpl userDetails) {
        List<MountainComment> comment = mountainCommentRepository.findAllByMountainIdAndUserId(mountainId,userDetails.getUser().getUserId());
        String msg;
        if (comment.size()>=1) {
            msg = "중복";
        } else {
            msg = "작성 가능";
        }
        Mountain mountain = mountainRepository.findById(mountainId).orElseThrow(
                () -> new IllegalArgumentException("산 정보가 존재하지 않습니다.")
        );
        validator.emptyMComment(mCommentRequestDto);

        MountainComment mountainComment = new MountainComment(mountainId, userDetails, mCommentRequestDto);
        mountainCommentRepository.save(mountainComment);

        List<TitleDto> titleDtoList = new ArrayList<>();
        String userTitle;
        String userTitleImgUrl;
        Long cnt = mountainCommentRepository.countAllByUserId(userDetails.getUser().getUserId());
        if (cnt == 10) {
            userTitle = "세르파";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        }

        MCommentResponseDto mCommentResponseDto = new MCommentResponseDto(
                mountainComment,
                userDetails,
                msg,
                titleDtoList
        );
        return mCommentResponseDto;
    }

    // 댓글 수정
    @Transactional
    public MCommentResponseDto updateComment(Long mountainCommentId, MCommentRequestDto mCommentRequestDto, UserDetailsImpl userDetails) {

        MountainComment mountainComment = mountainCommentRepository.findById(mountainCommentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        mountainComment.update(mountainComment,mCommentRequestDto,userDetails);
        return new MCommentResponseDto(mountainComment, userDetails);
    }

    // 댓글 삭제
    public String deleteComment(Long mountainCommentId) {
        MountainComment mountainComment = mountainCommentRepository.findById(mountainCommentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        try {
            mountainCommentRepository.deleteById(mountainCommentId);
            return "true";
        } catch (IllegalArgumentException e) {
            return "false";
        }
    }
}
