package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.dto.SolvedResponseTierDTO;
import com.hiarc.Hiting.domain.hiting.dto.SolvedResponseDTO;
import com.hiarc.Hiting.domain.hiting.repository.SolvedRepository;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedService {

    private final RestTemplate restTemplate; //solvedAc 위해

    public SolvedService() {this.restTemplate = new RestTemplate();}

    public List<SolvedResponseDTO> SolvedListByHandle(String handle) {
        String url = "https://solved.ac/api/v3/user/problem_stats?handle=" + handle;
        ResponseEntity<List<SolvedResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SolvedResponseDTO>>() {}
        );
        List<SolvedResponseDTO> dtoList = response.getBody();

        if (dtoList == null) {
            throw new RuntimeException(ErrorStatus.OPEN_API_FAIL.getMessage());
        }

        return dtoList;
    }
}
