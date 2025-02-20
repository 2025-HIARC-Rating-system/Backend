package com.hiarc.Hiting.domain.hiting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private String handle;
    private int tier;
    private boolean isEvent;
    private int dailyHiting;
    private int totalHiting;
}
