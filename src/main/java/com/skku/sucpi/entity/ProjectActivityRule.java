package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "project_activity_rule")
@Entity(name = "project_activity_rule")
@IdClass(ProjectActivityRule.ProjectActivityRuleId.class)
public class ProjectActivityRule {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(name = "custom_weight")
    private Double customWeight;

    // static inner composite key helper
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectActivityRuleId implements Serializable {
        private Long project;
        private Long activity;
    }
}
