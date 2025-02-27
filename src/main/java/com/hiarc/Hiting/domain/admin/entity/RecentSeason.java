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
public class RecentSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recentSeasonId")
    private Long id;

    private String name;
    private String handle;
    private Integer tier_level;
    private Integer divNum;
    private Integer totalHiting;
    private Integer eventHiting;
    private LocalDate streakStart;
    private LocalDate streakEnd;

    @CreatedDate
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @Builder
    public RecentSeason(String name, String handle, Integer tier_level, Integer divNum, Integer totalHiting, Integer eventHiting, LocalDate streakStart, LocalDate streakEnd) {

        this.name = name;
        this.handle = handle;
        this.tier_level = tier_level;
        this.divNum = divNum;
        this.totalHiting = totalHiting;
        this.eventHiting = eventHiting;
        this.streakStart = streakStart;
        this.streakEnd = streakEnd;

    }

    public void updateStudent(Students student) {
        this.students = student;
    }


}
