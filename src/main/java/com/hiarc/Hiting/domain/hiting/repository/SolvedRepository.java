package com.hiarc.Hiting.domain.hiting.repository;

import com.hiarc.Hiting.domain.admin.entity.Student;
import com.hiarc.Hiting.domain.hiting.entity.Solved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolvedRepository extends JpaRepository<Solved, Long> {
    Optional<Solved> findByStudentAndLevel(Student student, Integer level);
    Optional<Solved> findByStudent(Student student);
}
