package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Entity(name = "submit")
public class Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submit_id")
    private Long id;

    // file table 추가 필요

    @Column(name = "submit_date")
    private LocalDateTime submitDate;

    @Column(name = "submit_state")
    private Integer state;

    @Column(name = "submit_approved_date")
    private LocalDateTime approvedDate;

    @Lob
    @Column(name = "submit_contents")
    private String content;
}
