package com.hiarc.Hiting.domain.hiting.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrapStreakListDTO {
    private int seasonTotal;
    private List<StreakResponseDTO> streakList;
}