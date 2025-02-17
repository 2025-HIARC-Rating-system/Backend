package com.hiarc.Hiting.domain.hiting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Solved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int eachSolved;


    @OneToOne
    @JoinColumn(name = "hitingId")
    private Hiting hiting;

    @Builder
    public Solved(int level, int eachSolved) {
        this.level = level;
        this.eachSolved = eachSolved;
    }

}
