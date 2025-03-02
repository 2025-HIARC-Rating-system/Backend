package com.hiarc.Hiting.domain.admin.controller;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;


//코드 전반적으로 수정 필요 (예외처리 추가할 것)

 // Swagger API 큰묶음
public interface AdminConfiguration {

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> resetTermAndAddStudents(@RequestBody List<StudentRequestDTO> requests);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> resetSeason();

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeSeasonDate(@RequestBody DateDTO request);


    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeEventDate(@RequestBody DateDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeSeasonEndOnly(@RequestBody LocalDateTime end);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeEventEndOnly(@RequestBody LocalDateTime end);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeFirstSeasonDate(@RequestBody DateDTO request);


}
