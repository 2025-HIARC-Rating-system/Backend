package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface HitingConfiguration {

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> calculateHitingAndStreak();
}
