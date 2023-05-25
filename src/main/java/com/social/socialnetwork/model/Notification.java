package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private String content;
    @Enumerated(EnumType.STRING)
    private TypeNotifications typeNotifications;
    private Date createTime;
    private Boolean isRead;

}
