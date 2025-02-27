package com.hiarc.Hiting.domain.hiting.dto;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolvedResponseDTO {
    private Integer level;
    private Integer solved;
}
