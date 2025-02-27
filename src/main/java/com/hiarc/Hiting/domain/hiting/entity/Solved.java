package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"studentId","level"}) // 학생+레벨 유니크
})
public class Solved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer level;
    @Column(nullable = false)
    private Integer eachSolved;


    @ManyToOne
    @JoinColumn(name = "studentsId")
    private Students students;

    @Builder
    public Solved(Integer level, Integer eachSolved) {
        this.level = level;
        this.eachSolved = eachSolved;
    }

    public void updateEachSolved(Integer eachSolved) {
        this.eachSolved = eachSolved;
    }

}
