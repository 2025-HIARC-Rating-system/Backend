package com.hiarc.Hiting.domain.streak.repository;

import com.hiarc.Hiting.domain.streak.entity.Streak;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {

}
