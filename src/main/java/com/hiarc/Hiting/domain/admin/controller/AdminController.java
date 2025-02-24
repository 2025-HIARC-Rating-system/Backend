package com.hiarc.Hiting.domain.admin.controller;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.service.AdminService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminConfiguration{

    private final AdminService adminService;

    @Override
    @PostMapping("/student")
    @Operation(summary = "학회원 1명 등록 API", description = "학회원 정보 등록 + solvedAc에서 티어 가져옴 + div 부여")
    public ResponseEntity<ApiResponse<Void>> addStudent(@RequestBody StudentRequestDTO request) {
        try {
            Students saved = adminService.addStudent(request);
            adminService.changeStudentTierDiv(saved.getHandle());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());

        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.MEMBER_EXIST, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }
    }

    @PostMapping("/student/batch")
    @Operation(summary = "학회원 여러명 등록 API", description = "학회원 정보 등록 + solvedAc에서 티어 가져옴 + div 부여")
    public ResponseEntity<ApiResponse<?>> addStudents(@RequestBody List<StudentRequestDTO> requests) {
        List<Students> savedList;

        try {
            savedList = adminService.addStudents(requests);
            for (Students s : savedList) { adminService.changeStudentTierDiv(s.getHandle()); }
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());

        } catch (DuplicateStudentsException e) {
            List<String> duplicateHandles = e.getDuplicateHandles();
            // "이미 존재하는 학회원입니다." + 중복 이름들
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.MEMBER_EXIST, duplicateHandles));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }

    }

    @PutMapping("/tier")
    @Operation(summary = "solvedAc 티어 불러오는 API", description = "div 할당까지")
    public ResponseEntity<?> changeTierDivNum(@RequestParam String handle) {
        try {
            adminService.changeStudentTierDiv(handle);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        }
    }

    @PostMapping("/new-season")
    @Operation(summary = "새로운 시즌 기간 등록 API", description = "시즌 기간 등록")
    public ResponseEntity<ApiResponse<Void>> changeSeasonDate(@RequestBody DateDTO request) {
        try {
            adminService.changeSeasonDate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.INVALID_DATE_FORMAT, null));
        }

    }

    @PostMapping("/first-season")
    @Operation(summary = "최초 시즌 기간 등록 API", description = "DB 삭제시 최초 1회만")
    public ResponseEntity<ApiResponse<Void>> changeFirstSeasonDate(@RequestBody DateDTO request) {
        try {
            adminService.initialSeasonDate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.INVALID_DATE_FORMAT, null));
        }
    }


    @PostMapping("/new-event")
    @Operation(summary = "새로운 이벤트 기간 등록 API", description = "이벤트 기간 등록")
    public ResponseEntity<ApiResponse<Void>> changeEventDate(@RequestBody DateDTO request) {
        try {
            adminService.changeEventDate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.INVALID_DATE_FORMAT, null));
        }

    }

    @PostMapping("/new-season/end")
    @Operation(summary = "시즌 중도 중단 API", description = "시즌 끝나는 날짜를 변경하여 중단한다")
    public ResponseEntity<ApiResponse<Void>> changeSeasonEndOnly(@RequestBody DateDTO request) {
        try {
            adminService.changeSeasonEndOnly(request);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        }

    }

    @PostMapping("/new-event/end")
    @Operation(summary = "이벤트 중도 중단 API", description = "이벤트 끝나는 날짜를 변경하여 중단한다")
    public ResponseEntity<ApiResponse<Void>> changeEventEndOnly(@RequestBody DateDTO request) {
        try {
            adminService.changeEventEndOnly(request);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        }

    }

}
