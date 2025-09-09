package com.skku.sucpi.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "submit")
@Entity(name = "submit")
public class Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submit_id")
    private Long id;

    @Column(name = "submit_date")
    @CreationTimestamp
    private LocalDateTime submitDate;

    @Column(name = "submit_state")
    private Integer state; // 0: 제출됨(미승인), 1: 승인, 2: 반려

    @Column(name = "submit_approved_date")
    @UpdateTimestamp
    private LocalDateTime approvedDate;

    @Column(name = "submit_title")
    private String title;

    @Lob
    @Column(name = "submit_content")
    private String content;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // activity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    // comment
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submit", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // file
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submit", cascade = CascadeType.ALL)
    List<FileStorage> files;

    public void updateState(Integer state) {
        this.state = state;
        this.approvedDate = LocalDateTime.now();
    }

    public void updateComment(String comment) {
//        this.comment = comment;
    }
}
