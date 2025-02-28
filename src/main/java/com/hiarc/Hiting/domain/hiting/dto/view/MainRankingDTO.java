package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainRankingDTO {
    private String handle;
    private int totalHiting;
    private int rank;
    private int tier;

    public void updateRank(int rank){ this.rank = rank; }
}
