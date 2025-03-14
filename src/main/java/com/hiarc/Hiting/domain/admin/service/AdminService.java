package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.*;
import com.hiarc.Hiting.domain.admin.entity.*;
import com.hiarc.Hiting.domain.admin.entity.Date;
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
import java.util.*;


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
    private final DateService dateService;

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
        if (dateService.isEvent(date.getEventStart(), date.getEventEnd())){
            return;
        }

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
                    .seasonHiting(hiting.getSeasonHiting())
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

    @Transactional(readOnly = true)
    public WrapRecentSeasonDTO wrapRecentSeason(){

        List<RecentSeasonDTO> div1List = new ArrayList<>();
        List<RecentSeasonDTO> div2List = new ArrayList<>();
        List<RecentSeasonDTO> div3List = new ArrayList<>();

        Optional <Date> opDate = dateRepository.findTopByOrderByIdAsc();
        Date date = opDate.orElse(null);

        LocalDateTime seasonStart = date.getSeasonStart();
        LocalDateTime seasonEnd = date.getSeasonEnd();

        for (int i = 1; i <= 3; i++) {
            List<RecentSeason> recentSeasons = Optional.ofNullable(recentSeasonRepository.findByDivNum(i))
                    .orElse(Collections.emptyList());

            List<RecentSeasonDTO> recentList = recentSeasons.stream()
                    .map(recentSeason -> new RecentSeasonDTO(
                            recentSeason.getName(),
                            recentSeason.getHandle(),
                            recentSeason.getTier_level(),
                            recentSeason.getTotalHiting(),
                            recentSeason.getSeasonHiting(),
                            recentSeason.getStreakStart(),
                            recentSeason.getStreakEnd()
                    ))
                    .sorted(Comparator.comparing(RecentSeasonDTO::getSeasonHiting,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();

            if (i == 1) {
                div1List = recentList;
            } else if (i == 2) {
                div2List = recentList;
            } else if (i == 3) {
                div3List = recentList;
            }

        }

        return new WrapRecentSeasonDTO(seasonStart, seasonEnd, div1List, div2List, div3List);

    }


    @Transactional(readOnly = true)
    public WrapRecentEventDTO wrapRecentEvent(){

        Optional <Date> opDate = dateRepository.findTopByOrderByIdAsc();
        Date date = opDate.orElse(null);

        LocalDateTime eventStart = date.getEventStart();
        LocalDateTime eventEnd = date.getEventEnd();
        String eventCategory = String.valueOf(date.getEventCategory());
        String detailCategory = date.getDetailCategory();

        List<RecentEvent> recentEvents = Optional.of(recentEventRepository.findAll())
                .orElse(Collections.emptyList());
        List<RecentEventDTO> eventList = recentEvents.stream()
                .map(recentEvent -> new RecentEventDTO(
                        recentEvent.getName(),
                        recentEvent.getHandle(),
                        recentEvent.getEventHiting()
                ))
                .sorted(Comparator.comparing(RecentEventDTO::getEventHiting,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();


        return new WrapRecentEventDTO(eventCategory, detailCategory, eventStart, eventEnd, eventList);

    }


}
