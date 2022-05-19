package com.project.sangil_be.service;

import com.project.sangil_be.dto.*;
import com.project.sangil_be.model.Completed;
import com.project.sangil_be.model.GetTitle;
import com.project.sangil_be.model.Mountain;
import com.project.sangil_be.model.Tracking;
import com.project.sangil_be.repository.*;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.utils.DistanceToUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrackingService {
    private final TrackingRepository trackingRepository;
    private final CompletedRepository completedRepository;
    private final MountainRepository mountainRepository;
    private final MountainCommentRepository mountainCommentRepository;
    private final GetTitleRepository getTitleRepository;

    // 트래킹 시작
    @Transactional
    public StartTrackingResponseDto startMyLocation(Long mountainId, StartTrackingRequestDto startTrackingRequestDto, UserDetailsImpl userDetails) {
        Completed completed = new Completed(mountainId, startTrackingRequestDto.getSend(), userDetails.getUser().getUserId());
        completedRepository.save(completed);
        return new StartTrackingResponseDto(completed.getCompleteId());
    }

    // 맵 트래킹 5초 마다 저장
    @Transactional
    public DistanceResponseDto saveMyLocation(Long completedId, TrackingRequestDto trackingRequestDto, UserDetailsImpl userDetails) {
        List<Tracking> trackinglist = trackingRepository.findAllByCompletedId(completedId);
        Completed completed = completedRepository.findByCompleteId(completedId);
        Mountain mountain = mountainRepository.findByMountainId(completed.getMountainId());
        Tracking saveTracking = new Tracking();
        saveTracking.setMountain(mountain);
        saveTracking.setUser(userDetails.getUser());
        DistanceResponseDto distanceResponseDto = new DistanceResponseDto();
        if (trackinglist.size() == 0) {
            saveTracking.setCompletedId(completedId);
            saveTracking.setLat(trackingRequestDto.getLat());
            saveTracking.setLng(trackingRequestDto.getLng());
            Double distanceM = 0d;
            Double distanceK = 0d;
            saveTracking.setDistanceM(distanceM);
            saveTracking.setDistanceK(distanceK);
            trackingRepository.save(saveTracking);
            distanceResponseDto.setDistanceK(String.format("%.2f", distanceK));
            distanceResponseDto.setDistanceM(String.format("%.2f", distanceM));
        } else {
            for (int i = trackinglist.size() - 1; i < trackinglist.size(); i++) {
                Double distanceM = DistanceToUser.distance(trackinglist.get(i).getLat(), trackinglist.get(i).getLng(), trackingRequestDto.getLat(), trackingRequestDto.getLng(), "meter");
                Double distanceK = DistanceToUser.distance(trackinglist.get(i).getLat(), trackinglist.get(i).getLng(), trackingRequestDto.getLat(), trackingRequestDto.getLng(), "kilometer");
                saveTracking.setCompletedId(completedId);
                saveTracking.setLat(trackingRequestDto.getLat());
                saveTracking.setLng(trackingRequestDto.getLng());
                saveTracking.setDistanceM(distanceM);
                saveTracking.setDistanceK(distanceK);
                trackingRepository.save(saveTracking);
                distanceResponseDto.setDistanceK(String.format("%.2f", distanceK));
                distanceResponseDto.setDistanceM(String.format("%.2f", distanceM));
            }
        }
        return distanceResponseDto;
    }

    // 트래킹 완료 후 저장
    @Transactional
    public TitleResponseDto saveTracking(Long completedId, CompleteRequestDto completeRequestDto, UserDetailsImpl userDetails) {

        List<Completed> completedList = completedRepository.findAllByUserId(userDetails.getUser().getUserId());
        List<TitleDto> titleDtoList = new ArrayList<>();
        double distance = 0d;
        double height = 0d;
        for (Completed completed : completedList) {
            Mountain mountain = mountainRepository.findByMountainId(completed.getMountainId());
            distance += completed.getTotalDistance();
            height += mountain.getHeight();
        }
        String userTitle;
        String userTitleImgUrl;
        int cnt = completedList.size();
        if (cnt == 1) {
            userTitle = "방구석 홍길";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        } else if (cnt == 3) {
            userTitle = "리틀홍길";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        } else if (cnt == 10) {
            userTitle = "내장래희망 홍길형님";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        } else if (cnt == 100) {
            userTitle = "UM.....홍길?";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        }

        if (distance >= 1 && distance < 100) {
            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "아직 여기라고?").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "아직 여기라고?";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
            }
        } else if (distance >= 100 && distance < 1000) {

            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "백만불짜리다리").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "백만불짜리다리?";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
            }
        } else if (distance > 1000) {
            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "산타고 전국일주").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "산타고 전국일주";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
            }
        }

        if (height >= 10 && height < 30) {
            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "내가 탄 산 높이 히말라야").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "내가 탄 산 높이 히말라야";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                TitleDto titleDto = new TitleDto(userTitle, userTitleImgUrl);
                titleDtoList.add(titleDto);
            }
        } else if (height >= 30 && height < 1000) {

            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "구름위를걷는자").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "구름위를걷는자";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                TitleDto titleDto = new TitleDto(userTitle, userTitleImgUrl);
                titleDtoList.add(titleDto);
            }
        } else if (height > 1000) {
            if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "대기권 돌파~").isPresent()) {
                System.out.println("패스");
            } else {
                userTitle = "대기권 돌파~";
                userTitleImgUrl = "";
                GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, userDetails.getUser());
                getTitleRepository.save(getTitle);
                TitleDto titleDto = new TitleDto(userTitle, userTitleImgUrl);
                titleDtoList.add(titleDto);
            }
        }

        Completed completed = completedRepository.findByCompleteId(completedId);
        String msg;
        if (mountainCommentRepository.existsByUserIdAndMountainId(completed.getUserId(), completed.getMountainId())) {
            completed.update(completeRequestDto);
            msg = "true";
        } else {
            completed.update(completeRequestDto);
            msg = "false";
        }

        return new TitleResponseDto(titleDtoList, msg);
    }


    // 맵트래킹 삭제 (10분 이하)
    @Transactional
    public String deleteTracking(Long completedId) {
        try {
            completedRepository.deleteByCompleteId(completedId);
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    // 맵 트래킹 상세페이지
    @Transactional
    public TrackingListDto detailTracking(Long completedId, UserDetailsImpl userDetails) {
        List<Tracking> trackingList = trackingRepository.findAllByCompletedId(completedId);
        Completed completed = completedRepository.findByCompleteId(completedId);
        List<TrackingResponseDto> trackingResponseDtoList = new ArrayList<>();
        Mountain mountain = mountainRepository.findByMountainId(completed.getMountainId());
        for (Tracking tracking : trackingList) {
            TrackingResponseDto trackingResponseDto = new TrackingResponseDto(tracking.getLat(), tracking.getLng());
            trackingResponseDtoList.add(trackingResponseDto);
        }
        return new TrackingListDto(userDetails, completedId, mountain, completed, trackingResponseDtoList);
    }

}
