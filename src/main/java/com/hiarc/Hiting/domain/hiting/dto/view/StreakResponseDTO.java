package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StreakResponseDTO {

    private String handle;
    private int tier;
    private int div;
    private int seasonStreak;
    private int totalStreak;
    private LocalDate startDate;

}
