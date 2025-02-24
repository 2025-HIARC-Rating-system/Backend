package com.hiarc.Hiting.domain.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime seasonStart;
    @Setter
    private LocalDateTime seasonEnd;
    private LocalDateTime eventStart;
    @Setter
    private LocalDateTime eventEnd;

    @Builder
    public Date(LocalDateTime seasonStart, LocalDateTime seasonEnd, LocalDateTime eventStart, LocalDateTime eventEnd) {
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public Date updateSeasonStart(LocalDateTime seasonStart) {
        this.seasonStart = seasonStart;
        return this;
    }

    public Date updateSeasonEnd(LocalDateTime seasonEnd) {
        this.seasonEnd = seasonEnd;
        return this;
    }

    public Date updateEventStart(LocalDateTime eventStart) {
        this.eventStart = eventStart;
        return this;
    }

    public Date updateEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
        return this;
    }

}
