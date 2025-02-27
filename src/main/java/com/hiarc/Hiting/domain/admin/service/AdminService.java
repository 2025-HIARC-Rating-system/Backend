package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.RecentSeason;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.RecentSeasonRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import com.hiarc.Hiting.domain.hiting.repository.HitingRepository;
import com.hiarc.Hiting.domain.hiting.repository.StreakRepository;
import com.hiarc.Hiting.domain.hiting.service.SolvedAcService;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final SolvedTierService solvedAcService;
    private final RecentSeasonRepository recentSeasonRepository;
    private final HitingRepository hitingRepository;
    private final SolvedAcService.EventRepository eventRepository;
    private final StreakRepository streakRepository;


    @Transactional
    public List<Students> addStudents(List<StudentRequestDTO> requests) { //기존 학생이랑 조회, 등록 ->

        for (StudentRequestDTO studentDTO : requests) {
            Optional<RecentSeason> isExistingStudent = recentSeasonRepository.findByHandle(studentDTO.getHandle());
            if (isExistingStudent.isPresent()) {

            } else {

            }
        }

//        List<String> names = requests.stream()
//                .map(StudentRequestDTO::getHandle)
//                .collect(Collectors.toList());
//        List<Students> existingStudents = studentRepository.findAllByHandleIn(names);
//
//        if (!existingStudents.isEmpty()) {
//            List<String> duplicateHandles = existingStudents.stream()
//                    .map(Students::getHandle)
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            throw new DuplicateStudentsException(ErrorStatus.MEMBER_EXIST, duplicateHandles);
//        }
//
//        List<Students> students = requests.stream()
//                .map(StudentRequestDTO::toEntity)
//                .collect(Collectors.toList());
//        return studentRepository.saveAll(students);
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
    public void changeStudentTierDiv(String studentHandle) throws IOException {
        Students students = studentRepository.findByHandle(studentHandle)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));

        int tier = solvedAcService.getTierByHandle(studentHandle);

        students.updateTierLevel(tier);
        students.updateDivNum(calculateDiv(tier));

        studentRepository.save(students);
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


    @Transactional
    public void resetRecentSeason(){ //recent의 값 다 삭제, 값 다 복사함

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


















}
