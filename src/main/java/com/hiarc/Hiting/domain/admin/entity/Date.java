package com.hiarc.Hiting.domain.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public Date(String seasonStart, String seasonEnd, String eventStart, String eventEnd) {
        this.seasonStart = LocalDateTime.parse(seasonStart);
        this.seasonEnd = LocalDateTime.parse(seasonEnd);
        this.eventStart = LocalDateTime.parse(eventStart);
        this.eventEnd = LocalDateTime.parse(eventEnd);
    }
}
