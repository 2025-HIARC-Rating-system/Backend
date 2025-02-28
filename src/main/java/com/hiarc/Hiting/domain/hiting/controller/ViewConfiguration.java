package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.hiting.dto.view.HandleResponseDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.RankingDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.WrapStreakListDTO;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ViewConfiguration {

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<WrapStreakListDTO>> streakListView();

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<List<RankingDTO>>> divListView(@PathVariable("div") int div);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<HandleResponseDTO>> findByHandleView(@RequestParam String handle);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<?>> mainView();

}


