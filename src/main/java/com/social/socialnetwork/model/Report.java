package com.social.socialnetwork.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contentReport;
    @OneToOne
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
}
