package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.domain.event.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import com.hiarc.Hiting.domain.streak.entity.Streak;
import com.hiarc.Hiting.global.enums.TierCategory;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 어느 곳에서나 객체를 생성할 수 있는 상황을 막기위
@EntityListeners(AuditingEntityListener.class) //created date 사용하기 위해
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentsId")
    private Long id;

    @Column(nullable = false)
    private String name;

    //Default
    private Integer tier_level;

    @Column(nullable = false)
    private String handle;

    //Default
    private Integer divNum = 0;

    @CreatedDate
    private LocalDate hitingStart;


    //다른 그룹과의 관계
    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hiting hiting;

    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Streak streak;

    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Event event;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private List<Solved> solveds = new ArrayList<>();


    @Builder
    public Students(String name, Integer tier_level, String handle, Integer divNum, LocalDate hitingStart) {
        this.name = name;
        this.tier_level = (tier_level == null) ? 0 : tier_level; // 조건값 아니면 defult 값 지정
        this.handle = handle;
        this.divNum = (divNum == null) ? 0 : divNum;
        this.hitingStart = hitingStart;
    }


    public Students updateTierLevel(Integer tier_level) {
        this.tier_level = tier_level;
        return this;
    }

    public Students updateDivNum(Integer divNum) {
        this.divNum= divNum;
        return this;
    }



}
