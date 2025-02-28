package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainStreakDTO {
    private String handle;
    private int tier;
    private int divNum;
    private LocalDate startDate;
    private int totalStreak;
}
