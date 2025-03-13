package com.hiarc.Hiting.domain.admin.controller;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.EventResponseDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.DateRepository;
import com.hiarc.Hiting.domain.admin.service.AdminService;
import com.hiarc.Hiting.domain.admin.service.DateService;
import com.hiarc.Hiting.domain.hiting.service.HitingService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.DuplicateStudentsException;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import com.hiarc.Hiting.global.enums.DefaultDate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin", description = "관리자 페이지 관련 API ")
public class AdminController implements AdminConfiguration{

    private final AdminService adminService;
    private final DateService dateService;
    private final HitingService hitingService;
    private final DateRepository dateRepository;


    @PostMapping("/reset/term")
    @Operation(summary = "새로운 학기 시작 및 학생 리스트 추가 API", description = "새로운 학기가 시작될때 학회원 정보 등록. DB 삭제 후 다시 생성. solvedAC에서 티어 받아오고 div계산까지 진행됨.")
    public ResponseEntity<ApiResponse<Void>> resetTermAndAddStudents(@RequestBody List<StudentRequestDTO> requests) {

        adminService.newTerm();

        try {
            adminService.addStudents(requests);
            hitingService.realTimeHitings();
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }

    }

    @PostMapping("/reset/season")
    @Operation(summary = "시즌 DB 초기화 API", description = "시즌이 끝났을때 실행. 어드민 페이지 중 지난시즌 목록 수정, seasonHiting값 0으로 초기화")
    public ResponseEntity<ApiResponse<Void>> resetSeason() {
        adminService.seasonEndReset();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
    }

    @PostMapping("/reset/event")
    @Operation(summary = "이벤트 관련 DB 초기화 API", description = "이벤트가 끝났을때 실행. 어드민 페이지 중 지난이벤트 목록 수정, eventHiting값 0으로 초기화")
    public ResponseEntity<ApiResponse<Void>> resetEvent() {
        adminService.eventEndReset();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
    }

    @PostMapping("/season/new")
    @Operation(summary = "새로운 시즌 기간 등록 API", description = "시즌 기간 등록")
    public ResponseEntity<ApiResponse<Void>> changeSeasonDate(@RequestBody DateDTO request) {
        try {
            dateService.changeSeasonDate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(ErrorStatus.INVALID_DATE_FORMAT, null));
        }

    }

    @PostMapping("/event/new")
    @Operation(summary = "새로운 이벤트 기간 등록 API", description = "이벤트 기간 및 유형, 상세정보 등록, eventHiting 초기화")
    public ResponseEntity<ApiResponse<Void>> changeEventDate(@RequestBody EventResponseDTO request) {
        try {
            dateService.changeEventAndDate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }

    }

    @PostMapping("/season/end")
    @Operation(summary = "시즌 중도 중단 API", description = "시즌 끝나는 날짜를 변경함으로 중단한다. 현재 날짜보다 미래 시간을 입력할 것.")
    public ResponseEntity<ApiResponse<Void>> changeSeasonEndOnly(@RequestBody LocalDateTime end) {
        try {
            dateService.changeSeasonEndOnly(end);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        }

    }

    @PostMapping("/event/end")
    @Operation(summary = "이벤트 중도 중단 API", description = "이벤트 끝나는 날짜를 변경함으로 중단한다. 현재 날짜보다 미래 시간을 입력할 것.")
    public ResponseEntity<ApiResponse<Void>> changeEventEndOnly(@RequestBody LocalDateTime end) {
        try {
            dateService.changeEventEndOnly(end);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        }

    }

    @PostMapping("/first-season")
    @Operation(summary = "최초 시즌 기간 등록 API", description = "DB 삭제시 최초 1회 필요함")
    public ResponseEntity<ApiResponse<Void>> changeFirstSeasonDate() {

        LocalDateTime defaultStart = DefaultDate.DEFAULT_START.getDateTime();
        LocalDateTime defaultEnd = DefaultDate.DEFAULT_START.getDateTime();

        Date date = Date.builder()
                .seasonStart(defaultStart)
                .seasonEnd(defaultEnd)
                .eventStart(defaultStart)
                .eventEnd(defaultEnd)
                .build();
        dateRepository.save(date);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
    }

    @PostMapping("/student/test")
    @Operation(summary = "학회원 1명 등록 API", description = "(테스트용 API) 학회원 정보 등록 + solvedAc에서 티어 가져옴 + div 부여")
    public ResponseEntity<ApiResponse<Void>> addOneStudent(@RequestBody StudentRequestDTO request) {
        try {
            adminService.addOneStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());

        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }
    }


}
