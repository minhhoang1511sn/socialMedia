package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,Long> {
    @Query("select uc from UserPost uc where uc.userId = :id")
    List<UserPost> findAllByUserId(Long id);
}
