package com.hiarc.Hiting.domain.admin.dto;

import com.hiarc.Hiting.domain.admin.entity.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventResponseDTO {

    private LocalDateTime start;
    private LocalDateTime end;
    private String eventCategory;
    private String detailCategory;

}
