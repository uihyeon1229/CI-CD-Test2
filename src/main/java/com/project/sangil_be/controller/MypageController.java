package com.project.sangil_be.controller;

import com.project.sangil_be.dto.*;
import com.project.sangil_be.model.User;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    // 맵트래킹 마이페이지
    @GetMapping("/api/mypages/tracking")
    public List<CompletedListDto> myPageTracking(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.myPageTracking(userDetails);
    }

    // 맵트래킹 마이페이지 산 선택
    @GetMapping("/api/mypages/tracking/{mountainId}")
    public List<CompletedMountainDto> selectMountain(@PathVariable Long mountainId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.selectMountain(mountainId,userDetails);
    }

    // 닉네임 중복체크
    @PostMapping("/api/mypages/usernameCheck")
    public String usernameCheck (@RequestBody UsernameRequestDto usernameRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.usernameCheck(usernameRequestDto,userDetails);
    }

    // nickname 수정
    @PutMapping("/api/mypages/profilename")
    public UserResponseDto editname(@RequestBody UsernameRequestDto usernameRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.editname(usernameRequestDto, userDetails);
    }

    // userimageUrl 수정
    @PutMapping("/api/mypages/profileUrl")
    public void editimage(@RequestParam("file") MultipartFile multipartFile, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        mypageService.editimage(multipartFile, user);
    }

    // 마이페이지 즐겨찾기한 산
    @GetMapping("/api/mypages/bookmark")
    public List<BookMarkResponseDto> getBookMarkMountain (@RequestParam double lat, @RequestParam double lng, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getBookMarkMountain(lat,lng,userDetails);
    }

    // 칭호 리스트
    @GetMapping("/api/mypages/userTitle")
    public UserTitleResponseDto getUserTitle(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getUserTitle(userDetails);
    }

    // 칭호 변경
    @PutMapping("/api/mypages/userTitle")
    public ChangeTitleDto putUserTitle(@RequestBody ChangeTitleRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.putUserTitle(requestDto, userDetails);
    }
}
