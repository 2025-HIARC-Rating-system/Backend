package com.hiarc.Hiting.domain.admin.dto;

import com.hiarc.Hiting.domain.admin.entity.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DateDTO {
    private LocalDateTime start;
    private LocalDateTime end;

    public Date toEntitySeason() {
        return Date.builder()
                .seasonStart(start)
                .seasonEnd(end)
                .build();
    }

}
