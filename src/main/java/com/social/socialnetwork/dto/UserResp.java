package com.social.socialnetwork.dto;

import lombok.Data;

@Data

public class UserResp {
    private Long id;
    private String email;
    private String lastName;
    private String gender;
    private String address;
    // admin
    private String role;
    private boolean enable;

}
