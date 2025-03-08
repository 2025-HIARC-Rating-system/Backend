package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.global.enums.EventCategory;
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

    @Enumerated(value = EnumType.STRING)
    private EventCategory eventCategory;

    private String detailCategory;

    @Builder
    public Date(LocalDateTime seasonStart, LocalDateTime seasonEnd, LocalDateTime eventStart, LocalDateTime eventEnd, EventCategory eventCategory, String detailCategory) {
        this.seasonStart = (seasonStart != null) ? seasonStart : LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        this.seasonEnd = (seasonEnd != null) ? seasonEnd : LocalDateTime.of(1970, 1, 2, 0, 0, 0);
        this.eventStart = (eventStart != null) ? eventStart : LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        this.eventEnd = (eventEnd != null) ? eventEnd : LocalDateTime.of(1970, 1, 2, 0, 0, 0);
        this.eventCategory = eventCategory;
        this.detailCategory = (detailCategory != null) ? detailCategory : "false";
    }

    public void updateSeasonStart(LocalDateTime seasonStart) {
        this.seasonStart = seasonStart;
    }

    public void updateSeasonEnd(LocalDateTime seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public void updateEventStart(LocalDateTime eventStart) {
        this.eventStart = eventStart;
    }

    public void updateEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

    public void updateEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void updateDetailCategory(String detailCategory) {
        this.detailCategory = detailCategory;
    }

}
