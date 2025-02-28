package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.admin.controller.AdminConfiguration;
import com.hiarc.Hiting.domain.hiting.service.HitingService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Hiting", description = "계산 관련 API")
@RequestMapping
public class HitingController implements HitingConfiguration{

    private final HitingService hitingService;

    @GetMapping("/ranking/test")
    @Operation(summary = "모든 학생 hiting 및 스트릭 계산 API", description = "(테스트용 API) 기존에는 15분에 1회 자동 호출됨.")
    public ResponseEntity<ApiResponse<Void>> calculateHitingAndStreak() {
        try {
            hitingService.realTimeHitings();
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess());
        } catch (GeneralException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.of(ErrorStatus.OPEN_API_FAIL, null));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        }
    }

}
