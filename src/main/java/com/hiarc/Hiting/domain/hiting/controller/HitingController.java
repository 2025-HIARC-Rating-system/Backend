package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.dto.RankingDTO;
//import com.hiarc.Hiting.domain.hiting.service.HitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Tag(name = "raking", description = "하이팅 계산 관련 API ") //API 큰묶음
@RequestMapping("/ranking")
public class HitingController {

    //private final HitingService hitingService;
    //private final StudentRepository studentRepository;






}
