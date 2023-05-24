package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> getAllImageByUser(User user);
}
