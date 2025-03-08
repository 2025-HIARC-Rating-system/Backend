package com.hiarc.Hiting.domain.admin.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RecentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recentEvengId")
    private Long id;

    private int eventHiting;
    private String eventCategory;
    private String detailCategory;

    private String name;
    private String handle;

    @CreatedDate
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @Builder
    public RecentEvent(int eventHiting, String eventCategory, String detailCategory, String name, String handle) {
        this.eventHiting = eventHiting;
        this.eventCategory = eventCategory;
        this.detailCategory = detailCategory;
        this.name = name;
        this.handle = handle;
    }

    public void updateStudent(Students student) {
        this.students = student;
    }


}
