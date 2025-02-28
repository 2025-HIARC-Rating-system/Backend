package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Streak(boolean dailyStreak, LocalDate streakStart, LocalDate streakEnd) {
        this.dailyStreak = dailyStreak;
        this.streakStart = streakStart;
        this.streakEnd = streakEnd;
    }

    public void updateStudent(Students student) {
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
