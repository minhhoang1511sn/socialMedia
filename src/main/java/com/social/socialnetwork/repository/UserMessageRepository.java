package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage,Long> {
    List<UserMessage> findAllByUserId(Long id);
}