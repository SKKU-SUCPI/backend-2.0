package com.skku.sucpi.entity;

import com.skku.sucpi.dto.activity.ActivityRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "activity")
@Entity(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "activity_class")
    private String activityClass;

    @Column(name = "activity_detail")
    private String detail;

    @Column(name = "activity_weight")
    private Double weight;

    @Column(name = "activity_domain")
    private Long domain;

    public void updateFromDto(ActivityRequestDto dto, Category category) {

        if (dto.getActivityClass() != null) {
            this.activityClass = dto.getActivityClass();
        }
        if (dto.getActivityDetail() != null) {
            this.detail = dto.getActivityDetail();
        }
        if (dto.getActivityWeight() != null) {
            this.weight = dto.getActivityWeight();
        }
        if (category != null) {
            this.category = category;
        }
    }
}
