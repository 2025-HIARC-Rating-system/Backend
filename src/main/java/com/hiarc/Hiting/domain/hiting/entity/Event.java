package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.global.enums.EventCategory;
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

    @Enumerated(EnumType.STRING)
    private EventCategory eventCategory;

    private Integer tierCount;
    private Integer eventHiting;


    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @Builder
    public Event(int tierCount, EventCategory eventCategory) {
        this.tierCount = tierCount;
        this.eventCategory = eventCategory;
    }


    public void updateStudent(Students student) {
        this.students = student;
    }


}
