package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<Message,Long> {
    @Query(value="SELECT m FROM Message m WHERE (m.sender.id = :userId AND m.receiver.id = :companionId) OR " +
            "(m.sender.id = :companionId AND m.receiver.id = :userId) ORDER BY m.createTime")
    List<Message> findConversation(Long userId, Long companionId);
    Message findFirstBySenderIdOrReceiverIdOrderByIdDesc(Long id, Long id1);
    @Query(value="SELECT m FROM Message m WHERE m.id IN (SELECT MAX(id) FROM Message GROUP BY sender, receiver) " +
            " AND (m.sender.id = :id OR m.receiver.id = :id)")
    Iterable<Message> findAllRecentMessages(Long id);
}
