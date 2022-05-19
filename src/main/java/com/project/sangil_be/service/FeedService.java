package com.project.sangil_be.service;

import com.project.sangil_be.S3.S3Service;
import com.project.sangil_be.dto.FeedListResponseDto;
import com.project.sangil_be.dto.FeedResponseDto;
import com.project.sangil_be.dto.GoodCheckResponseDto;
import com.project.sangil_be.dto.TitleDto;
import com.project.sangil_be.model.*;
import com.project.sangil_be.repository.CompletedRepository;
import com.project.sangil_be.repository.FeedRepository;
import com.project.sangil_be.repository.GetTitleRepository;
import com.project.sangil_be.repository.GoodRepository;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class FeedService {
    private final S3Service s3Service;
    private final FeedRepository feedRepository;
    private final GoodRepository goodRepository;
    private final GetTitleRepository getTitleRepository;

    // 피드 작성
    public FeedResponseDto saveFeed(String feedContent, MultipartFile multipartFile, UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        String feedImgUrl = s3Service.upload(multipartFile, "feedimage");

        Feed feed = new Feed(feedContent, feedImgUrl, user);
        feedRepository.save(feed);

        boolean goodStatus = goodRepository.existsByFeedIdAndUserId(feed.getFeedId(), user.getUserId());

        List<TitleDto> titleDtoList = new ArrayList<>();
        String userTitle;
        String userTitleImgUrl;
        Long cnt = feedRepository.countAllByUser(user);
        if (getTitleRepository.findByUserAndUserTitle(userDetails.getUser(), "예비 찰칵러").isPresent()) {
            System.out.println("패스");
        } else if (cnt == 1) {
            userTitle = "예비 찰칵러";
            userTitleImgUrl = "";
            GetTitle getTitle = new GetTitle(userTitle, userTitleImgUrl, user);
            getTitleRepository.save(getTitle);
            titleDtoList.add(new TitleDto(userTitle, userTitleImgUrl));
        }

        FeedResponseDto feedResponseDto = new FeedResponseDto(user, feed, 0, goodStatus, titleDtoList);
        return feedResponseDto;

    }


    public GoodCheckResponseDto goodCheck(Long feedId, User user) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("피드가 없습니다.")
        );

        Optional<Good> good = goodRepository.findByFeedIdAndUserId(feedId, user.getUserId());

        if (!good.isPresent()) {
            Good savegood = new Good(feed, user);
            goodRepository.save(savegood);

        } else {
            goodRepository.deleteById(good.get().getGoodId());

        }
        boolean goodStatus = goodRepository.existsByFeedIdAndUserId(feedId, user.getUserId());

        Long goodCnt = goodRepository.countAllByFeedId(feedId);
        GoodCheckResponseDto goodCheckResponseDto = new GoodCheckResponseDto(goodCnt, goodStatus);

        return goodCheckResponseDto;
    }

    @Transactional
    public void deletefeed(Long feedId, Optional<Feed> feed) {

        feedRepository.deleteById(feedId);
        goodRepository.deleteByFeedId(feedId);

        String[] key = feed.get().getFeedImgUrl().split(".com/");
        String imageKey = key[key.length - 1];

        s3Service.deletefeed(imageKey);

    }

    public FeedResponseDto detail(Long feedId, User user) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("해당 피드가 없습니다.")
        );

        int goodCnt = goodRepository.findByFeedId(feedId).size();

        boolean goodStatus = goodRepository.existsByFeedIdAndUserId(feed.getFeedId(), user.getUserId());

        FeedResponseDto feedResponseDto = new FeedResponseDto(feed, goodCnt, goodStatus);
        return feedResponseDto;
    }

    public FeedListResponseDto myfeeds(User user, int pageNum) {

        List<Feed> feed = feedRepository.findByUser(user);

        List<FeedResponseDto> feedResponseDtos = new ArrayList<>();

        for (Feed feeds : feed) {

            int goodCnt = goodRepository.findByFeedId(feeds.getFeedId()).size();
            boolean goodStatus = goodRepository.existsByFeedIdAndUserId(feeds.getFeedId(), user.getUserId());

            feedResponseDtos.add(new FeedResponseDto(feeds, goodCnt, goodStatus));

        }
        Pageable pageable = getPageable(pageNum);

        int start = pageNum * 15;
        int end = Math.min((start + 15), feed.size());

        Page<FeedResponseDto> page = new PageImpl<>(feedResponseDtos.subList(start, end), pageable, feedResponseDtos.size());
        return new FeedListResponseDto(page);
    }

    private Pageable getPageable(int pageNum) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(pageNum, 15, sort);
    }
}
