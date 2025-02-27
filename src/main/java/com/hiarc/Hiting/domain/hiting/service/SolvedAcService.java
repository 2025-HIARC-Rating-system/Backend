package com.hiarc.Hiting.domain.hiting.service;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.dto.SolvedResponseDTO;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
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
        ); //리스트일때 등 받을 수 있음, 구체적 타입으로 받을 수 있음, 리스트 매핑
        List<SolvedResponseDTO> solvedList = response.getBody();

        if (solvedList == null) {
            throw new RuntimeException(ErrorStatus.OPEN_API_FAIL.getMessage());
        }

        return solvedList;
    }

    @Repository
    public static interface EventRepository extends JpaRepository<Event, Long> {

        Event findByStudents(Students student);
    }
}
