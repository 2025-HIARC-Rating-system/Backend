package com.hiarc.Hiting.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventDTO {

    private LocalDateTime start;
    private LocalDateTime end;
    private String eventCategory;
    private String detailCategory;

}
