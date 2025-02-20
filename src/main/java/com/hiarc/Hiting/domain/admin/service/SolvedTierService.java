package com.hiarc.Hiting.domain.admin.service;

import com.hiarc.Hiting.domain.admin.dto.SolvedResponseTierDTO;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import com.hiarc.Hiting.global.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SolvedTierService {

    private final RestTemplate restTemplate; //solvedAc 위해

    public SolvedTierService() {
        this.restTemplate = new RestTemplate();
    }

    public int getTierByHandle(String handle) {
        String url = "https://solved.ac/api/v3/user/show?handle=" + handle;
        SolvedResponseTierDTO tierDTO = restTemplate.getForObject(url, SolvedResponseTierDTO.class);

        if (tierDTO == null) {
            throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        return tierDTO.getTier();
    }

}


