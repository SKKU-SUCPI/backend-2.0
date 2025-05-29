package com.skku.sucpi.dto.activity;

import com.skku.sucpi.entity.Activity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;


public class ActivityDto {

    @Getter
    @Jacksonized
    @Builder
    @Schema(name = "ActivityDto_Response")
    static public class Response {
        private Long activityId;
        private String activityClass;
        private String activityName;
        private String activityDetail;
        private Double activityWeight;
        private Long activityDomain;

        private Long categoryId;
        private String categoryName;
        private Double categoryRatio;

        public static Response fromEntity(Activity activity) {
            return Response.builder()
                    .activityId(activity.getId())
                    .activityClass(activity.getActivityClass())
                    .activityName(activity.getName())
                    .activityDetail(activity.getDetail())
                    .activityWeight(activity.getWeight())
                    .activityDomain(activity.getDomain())
                    .categoryId(activity.getCategory().getId())
                    .categoryName(activity.getCategory().getName())
                    .categoryRatio(activity.getCategory().getRatio())
                    .build();
        }
    }


}
