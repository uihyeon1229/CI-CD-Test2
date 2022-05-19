//package com.project.sangil_be.service;
//
//import com.project.sangil_be.dto.CommentListDto;
//import com.project.sangil_be.dto.SearchDto;
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static com.project.sangil_be.model.QMountain.mountain1;
//import static com.project.sangil_be.model.QMountainComment.mountainComment1;
//import static com.project.sangil_be.model.QUser.user;
//
//@SpringBootTest
//class MountainServiceTest {
//
//    @Autowired
//    EntityManager em;
//    JPAQueryFactory queryFactory;
//
//    @BeforeEach
//    public void before() {
//        queryFactory = new JPAQueryFactory(em);
//    }
//
////    @Test
////    void searchMountain() {
////        String keyword = "악";
////        PageRequest pageable = PageRequest.of(1, 5);
////
////        Page<SearchDto> searchDtos = mountainRepository.searchPage(keyword, pageable);
////        for (SearchDto searchDto : searchDtos) {
////            System.out.println("searchDto = " + searchDto);
////        }
////    }
//
//    @Test
//    void searchMountain2() {
//        String keyword = "악";
//        QueryResults<SearchDto> results = queryFactory
//                .select(Projections.constructor(SearchDto.class,
//                        mountain1.mountainId,
//                        mountain1.mountain,
//                        mountain1.mountainAddress,
//                        mountain1.mountainImgUrl,
//                        mountainComment1.star.avg().as("starAvr"),
//                        mountain1.lat,
//                        mountain1.lng))
//                .from(mountain1)
//                .leftJoin(mountainComment1).on(mountainComment1.mountainId.eq(mountain1.mountainId))
//                .where(mountain1.mountain.contains(keyword).or(mountain1.mountainAddress.contains(keyword)))
//                .groupBy(mountain1.mountainId)
//                .offset(0)
//                .limit(5)
//                .fetchResults();
//
//        List<SearchDto> content = results.getResults();
//        for (SearchDto searchDto : content) {
//            System.out.println("searchDto = " + searchDto);
//        }
//
//    }
//
//    @Test
//    void searchMountain3() {
//        Long mountainId = 1L;
//        QueryResults<CommentListDto> results = queryFactory
//                .select(Projections.constructor(CommentListDto.class,
//                        mountainComment1.mountainCommentId,
//                        mountainComment1.mountainComment,
//                        user.userId,
//                        user.nickname,
//                        user.userTitle,
//                        mountainComment1.star,
//                        mountainComment1.createdAt))
//                .from(mountainComment1)
//                .join(user).on(mountainComment1.userId.eq(user.userId))
//                .where(mountainComment1.mountainId.eq(mountainId))
//                .offset(0)
//                .limit(5)
//                .fetchResults();
//
//        List<CommentListDto> content = results.getResults();
//        for (int i = 0; i < content.size(); i++) {
//            System.out.println(content.get(i).getUserId());
//        }
//    }
//
////    @Test
////    void searchMountain4() {
////        Long mountainId = 1L;
////        List<MountainResponseDto> fetch = queryFactory
////                .select(Projections.constructor(MountainResponseDto.class,
////                        m.mountainId,
////                        m.mountain,
////                        m.mountainImgUrl,
////                        m.mountainAddress,
////                        m.mountainInfo,
////                        m.height,
////                        c.star.avg().as("starAvr"),
////                        Projections.constructor(CourseListDto.class,
////                                course1.courseId,
////                                course1.course,
////                                course1.courseTime).as("courseDto")))
////                .from(m)
////                .innerJoin(c).on(c.mountainId.eq(c.mountainId))
////                .innerJoin(course1).on(c.mountainId.eq(course1.mountainId))
////                .where(m.mountainId.eq(mountainId))
////                .fetch();
////        for (MountainResponseDto mountainResponseDto : fetch) {
////            System.out.println("mountainResponseDto = " + mountainResponseDto);
////        }
////    }
//
//
//}