package com.hiarc.Hiting.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RecentSeasonDTO {

    private String name;
    private String handle;
    private Integer tier_level;
    private Integer totalHiting;
    private Integer seasonHiting;
    private LocalDate streakStart;
    private LocalDate streakEnd;

}
