package com.hiarc.Hiting.domain.admin.dto;

import com.hiarc.Hiting.domain.admin.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
public class StudentRequestDTO {

    private String name;
    private String handle;

    public Student toEntity() {
        return Student.builder()
                .name(name)
                .handle(handle)
                .build();
    }

}
