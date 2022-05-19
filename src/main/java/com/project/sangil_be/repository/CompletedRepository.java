package com.project.sangil_be.repository;

import com.project.sangil_be.model.Completed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedRepository extends JpaRepository<Completed,Long> {

    Completed findByCompleteId(Long completedId);

    void deleteByCompleteId(Long completedId);

    List<Completed> findAllByUserId(Long userId);

    List<Completed> findAllByMountainIdAndUserId(Long mountainId, Long userId);

    Long countAllByUserId(Long userId);
}
