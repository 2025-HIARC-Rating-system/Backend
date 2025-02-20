package com.hiarc.Hiting.domain.admin.dto;

import com.hiarc.Hiting.domain.admin.entity.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class StudentRequestDTO {

    private String name;
    private String handle;

    public Students toEntity() {
        return Students.builder()
                .name(name)
                .handle(handle)
                .build();
    }

}
