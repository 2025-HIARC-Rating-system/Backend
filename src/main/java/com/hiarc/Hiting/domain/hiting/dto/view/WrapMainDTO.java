package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrapMainDTO {

    private List<MainRankingDTO> div1List;
    private List<MainRankingDTO> div2List;
    private List<MainRankingDTO> div3List;

    private List<StreakResponseDTO> streakList;
    private List<MainEventDTO> eventList;
}
