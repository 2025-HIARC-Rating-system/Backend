package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hitingId")
    private Long id;

    @ColumnDefault("0")
    private Integer dailyHiting;
    @ColumnDefault("0")
    private Integer totalHiting;
    @ColumnDefault("0")
    private Integer seasonHiting;


    @OneToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @OneToMany(mappedBy = "hiting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Solved solved;

    @Builder
    public Hiting(Integer dailyHiting, Integer totalHiting, Integer seasonHiting) {
        this.dailyHiting = dailyHiting;
        this.totalHiting = totalHiting;
        this.seasonHiting = seasonHiting;
    }



}
