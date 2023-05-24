package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> getAllVideoByUser(User user);
}
