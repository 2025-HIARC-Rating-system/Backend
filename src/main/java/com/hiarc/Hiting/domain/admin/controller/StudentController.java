package com.hiarc.Hiting.domain.admin.controller;

import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.Student;
import com.hiarc.Hiting.domain.admin.service.StudentService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.Internal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 페이지 관련 API ") //API 큰묶음
@RequestMapping("/admin")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/student")
    @Operation(summary = "학회원 1명 등록 API", description = "학회원 정보 등록 + solvedAc에서 티어 가져옴 + div 부여")
    public ResponseEntity<ApiResponse<Void>> addStudent(@RequestBody StudentRequestDTO request) {
        try {
            Student saved = studentService.addStudent(request);
            studentService.updateStudentTierDiv(saved.getHandle());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());

        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.MEMBER_EXIST, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        }
    }

    @PostMapping("/student/batch")
    @Operation(summary = "학회원 여러명 등록 API", description = "학회원 정보 등록 + solvedAc에서 티어 가져옴 + div 부여")
    public ResponseEntity<ApiResponse<?>> addStudents(@RequestBody List<StudentRequestDTO> requests) {
        List<Student> savedList = null;

        try {
            savedList = studentService.addStudents(requests);
            for (Student s : savedList) { studentService.updateStudentTierDiv(s.getHandle()); }
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());

        } catch (DuplicateStudentsException e) {
            List<String> duplicateHandles = e.getDuplicateHandles();
            // "이미 존재하는 학회원입니다." + 중복 이름들
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.MEMBER_EXIST, duplicateHandles));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        }

    }

    @PutMapping("/tier")
    @Operation(summary = "solvedAc 티어 불러오는 API", description = "div 할당까지")
    public ResponseEntity<?> updateTierDiv(@RequestParam String handle) {
        try {
            studentService.updateStudentTierDiv(handle);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        }
    }











}
