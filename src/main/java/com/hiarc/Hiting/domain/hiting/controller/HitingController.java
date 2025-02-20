package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.hiting.service.HitingService;
import com.hiarc.Hiting.domain.hiting.service.SolvedService;
import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.apiPayload.code.status.SuccessStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Tag(name = "Hiting", description = "하이팅 계산 관련 API ") //API 큰묶음
@RequestMapping("/hiting")
public class HitingController {

    private final HitingService hitingService;

}
