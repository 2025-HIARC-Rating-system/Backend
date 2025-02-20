package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Student;
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
    @JoinColumn(name = "hitingId")
    private Hiting hiting;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @Builder
    public Solved(int level, int eachSolved) {
        this.level = level;
        this.eachSolved = eachSolved;
    }

    public void setEachSolved(Integer eachSolved) {
        this.eachSolved = eachSolved;
    }

    public Integer getEachSolved() {
        return eachSolved;
    }

}
