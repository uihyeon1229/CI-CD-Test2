package com.project.sangil_be.repository;

import com.project.sangil_be.model.GetTitle;
import com.project.sangil_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GetTitleRepository extends JpaRepository<GetTitle,Long> {
    List<GetTitle> findAllByUser(User user);

    Optional<GetTitle> findByUserAndUserTitle(User user, String s);

    int countAllByUser(User user);
}
