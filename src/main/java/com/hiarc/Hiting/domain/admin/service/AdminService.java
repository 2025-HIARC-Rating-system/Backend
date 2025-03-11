package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.*;
import com.hiarc.Hiting.domain.admin.repository.*;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final SolvedAcService solvedAcService;
    private final RecentSeasonRepository recentSeasonRepository;
    private final RecentEventRepository recentEventRepository;
    private final HitingRepository hitingRepository;
    private final EventRepository eventRepository;
    private final StreakRepository streakRepository;
    private final DateRepository dateRepository;

    LocalDateTime defaultStart = DefaultDate.DEFAULT_START.getDateTime();
    LocalDateTime defaultEnd = DefaultDate.DEFAULT_END.getDateTime();

    @Transactional
    public void addStudents(List<StudentRequestDTO> requests) {

        for (StudentRequestDTO studentDTO : requests) {
            Optional<RecentSeason> isExistingStudent = recentSeasonRepository.findByHandle(studentDTO.getHandle());
            if (isExistingStudent.isPresent()) {
                RecentSeason existingStudent = isExistingStudent.get();
                Students newStudent = Students.builder()
                        .name(studentDTO.getName())
                        .tier_level(existingStudent.getTier_level())
                        .handle(existingStudent.getHandle())
                        .divNum(existingStudent.getDivNum())
                        .build();
                studentRepository.save(newStudent);
                changeStudentTierDiv(newStudent.getHandle());
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

    @Scheduled(cron = "0 15 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void changeAllStudentsTierDiv() {

        List<Students> allStudents = studentRepository.findAll();
        for (Students student : allStudents) {
            String handle = student.getHandle();
            int tier = solvedAcService.getTierByHandle(handle);
            student.updateTierLevel(tier);
            student.updateDivNum(calculateDiv(tier));
        }
    }

    @Scheduled(cron = "0 30 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void changeAllStudentsTagEvent() {

        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        String detailCategory = date.getDetailCategory();
        List<Students> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        for (Students student : allStudents) {
            String handle = student.getHandle();
            Event event = eventRepository.findByStudents(student);
            int countBefore = event.getTagCount();
            int countToday = solvedAcService.getTagSolvedByHandle(handle, detailCategory); //점수
            int eventHiting = countToday - countBefore; // 점수 임의로 설정
            Hiting hiting = hitingRepository.findByStudents(student);
            hiting.updateEventHiting(eventHiting);
            event.updateTagCount(countToday);
        }
    }

    @Transactional
    public void initialAllStudentsTagCount() {

        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        String detailCategory = date.getDetailCategory();
        List<Students> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        for (Students student : allStudents) {
            String handle = student.getHandle();
            Event event = eventRepository.findByStudents(student);
            int countToday = solvedAcService.getTagSolvedByHandle(handle, detailCategory);
            event.updateTagCount(countToday);
        }
    }


    @Transactional
    public void resetRecentSeason() {

        recentSeasonRepository.deleteAll();
        List<Students> studentsList = studentRepository.findAll();
        for (Students student : studentsList) {
            Hiting hiting = hitingRepository.findByStudents(student);
            Streak streak = streakRepository.findByStudents(student);
            RecentSeason recent = RecentSeason.builder()
                    .name(student.getName())
                    .handle(student.getHandle())
                    .divNum(student.getDivNum())
                    .totalHiting(hiting.getTotalHiting())
                    .streakStart(streak.getStreakStart())
                    .streakEnd(streak.getStreakEnd())
                    .build();

            recentSeasonRepository.save(recent);
        }
    }

    @Transactional
    public void resetRecentEvent() {

        recentEventRepository.deleteAll();
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        List<Students> studentsList = studentRepository.findAll();
        for (Students student : studentsList) {
            Hiting hiting = hitingRepository.findByStudents(student);
            RecentEvent recent = RecentEvent.builder()
                    .eventHiting(hiting.getEventHiting())
                    .eventCategory(String.valueOf(date.getEventCategory()))
                    .detailCategory(date.getDetailCategory())
                    .name(student.getName())
                    .handle(student.getHandle())
                    .build();
            recentEventRepository.save(recent);
        }
    }

    @Transactional
    public void seasonEndReset(){
        resetRecentSeason();
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        date.updateSeasonStart(defaultStart);
        date.updateSeasonEnd(defaultEnd);
        hitingRepository.resetSeasonHitingForAll();

    }

    @Transactional
    public void eventEndReset(){
        resetRecentEvent();
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        date.updateSeasonStart(defaultStart);
        date.updateSeasonEnd(defaultEnd);
        hitingRepository.resetSeasonHitingForAll();

    }



    @Transactional
    public void newTerm(){
        resetRecentSeason();
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));
        date.updateSeasonStart(defaultStart);
        date.updateSeasonEnd(defaultEnd);
        date.updateEventStart(defaultStart);
        date.updateEventEnd(defaultEnd);
        studentRepository.deleteAll();
    }

    @Transactional
    public void addOneStudent(StudentRequestDTO request) {
        Optional<RecentSeason> isExistingStudent = recentSeasonRepository.findByHandle(request.getHandle());
        if (isExistingStudent.isPresent()) {
            RecentSeason existingStudent = isExistingStudent.get();
            Students newStudent = Students.builder()
                    .name(request.getName())
                    .tier_level(existingStudent.getTier_level())
                    .handle(existingStudent.getHandle())
                    .divNum(existingStudent.getDivNum())
                    .build();
            studentRepository.save(newStudent);
            changeStudentTierDiv(newStudent.getHandle());
            Hiting hiting = newStudent.getHiting();
            Streak streak = newStudent.getStreak();
            hiting.updateTotalHiting(existingStudent.getTotalHiting());
            streak.updateStreakStart(existingStudent.getStreakStart());
            streak.updateStreakEnd(existingStudent.getStreakEnd());
        } else {
            Students newStudent = studentRepository.save(request.toEntity());
            changeStudentTierDiv(newStudent.getHandle());
        }

    }




}
