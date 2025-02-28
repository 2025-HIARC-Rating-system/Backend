package com.hiarc.Hiting.domain.hiting.controller;


import com.hiarc.Hiting.domain.hiting.dto.view.HandleResponseDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.RankingDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.WrapMainDTO;
import com.hiarc.Hiting.domain.hiting.dto.view.WrapStreakListDTO;
import com.hiarc.Hiting.domain.hiting.service.ViewService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.apiPayload.code.status.SuccessStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "View", description = "뷰 관련 API")
@RequestMapping
public class ViewController implements ViewConfiguration{

    private final ViewService viewService;


    @GetMapping("/streak")
    @Operation(summary = "스트릭 목차 반환 API", description = "스트릭 상세 페이지. 누적 스트릭이 가장 큰 사람부터 보여줌")
    public ResponseEntity<ApiResponse<WrapStreakListDTO>> streakListView(){
        try {
            WrapStreakListDTO response = viewService.wrapStreakListData();
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(SuccessStatus.OK, response));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.DATE_NOT_FOUND, null));
        }

    }

    @GetMapping("/rating/{div}")
    @Operation(summary = "디비전별로 목차 반환 API", description = "디비전 상세 페이지. seasonHiting이 가장 큰 사람부터 보여줌")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> divListView(@PathVariable("div") int div){
        try {
            List<RankingDTO> response = viewService.wrapDivRankData(div);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(SuccessStatus.OK, response));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.HITING_NOT_FOUND, null));
        }

    }

    @GetMapping("/search")
    @Operation(summary = "검색 및 핸들별 정보 API", description = "핸들별 상세 페이지. 검색했을때의 결과")
    public ResponseEntity<ApiResponse<HandleResponseDTO>> findByHandleView(@RequestParam String handle){
        try {
            HandleResponseDTO response = viewService.wrapByHandle(handle);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(SuccessStatus.OK, response));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.MEMBER_NOT_FOUND, null));
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.of(ErrorStatus.HITING_NOT_FOUND, null));
        }

    }

    @GetMapping("/")
    @Operation(summary = "메인 화면 API")
    public ResponseEntity<ApiResponse<?>> mainView(){
        WrapMainDTO response = viewService.wrapMainData();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(SuccessStatus.OK, response));

    }




}
