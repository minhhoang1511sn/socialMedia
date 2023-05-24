package com.social.socialnetwork.dto;

import com.social.socialnetwork.model.Comment;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.PostType;
import com.social.socialnetwork.model.Video;
import lombok.Data;

import java.util.List;

@Data
public class PostResp {
    private Long id;
    private Long user;
    private Long countLike;
    private List<Comment> commentList;
    private Image imageList;
    private Video videoList;
    private String content;
    private PostType postType;
}
