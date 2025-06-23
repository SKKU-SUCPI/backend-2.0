package com.skku.sucpi.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // builder 전용 생성자
@Builder                                              // Submit.builder()… .build() 사용 가능
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
    private Integer state;

    @Column(name = "submit_approved_date")
    @UpdateTimestamp
    private LocalDateTime approvedDate;

    @Lob
    @Column(name = "submit_content")
    private String content;

    @Lob
    @Column(name = "submit_comment")
    private String comment;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // activity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    // file
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submit",cascade = CascadeType.ALL, orphanRemoval = true)
    List<FileStorage> files;

    public void updateState(Integer state) {
        this.state = state;
        this.approvedDate = LocalDateTime.now();
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}
