package com.hiarc.Hiting.domain.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime seasonStart;
    private LocalDateTime seasonEnd;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    @Builder
    public Date(LocalDateTime seasonStart, LocalDateTime seasonEnd, LocalDateTime eventStart, LocalDateTime eventEnd) {
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public void updateSeasonStart(LocalDateTime seasonStart) {
        this.seasonStart = seasonStart;
    }

    public void updateSeasonEnd(LocalDateTime seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public void updateEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

}
