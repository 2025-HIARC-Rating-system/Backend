package com.hiarc.Hiting.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class WrapRecentSeasonDTO {

    private LocalDateTime seasonStart;
    private LocalDateTime seasonEnd;
    private List<RecentSeasonDTO> div1List;
    private List<RecentSeasonDTO> div2List;
    private List<RecentSeasonDTO> div3List;

}
