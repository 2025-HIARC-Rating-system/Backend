package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final SolvedTierService solvedAcService;
    private final DateRepository dateRepository;

    public Students addStudent(StudentRequestDTO request) {
        if (studentRepository.existsByHandle(request.getHandle())) {
            throw new GeneralException(ErrorStatus.MEMBER_EXIST);
        }
        return studentRepository.save(request.toEntity());
    }

    public List<Students> addStudents(List<StudentRequestDTO> requests) {

        List<String> names = requests.stream()
                .map(StudentRequestDTO::getHandle)
                .collect(Collectors.toList());
        List<Students> existingStudents = studentRepository.findAllByHandleIn(names);

        if (!existingStudents.isEmpty()) {
            List<String> duplicateHandles = existingStudents.stream()
                    .map(Students::getHandle)
                    .distinct()
                    .collect(Collectors.toList());

            throw new DuplicateStudentsException(ErrorStatus.MEMBER_EXIST, duplicateHandles);
        }

        List<Students> students = requests.stream()
                .map(StudentRequestDTO::toEntity)
                .collect(Collectors.toList());
        return studentRepository.saveAll(students);
    }

    private int calculateDiv(int tier) {
        if (tier >= 0 && tier <= 10) {
            return 3;
        } else if (tier >= 11 && tier <= 15) {
            return 2;
        } else if (tier >= 16 && tier <= 30) {
            return 1;
        }
        return 0;
    }

    public Students updateStudentTierDiv(String studentHandle) throws IOException {
        Students students = studentRepository.findByHandle(studentHandle)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));

        int tier = solvedAcService.getTierByHandle(studentHandle);

        students.setTier_level(tier);
        students.setDiv(calculateDiv(tier));

        return studentRepository.save(students);
    }

    public Date initialSeasonDate(DateDTO request) {
        validateDateRange(request);
        return dateRepository.save(request.toEntitySeason());
    }

    @Transactional
    public Date updateSeasonDate(DateDTO request) {
        validateDateRange(request);
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.setSeasonStart(request.getStart());
        date.setSeasonEnd(request.getEnd());

        return dateRepository.save(date);
    }

    @Transactional
    public Date updateEventDate(DateDTO request) {
        validateDateRange(request);
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.setEventStart(request.getStart());
        date.setEventEnd(request.getEnd());

        return dateRepository.save(date);
    }

    @Transactional
    public Date updateSeasonEndOnly(DateDTO request) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.setSeasonEnd(request.getEnd());

        return dateRepository.save(date);
    }

    @Transactional
    public Date updateEventEndOnly(DateDTO request) {
        Date date = dateRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException(ErrorStatus.DATE_NOT_FOUND));

        date.setEventEnd(request.getEnd());

        return dateRepository.save(date);
    }

    //날짜 순서 검증
    private void validateDateRange(DateDTO request) {

        if (request.getStart().isAfter(request.getEnd())) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }
















}
