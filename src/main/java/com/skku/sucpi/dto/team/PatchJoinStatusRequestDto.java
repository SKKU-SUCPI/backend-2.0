package com.skku.sucpi.dto.team;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchJoinStatusRequestDto {
    @NotNull
    private Long teamId;

    @NotBlank
    private Integer joinStatus;
}
