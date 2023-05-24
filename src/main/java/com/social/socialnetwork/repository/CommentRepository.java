package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentByPost(Post post);
}
