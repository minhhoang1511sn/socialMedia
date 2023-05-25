package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("select p from Post p where p.user = :user order by p.createDate desc ")
    List<Post> findPostOrderByCreateDate(User user);
    @Query("select p from Post p where p.createDate is not null  order by p.createDate desc ")
    List<Post> findAllPostOrderByCreateDate();
}