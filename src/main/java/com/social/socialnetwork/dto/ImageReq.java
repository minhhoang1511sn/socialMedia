package com.social.socialnetwork.dto;

import lombok.Data;

@Data
public class ImageReq {
    private Long id;
    private String imgLink;
    private Long postid;
    private Long userid;
}
