package com.project.sangil_be.service;

import com.project.sangil_be.S3.S3Service;
import com.project.sangil_be.dto.*;
import com.project.sangil_be.model.*;
import com.project.sangil_be.repository.*;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.utils.DistanceToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final UserTitleRepository userTitleRepository;
    private final GetTitleRepository getTitleRepository;
    private final UserRepository userRepository;
    private final CompletedRepository completedRepository;
    private final MountainRepository mountainRepository;
    private final BookMarkRepository bookMarkRepository;
    private final MountainCommentRepository mountainCommentRepository;
    private final S3Service s3Service;

    // 맵트래킹 마이페이지
    public List<CompletedListDto> myPageTracking(UserDetailsImpl userDetails) {
        List<Completed> completed = completedRepository.findAllByUserId(userDetails.getUser().getUserId());
        List<CompletedListDto> completedListDtos = new ArrayList<>();
        for (Completed complete : completed) {
            Mountain mountain = mountainRepository.findByMountainId(complete.getMountainId());
            CompletedListDto completedListDto = new CompletedListDto(complete, mountain);
            completedListDtos.add(completedListDto);
        }
        return completedListDtos;
    }

    // 맵트래킹 마이페이지 산 선택
    public List<CompletedMountainDto> selectMountain(Long mountainId, UserDetailsImpl userDetails) {
        List<Completed> completedList = completedRepository.findAllByMountainIdAndUserId(mountainId, userDetails.getUser().getUserId());
        Mountain mountain = mountainRepository.findByMountainId(mountainId);
        List<CompletedMountainDto> completedMountainDtos = new ArrayList<>();
        for (Completed completed : completedList) {
            CompletedMountainDto completedMountainDto = new CompletedMountainDto(completed, mountain);
            completedMountainDtos.add(completedMountainDto);
        }
        return completedMountainDtos;
    }

    // 닉네임 중복체크
    public String usernameCheck(UsernameRequestDto usernameRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId());
        if (user.getNickname().equals(usernameRequestDto.getNickname())) {
            return "false";
        } else {
            return "true";
        }
    }

    // nickname 수정
    @Transactional
    public UserResponseDto editname(UsernameRequestDto usernameRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId());
        user.editname(usernameRequestDto);
        return new UserResponseDto(user);
    }

    //userimageUrl 수정
    @Transactional
    public void editimage(MultipartFile multipartFile, User user) {

        String[] key = user.getUserImgUrl().split(".com/");
        String imageKey = key[key.length - 1];
        String profileImageUrl = s3Service.reupload(multipartFile, "profileimage", imageKey);

        user.editimage(profileImageUrl);
        userRepository.save(user);
    }

    //유저가 즐겨찾기한 산 가져오는 즐겨찾기
    @Transactional
    public List<BookMarkResponseDto> getBookMarkMountain(double lat, double lng, UserDetailsImpl userDetails) {
        List<BookMark> bookMarkList = bookMarkRepository.findAllByUserId(userDetails.getUser().getUserId());
        List<BookMarkResponseDto> bookMarkResponseDtos = new ArrayList<>();


        for (BookMark bookMark : bookMarkList) {
            boolean bookMarkChk = bookMarkRepository.existsByMountainIdAndUserId(bookMark.getMountainId(),
                    userDetails.getUser().getUserId());
            Mountain mountain = mountainRepository.findById(bookMark.getMountainId()).orElseThrow(
                    () -> new IllegalArgumentException("해당하는 산이 없습니다.")
            );

            //유저와 즐겨찾기한 산과의 거리 계산
            Double distance = DistanceToUser.distance(lat, lng, mountain.getLat(),
                    mountain.getLng(), "kilometer");

            int star = 0;
            Double starAvr = 0d;

            for (int i = 0; i < 10; i++) {
                List<MountainComment> mountainComments = mountainCommentRepository.findAllByMountainId(bookMark.getMountainId());
                if (mountainComments.size() == 0) {
                    starAvr = 0d;
                } else {
                    star += mountainComments.get(i).getStar();
                    starAvr = (double) star / mountainComments.size();
                }
            }
            bookMarkResponseDtos.add(new BookMarkResponseDto(mountain, bookMarkChk, starAvr, distance));
        }
        return bookMarkResponseDtos;
    }

    // 칭호 리스트
    public UserTitleResponseDto getUserTitle(UserDetailsImpl userDetails) {
        List<TitleDto> titleDtoList = new ArrayList<>();
        String userTitle;
        String userTitleImgUrl;
        int cnt = getTitleRepository.countAllByUser(userDetails.getUser());
        if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "내가~~!! 등!!신!!!").isPresent()) {
            System.out.println("패스");
        } else if (cnt == 19) {
            userTitle = "내가~~!! 등!!신!!!";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        }

        List<UserTitle> userTitles = userTitleRepository.findAll();
        List<GetTitle> getTitles = getTitleRepository.findAllByUser(userDetails.getUser());
        HashMap<String, Boolean> title = new HashMap<>();
        for (int i = 0; i < userTitles.size(); i++) {
            title.put(userTitles.get(i).getUserTitle(), false);
            for (int j = 0; j < getTitles.size(); j++) {
                if (userTitles.get(i).getUserTitle().equals(getTitles.get(j).getUserTitle())) {
                    title.replace(userTitles.get(i).getUserTitle(), false, true);
                }
            }
        }

        List<UserTitleDto> userTitleDtos = new ArrayList<>();
        for (int i = 0; i < userTitles.size(); i++) {
            UserTitleDto userTitleDto = new UserTitleDto(userTitles.get(i), title.get(userTitles.get(i).getUserTitle()));
            userTitleDtos.add(userTitleDto);
        }
        return new UserTitleResponseDto(userTitleDtos, titleDtoList);
    }

    // 칭호 변경
    @Transactional
    public ChangeTitleDto putUserTitle(ChangeTitleRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId());
        user.update(requestDto);
        return new ChangeTitleDto(userDetails, requestDto);
    }

}

