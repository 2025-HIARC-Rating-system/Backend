package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.global.enums.EventCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private String detailCategory;
    private int tagCount;


    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @Builder
    public Event(EventCategory eventCategory, String detailCategory, int tagCount) {
        this.eventCategory = eventCategory;

        this.detailCategory = (detailCategory != null) ? detailCategory : "false";
        this.tagCount = (detailCategory != null) ? tagCount : 0;

    }


    public void updateStudentEvent(Students student) {
        this.students = student;
    }


}
