package com.task.weaver.domain.project;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "custom_url")
    private String customUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "is_public")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
