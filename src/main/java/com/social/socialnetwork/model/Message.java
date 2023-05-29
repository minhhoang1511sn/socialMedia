package com.social.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User sender;
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User receiver;
    @OneToOne(fetch = FetchType.EAGER)
    private UserMessage uSender;
    @OneToOne(fetch = FetchType.EAGER)
    private UserMessage uReceiver;
    private String message;
    private LocalDateTime createTime;
}
