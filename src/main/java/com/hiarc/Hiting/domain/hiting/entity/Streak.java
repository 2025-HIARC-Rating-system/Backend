package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streakId", updatable = false)
    private Long id;

    private boolean dailyStreak;

    private LocalDate streakStart;
    private LocalDate streakEnd;

    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @LastModifiedDate
    private LocalDateTime modified;

    @Builder
    public Streak(boolean dailyStreak, LocalDate streakStart, LocalDate streakEnd) {
        this.dailyStreak = dailyStreak;
        this.streakStart = streakStart;
        this.streakEnd = streakEnd;
    }

    public void updateStudentStreak(Students student) {
        this.students = student;
    }

    public void updateStreakStart(LocalDate streakStart) {
        this.streakStart = streakStart;
    }

    public void updateStreakEnd(LocalDate streakEnd) {
        this.streakEnd = streakEnd;
    }

    public void updateDailyStreak(boolean dailyStreak) {
        this.dailyStreak = dailyStreak;
    }



}
