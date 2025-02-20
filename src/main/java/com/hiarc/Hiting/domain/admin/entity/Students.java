package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.domain.event.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import com.hiarc.Hiting.domain.streak.entity.Streak;
import com.hiarc.Hiting.global.enums.TierCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) //created date 사용하기 위해
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId")
    private Long id;

    @Column(nullable = false)
    private String name;
    private int tier_level = 0;
    @Column(nullable = false)
    private String handle;
    private int div = 0;

    @CreatedDate
    private LocalDate hitingStart;

    // 숫자로 Enum 변환
    public TierCategory getTier() {
        return TierCategory.fromLeveltoEnum(this.tier_level);
    }

    //다른 그룹과의 관계
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hiting hiting;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Solved> solved = new ArrayList<>();

    @Builder
    public Student(String name, int tier_level, String handle, int div, LocalDate hitingStart) {
        this.name = name;
        this.tier_level = tier_level;
        this.handle = handle;
        this.div = div;
        this.hitingStart = hitingStart;
    }

}
