package com.hiarc.Hiting.domain.admin.controller;

import com.hiarc.Hiting.domain.admin.dto.DateDTO;
import com.hiarc.Hiting.domain.admin.dto.StudentRequestDTO;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


//코드 전반적으로 수정 필요 (예외처리 추가할 것)

@Tag(name = "Admin", description = "관리자 페이지 관련 API ") // Swagger API 큰묶음
public interface AdminConfiguration {

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> addStudent(@RequestBody StudentRequestDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<?>> addStudents(@RequestBody List<StudentRequestDTO> requests);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<?> changeTierDivNum(@RequestParam String handle);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeSeasonDate(@RequestBody DateDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeFirstSeasonDate(@RequestBody DateDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeEventDate(@RequestBody DateDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeSeasonEndOnly(@RequestBody DateDTO request);

    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    ResponseEntity<ApiResponse<Void>> changeEventEndOnly(@RequestBody DateDTO request);


}
