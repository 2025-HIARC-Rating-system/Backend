package com.hiarc.Hiting.domain.admin.entity;

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

    private int tagCount;

    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;


    @Builder
    public Event(int tagCount) {
        this.tagCount = tagCount;
    }

    public void updateStudentEvent(Students student) {
        this.students = student;
    }

    public void updateTagCount(int tagCount) { this.tagCount = tagCount; }


}
