package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HandleResponseDTO {
    private String handle;
    private int tier;
    private int divNum;
    private int rank;
    private LocalDate startDate;     // streakStart
    private int totalStreak;
    private int seasonStreak;
    private int seasonTotal;
    private int totalHiting;
    private int seasonHiting;
    private int dailyHiting;
}
