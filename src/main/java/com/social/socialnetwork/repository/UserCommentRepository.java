package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment,Long> {
    @Query("select uc from UserComment uc where uc.userId = :id")
    List<UserComment> findAllByUserId(Long id);
}
