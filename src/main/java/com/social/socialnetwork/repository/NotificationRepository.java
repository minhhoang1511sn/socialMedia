package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Notification;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> getNotificationsByUser(User user);
}