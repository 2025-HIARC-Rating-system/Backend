package com.hiarc.Hiting.domain.hiting.repository;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.hiting.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {
    Streak findByStudents(Students student);
}
