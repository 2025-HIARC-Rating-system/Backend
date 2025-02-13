package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.global.enums.TierCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="student")
@EntityListeners(AuditingEntityListener.class) //created date 사용하기 위해
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId")
    private Long id;

    private String name;
    private int tier_level;
    private String handle;
    private String div;

    @CreatedDate
    private LocalDate hitingStart;

    // 숫자로 Enum 변환
    public TierCategory getTier() {
        return TierCategory.fromLeveltoEnum(this.tier_level);
    }

"""    @OneToOne
    private Hiting hiting;"""


}
