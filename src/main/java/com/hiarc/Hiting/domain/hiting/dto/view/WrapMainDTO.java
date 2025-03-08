package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrapMainDTO {

    private List<MainRankingDTO> Div1List;
    private List<MainRankingDTO> Div2List;
    private List<MainRankingDTO> Div3List;

    private List<StreakResponseDTO> StreakList;
    private List<MainEventDTO> EventList;
}
