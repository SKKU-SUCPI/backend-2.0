package com.skku.sucpi.dto.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ActivityListRequestDto {
    private List<ActivityRequestDto> activities;
}
