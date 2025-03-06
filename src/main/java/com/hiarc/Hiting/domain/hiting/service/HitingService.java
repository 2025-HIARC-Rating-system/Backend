package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.RecentSeason;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.repository.RecentSeasonRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.dto.SolvedResponseDTO;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.SolvedRepository;
import com.hiarc.Hiting.domain.admin.service.SolvedAcService;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.hiarc.Hiting.global.enums.TierCategory.fromLeveltoTierRating;
import static java.time.LocalDateTime.*;

@Service
@RequiredArgsConstructor
public class HitingService {

    private final HitingRepository hitingRepository;
    private final SolvedRepository solvedRepository;
    private final StudentRepository studentsRepository;
    private final SolvedAcService solvedAcService;
    private final StreakService streakService;
    private final DateRepository dateRepository;
    private final RecentSeasonRepository recentSeasonRepository;
    private final StreakRepository streakRepository;

    @Scheduled(fixedRate = 900000)
    @Transactional
    public void realTimeHitings() {

        LocalDateTime todayDate = LocalDateTime.now();
        if (todayDate.getHour() < 6) { todayDate = todayDate.minusDays(1); }

        LocalDateTime defaultStart = DefaultDate.DEFAULT_START.getDateTime();
        LocalDateTime defaultEnd = DefaultDate.DEFAULT_START.getDateTime();


        Optional<Date> dateOptional = dateRepository.findTopByOrderByIdAsc();
        Date date = dateOptional.orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        boolean isSeason = (!Objects.equals(date.getSeasonStart(), defaultStart) && !Objects.equals(date.getSeasonEnd(), defaultEnd))
                && !todayDate.isBefore(date.getSeasonStart()) && !todayDate.isAfter(date.getSeasonEnd());
        boolean isEvent = (!Objects.equals(date.getSeasonStart(), defaultStart) && !Objects.equals(date.getSeasonEnd(), defaultEnd))
                && !todayDate.isBefore(date.getEventStart()) && !todayDate.isAfter(date.getEventEnd());


        List<Students> allStudents = studentsRepository.findAll();
        if (allStudents.isEmpty()) {
            throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        for (Students student : allStudents) {

            String handle = student.getHandle();
            List<SolvedResponseDTO> solvedList = solvedAcService.SolvedListByHandle(handle);
            Hiting hiting = hitingRepository.findByStudents(student);
            Streak streak = streakRepository.findByStudents(student);

            int delta = 0;

            for (SolvedResponseDTO dto : solvedList) {
                int level = dto.getLevel();
                int SolvedNow = dto.getSolved();
                int SolvedBefore;

                Optional<Solved> optional = solvedRepository.findByStudentsAndLevel(student, level);

                if (optional.isEmpty()) {
                    SolvedBefore = SolvedNow;
                    Solved solved = Solved.builder()
                            .level(level)
                            .eachSolved(SolvedNow)
                            .students(student)
                            .build();
                    solvedRepository.save(solved);
                } else {
                    Solved solved = optional.get();
                    SolvedBefore = solved.getEachSolved();
                    solved.updateEachSolved(SolvedNow);
                }

                int tierRating = fromLeveltoTierRating(level);
                int levelDelta = (SolvedNow - SolvedBefore) * tierRating;
                delta += levelDelta;

            }

            if (isEvent){
                delta = delta *2;
                hiting.addEventHiting(delta);
            } else {
                hiting.updateEventHiting(0);
            }

            hiting.addDailyHiting(delta);
            hiting.addTotalHiting(delta);

            if (isSeason){
                hiting.addSeasonHiting(delta);
            } else {
                Optional<RecentSeason> opt = recentSeasonRepository.findByHandle(handle);
                int recentTotalHiting = 0;
                if (opt.isPresent()) {
                    recentTotalHiting = opt.get().getTotalHiting();
                }
                int mainHiting = hiting.getTotalHiting() - recentTotalHiting;
                hiting.updateSeasonHiting(mainHiting);
            }


            if (!streak.isDailyStreak()){
                boolean dailyStreak = streakService.calculateDailyStreak(student.getTier_level(), hiting.getDailyHiting());

                if (dailyStreak){
                    streak.updateDailyStreak(true);
                    if (streak.getStreakEnd() == defaultEnd.toLocalDate()) {
                        streak.updateStreakStart(todayDate.toLocalDate());
                    }
                    streak.updateStreakEnd(todayDate.toLocalDate());
                }
            }


        }
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetDailyHiting(){
        List<Students> allStudents = studentsRepository.findAll();
        for (Students student : allStudents) {
            Hiting hiting = hitingRepository.findByStudents(student);
            hiting.updateDailyHiting(0);
        }
    }


}