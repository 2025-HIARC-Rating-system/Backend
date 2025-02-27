package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
//import com.hiarc.Hiting.domain.hiting.service.HitingService;
import com.hiarc.Hiting.domain.hiting.service.SolvedAcService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "raking", description = "하이팅 계산 관련 API ") //API 큰묶음
@RequestMapping("/ranking")
public class HitingController {

    private final StudentRepository studentRepository;
    private final SolvedAcService solvedAcService;

    @PutMapping("/solved")
    @Operation(summary = "solvedAc 티어별로 해결한 문제수 불러오는 API", description = "list형식 반환")
    public ResponseEntity<List<?>> bringLevelSolvedList(@RequestParam String handle){
        try {
            solvedAcService.
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






}
