package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Entity(name = "submit")
public class Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submit_id")
    private Long id;

    @Column(name = "submit_date")
    private LocalDateTime submitDate;

    @Column(name = "submit_state")
    private Integer state;

    @Column(name = "submit_approved_date")
    private LocalDateTime approvedDate;

    @Lob
    @Column(name = "submit_contents")
    private String content;

    // user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // activity
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    // file
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submit",cascade = CascadeType.ALL, orphanRemoval = true)
    List<FileStorage> files;
}
