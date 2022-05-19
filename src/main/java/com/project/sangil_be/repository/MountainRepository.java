package com.project.sangil_be.repository;

import com.project.sangil_be.model.Mountain;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MountainRepository extends JpaRepository<Mountain, Long> ,MountainRepositoryCustom{
    // jpql
//    @Query("select u from Mountain u where u.mountain like %:keyword% or u.mountainAddress like %:keyword%")
//    List<Mountain> searchAllByMountain(@Param("keyword") String keyword);

    // jpql
    @Query("select e from Mountain e where e.lat between :x2 and :x1 and e.lng between :y2 and :y1")
    List<Mountain> findAll(@Param("x2")double x2, @Param("x1") double x1, @Param("y2") double y2, @Param("y1") double y1);

    Mountain findByMountainId(Long mountainId);

    // from절 안의 서브쿼리는 jpql이나 querydsl이 지원되지 않아 native query 사용
    @Query(nativeQuery = true,value = "select m.*,c.avrStar from (select m.mountain_id,m.mountain,m.mountain_address,m.mountain_img_url, count(bm.mountain_id) as cnt from mountain m left join book_mark bm on m.mountain_id = bm.mountain_id group by m.mountain_id order by cnt desc) m left join (select c.mountain_id, avg(c.star) as avrStar from mountain_comment c group by mountain_id) c on m.mountain_id=c.mountain_id limit 10" )
    List<Map<String,Object>> methodlist();

    // order by rand()는 jpql이나 querydsl이 지원되지 않아 native query 사용
    @Query(nativeQuery = true, value = "select m.mountain_id,m.mountain,m.mountain_address,m.mountain_img_url,m.lat,m.lng,avg(c.star) as avrStar from mountain m left join mountain_comment c on m.mountain_id=c.mountain_id where m.mountain_id between 1 and 100 group by mountain_id order by rand() limit 10")
    List<Map<String,Object>> beforeSearch();

}
