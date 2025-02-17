package com.hiarc.Hiting.domain.event.entity;

import com.hiarc.Hiting.domain.admin.entity.Student;
import com.hiarc.Hiting.global.enums.TierCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId")
    private Long id;

    private int tierCount;
    private int eventHiting;

    @OneToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @Builder
    public Event(int tierCount, int eventHiting) {
        this.tierCount = tierCount;
        this.eventHiting = eventHiting;
    }


}
