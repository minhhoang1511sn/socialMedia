package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByFirstUserAndSecondUser(User first,User second);
    @Query("select f from Friend f where f.firstUser = :user")
    List<Friend> findByFirstUser(User user);
    @Query("select f from Friend f where f.secondUser = :user")
    List<Friend> findBySecondUser(User user);
    @Query("select f from Friend f where f.firstUser = :first and f.secondUser = :second")
    Friend findByFirstUserAndSecondUser(User first,User second);

}
