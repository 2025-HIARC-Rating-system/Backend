package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StreakService {

    private final StudentRepository studentsRepository;
    private final StreakRepository streakRepository;

    LocalDate defaultStart = DefaultDate.DEFAULT_START.getDateTime().toLocalDate();
    LocalDate defaultEnd = DefaultDate.DEFAULT_END.getDateTime().toLocalDate();

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetDailyStreak() {

        List<Students> allStudents = studentsRepository.findAll();
        for (Students student : allStudents) {
            Streak streak = streakRepository.findByStudents(student);
            if (!streak.isDailyStreak()) {
                streak.updateStreakStart(defaultStart);
                streak.updateStreakEnd(defaultEnd);
            }
            streak.updateDailyStreak(false);
        }
    }


    public boolean calculateDailyStreak(int level, int dailyHiting){

        int streakLimit;

        if (level >= 0 && level <= 10) {
            streakLimit = 1;
        } else if (level >= 11 && level <= 16) {
            streakLimit = 6;
        } else if (level >= 17 && level <= 30) {
            streakLimit = 12;
        } else {
            throw new NotFoundException(ErrorStatus.TIER_LEVEL_INVALID);
        }

        return dailyHiting >= streakLimit;

    }

    public int calculateDays(LocalDate Start, LocalDate End){

        int betweenDays;

        if (Start.equals(defaultStart) || End.equals(defaultEnd)) {
            betweenDays=0;
        } else betweenDays = (int) ChronoUnit.DAYS.between(Start, End) + 1;

        if (betweenDays < 0) { betweenDays = 0;}

        return betweenDays;
    }

    public int calculateDivStreakRatio(int div) {
        List<Students> students = studentsRepository.findByDivNum(div);
        if (students.isEmpty()) {
            throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        int studentSize = students.size();
        int streakNum = 0;

        for (Students student : students) {
            Streak streak = streakRepository.findByStudents(student);
            if (!streak.getStreakStart().equals(defaultStart)) {
                streakNum += 1;
            }
        }
        return (int) ((streakNum * 100.0) / studentSize);
    }

}
