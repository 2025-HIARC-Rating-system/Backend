package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.SolvedResponseTierDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.Student;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SolvedAcService solvedAcService;

    public Student addStudent(StudentRequestDTO request) {
        if (studentRepository.existsByHandle(request.getHandle())) {
            throw new GeneralException(ErrorStatus.MEMBER_EXIST);
        }
        return studentRepository.save(request.toEntity());
    }

    public List<Student> addStudents(List<StudentRequestDTO> requests) {

        List<String> names = requests.stream()
                .map(StudentRequestDTO::getHandle)
                .collect(Collectors.toList());
        List<Student> existingStudents = studentRepository.findAllByHandleIn(names);

        if (!existingStudents.isEmpty()) {
            List<String> duplicateHandles = existingStudents.stream()
                    .map(Student::getHandle)
                    .distinct()
                    .collect(Collectors.toList());

            throw new DuplicateStudentsException(ErrorStatus.MEMBER_EXIST, duplicateHandles);
        }

        List<Student> students = requests.stream()
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

    public Student updateStudentTierDiv(String studentHandle) throws IOException {
        Student student = studentRepository.findByHandle(studentHandle)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        int tier = solvedAcService.getTierByHandle(studentHandle);

        student.setTier_level(tier);
        student.setDiv(calculateDiv(tier));

        return studentRepository.save(student);
    }













}
