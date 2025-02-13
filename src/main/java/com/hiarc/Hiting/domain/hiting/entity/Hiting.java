package com.hiarc.Hiting.domain.hiting.entity;

import com.hiarc.Hiting.domain.admin.entity.Student;
import jakarta.persistence.*;

@Entity
public class Hiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hitingId", updatable = false, nullable = false)
    private Long id;



    """@OneToOne
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;"""
}
