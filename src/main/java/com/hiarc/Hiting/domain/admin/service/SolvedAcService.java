package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.SolvedResponseTierDTO;
import com.hiarc.Hiting.domain.hiting.dto.SolvedResponseDTO;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.GeneralException;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedAcService {

    private final RestTemplate restTemplate; //solvedAc 위해

    public SolvedAcService() {this.restTemplate = new RestTemplate();}

    public List<SolvedResponseDTO> SolvedListByHandle(String handle) {
        String url = "https://solved.ac/api/v3/user/problem_stats?handle=" + handle;
        ResponseEntity<List<SolvedResponseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SolvedResponseDTO>>() {}
        );
        List<SolvedResponseDTO> solvedList = response.getBody();

        if (solvedList == null) {
            throw new RuntimeException(ErrorStatus.OPEN_API_FAIL.getMessage());
        }

        return solvedList;
    }

    public int getTierByHandle(String handle) {
        String url = "https://solved.ac/api/v3/user/show?handle=" + handle;
        try {
            SolvedResponseTierDTO tierDTO = restTemplate.getForObject(url, SolvedResponseTierDTO.class); //단일 객체일때
            if (tierDTO == null) {
                throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);
            }
            return tierDTO.getTier();
        } catch (RestClientException e) {
            throw new GeneralException(ErrorStatus.OPEN_API_FAIL);
        }

    }
}
