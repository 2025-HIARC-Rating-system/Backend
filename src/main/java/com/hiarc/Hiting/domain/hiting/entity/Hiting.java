package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private int dailyHiting;
    @ColumnDefault("0")
    private int totalHiting;
    @ColumnDefault("0")
    private int seasonHiting;


    @OneToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @OneToOne(mappedBy = "hiting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Solved solved;



}
