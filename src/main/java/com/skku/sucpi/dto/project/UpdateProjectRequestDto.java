package com.skku.sucpi.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequestDto {
    private String projectName;
    private Double multiplier;
    private LocalDate startDate;
    private LocalDate endDate;

    @PutMapping("/projects/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @RequestBody UpdateProjectRequestDto dto) {
        // update logic
        return ResponseEntity.ok().build();
    }
}
