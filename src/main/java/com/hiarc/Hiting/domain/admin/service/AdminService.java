package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.RecentSeason;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.RecentSeasonRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.EventRepository;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final SolvedAcService solvedAcService;
    private final RecentSeasonRepository recentSeasonRepository;
    private final HitingRepository hitingRepository;
    private final EventRepository eventRepository;
    private final StreakRepository streakRepository;

    @Transactional
    public void addStudents(List<StudentRequestDTO> requests) {

        for (StudentRequestDTO studentDTO : requests) {
            Optional<RecentSeason> isExistingStudent = recentSeasonRepository.findByHandle(studentDTO.getHandle());
            if (isExistingStudent.isPresent()) {
                RecentSeason existingStudent = isExistingStudent.get();
                Students newStudent = Students.builder()
                        .name(studentDTO.getName())
                        .tier_level(isExistingStudent.get().getTier_level())
                        .handle(isExistingStudent.get().getHandle())
                        .divNum(isExistingStudent.get().getDivNum())
                        .build();
                studentRepository.save(newStudent);
                Hiting hiting = newStudent.getHiting();
                Streak streak = newStudent.getStreak();
                hiting.updateTotalHiting(existingStudent.getTotalHiting());
                streak.updateStreakStart(existingStudent.getStreakStart());
                streak.updateStreakEnd(existingStudent.getStreakEnd());
            } else {
                Students newStudent = studentRepository.save(studentDTO.toEntity());
                changeStudentTierDiv(newStudent.getHandle());
            }

        }
    }

    private Integer calculateDiv(int tier) {
        if (tier >= 0 && tier <= 10) {
            return 3;
        } else if (tier >= 11 && tier <= 15) {
            return 2;
        } else if (tier >= 16 && tier <= 30) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public void changeStudentTierDiv(String studentHandle) {
        Students students = studentRepository.findByHandle(studentHandle)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));

        int tier = solvedAcService.getTierByHandle(studentHandle);

        students.updateTierLevel(tier);
        students.updateDivNum(calculateDiv(tier));

        studentRepository.save(students);
    }

    @Transactional
    public void resetRecentSeason() {

        recentSeasonRepository.deleteAll();
        List<Students> studentsList = studentRepository.findAll();
        for (Students student : studentsList) {
            Hiting hiting = hitingRepository.findByStudents(student);
            Event event = eventRepository.findByStudents(student);
            Streak streak = streakRepository.findByStudents(student);
            RecentSeason recent = RecentSeason.builder()
                    .name(student.getName())
                    .handle(student.getHandle())
                    .divNum(student.getDivNum())
                    .totalHiting(hiting.getTotalHiting())
                    .eventHiting(event.getEventHiting())
                    .streakStart(streak.getStreakStart())
                    .streakEnd(streak.getStreakEnd())
                    .build();

            recentSeasonRepository.save(recent);
        }
    }

    @Transactional
    public void seasonEndReset(){
        resetRecentSeason();
        hitingRepository.resetSeasonHitingForAll();

    }

    @Transactional
    public void newTerm(){
        resetRecentSeason();
        studentRepository.deleteAll();
    }






}
