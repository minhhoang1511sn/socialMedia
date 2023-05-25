package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostReq {
    private Long id;
    private Long user;
    private Long userPost;
    private Long countLike;
    private List<Comment> commentList;
    private Image imageList;
    private String content;
    private PostType postType;
    private Date createDate;
}
