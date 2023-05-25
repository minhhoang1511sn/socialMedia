package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private User sender;
    @OneToOne(fetch = FetchType.EAGER)
    private User receiver;
    private String message;
    private Date createTime;
}
