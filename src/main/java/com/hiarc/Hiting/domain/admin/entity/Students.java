package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 어느 곳에서나 객체를 생성할 수 있는 상황을 막기위
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


    //다른 그룹과의 관계
    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hiting hiting;

    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Streak streak;

    @OneToOne(mappedBy = "students", cascade = CascadeType.ALL, orphanRemoval = true)
    private Event event;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private List<Solved> solveds = new ArrayList<>();

    @OneToOne(mappedBy = "students")
    private RecentSeason recentSeason;


    @Builder
    public Students(String name, Integer tier_level, String handle, Integer divNum) {
        this.name = name;
        this.tier_level = (tier_level == null) ? 0 : tier_level; // 조건값 아니면 defult 값 지정
        this.handle = handle;
        this.divNum = (divNum == null) ? 0 : divNum;
        this.hiting = new Hiting(0, 0, 0, 0); //학생등록시 Hiting도 등록됨
        this.streak = new Streak(false, null, null);
        this.event = new Event(null, null, 0);
        this.hiting.updateStudentHiting(this); //양방향 참조 동기화
        this.streak.updateStudentStreak(this);
        this.event.updateStudentEvent(this);
    }


    public void updateTierLevel(Integer tier_level) {
        this.tier_level = tier_level;
    }

    public void updateDivNum(Integer divNum) {
        this.divNum= divNum;
    }



}
