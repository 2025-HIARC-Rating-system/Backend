package com.hiarc.Hiting.domain.hiting.controller;

import com.hiarc.Hiting.domain.admin.entity.Student;
import com.hiarc.Hiting.domain.admin.repository.StudentRepository;
import com.hiarc.Hiting.domain.hiting.dto.RankingDTO;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class HitingController {

    private final HitingService hitingService;
    private final StudentRepository studentRepository;

    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> getRankingList() {
        List<Student> students = studentRepository.findAll();

        List<RankingDTO> rankingList = students.stream()
                .map(student -> new RankingDTO(
                        student.getHandle(),
                        student.getTier_level(),
                        true,
                        200,
                        336
                ))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("rankingList", rankingList);

        return ResponseEntity.ok(result);
    }





}
