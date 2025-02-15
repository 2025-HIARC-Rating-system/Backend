package com.hiarc.Hiting.domain.admin.entity;

import com.hiarc.Hiting.domain.event.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import com.hiarc.Hiting.domain.streak.entity.Streak;
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

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int tier_level;
    @Column(nullable = false)
    private String handle;
    @Column(nullable = false)
    private String div;

    @CreatedDate
    private LocalDate hitingStart;

    // 숫자로 Enum 변환
    public TierCategory getTier() {
        return TierCategory.fromLeveltoEnum(this.tier_level);
    }


    //다른 그룹과의 관계
    @OneToOne(mappedBy = "hiting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hiting hiting;

    @OneToOne(mappedBy = "streak", cascade = CascadeType.ALL, orphanRemoval = true)
    private Streak streak;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Event event;





}
