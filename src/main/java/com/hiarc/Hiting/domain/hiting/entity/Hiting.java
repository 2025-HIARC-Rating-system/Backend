package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Students;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

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
    @ColumnDefault("0")
    private Integer eventHiting;


    @OneToOne
    @JoinColumn(name = "studentsId")
    private Students students;


    @Builder
    public Hiting(Integer dailyHiting, Integer totalHiting, Integer seasonHiting, Integer eventHiting) {
        this.dailyHiting = dailyHiting;
        this.totalHiting = totalHiting;
        this.seasonHiting = seasonHiting;
        this.eventHiting = eventHiting;
    }

    public void updateStudent(Students student) {
        this.students = student;
    }

    public void updateDailyHiting(Integer dailyHiting) {
        this.dailyHiting = dailyHiting;
    }

    public void updateSeasonHiting(Integer seasonHiting) {
        this.seasonHiting = seasonHiting;
    }

    public void updateTotalHiting(Integer totalHiting) {
        this.totalHiting = totalHiting;
    }

    public void updateEventHiting(Integer eventHiting) {
        this.eventHiting = eventHiting;
    }

    public void addDailyHiting(Integer delta) {
        this.dailyHiting += delta;
    }

    public void addSeasonHiting(Integer delta) {
        this.seasonHiting += delta;
    }

    public void addTotalHiting(Integer delta) {
        this.totalHiting += delta;
    }

    public void addEventHiting(Integer delta){
        this.eventHiting += delta;
    }


}
