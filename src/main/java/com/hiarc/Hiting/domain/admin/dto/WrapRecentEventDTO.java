package com.hiarc.Hiting.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class WrapRecentEventDTO {

    private String eventCategory;
    private String detailCategory;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private List<RecentEventDTO> eventList;

}
