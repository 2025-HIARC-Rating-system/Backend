package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.dto.view.StreakResponseDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.WrapStreakListDTO;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final StudentRepository studentsRepository;
    private final StreakRepository streakRepository;
    private final DateRepository dateRepository;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetDailyStreak() {
        List<Students> allStudents = studentsRepository.findAll();
        for (Students student : allStudents) {
            Streak streak = streakRepository.findByStudents(student);
            if (!streak.isDailyStreak()) {
                streak.updateStreakStart(null);
                streak.updateStreakEnd(null);
            }
            streak.updateDailyStreak(false);
        }
    }


    public boolean calculateDailyStreak(int level, int dailyHiting){

        int streakLimit;

        if (level >= 0 && level <= 5) {
            streakLimit = 1;
        } else if (level >= 6 && level <= 10) {
            streakLimit = 6;
        } else if (level >= 11 && level <= 16) {
            streakLimit = 12;
        } else if (level >= 17 && level <= 30) {
            streakLimit = 23;
        } else {
            throw new NotFoundException(ErrorStatus.TIER_LEVEL_INVALID);
        }

        return dailyHiting >= streakLimit;

    }

    public int calculateDays(LocalDate Start, LocalDate End){

        int betweenDays;

        if (Start == null || End == null) {
            betweenDays=0;
        } else betweenDays = (int) ChronoUnit.DAYS.between(Start, End);

        return betweenDays;
    }


}
