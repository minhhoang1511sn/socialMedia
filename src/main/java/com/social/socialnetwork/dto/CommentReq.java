package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.*;
import lombok.Data;

@Data
public class CommentReq {
    private Long id;
    private String content;
    private Double rate;
    private Long postId;
    private Long userCommentId;
    private Image image;
}
