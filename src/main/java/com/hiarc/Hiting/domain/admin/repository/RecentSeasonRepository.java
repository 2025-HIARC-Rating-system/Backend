package com.hiarc.Hiting.domain.admin.repository;

import com.hiarc.Hiting.domain.admin.entity.RecentSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentSeasonRepository extends JpaRepository<RecentSeason, Long> {
    Optional<RecentSeason> findByHandle(String handle);

    List<RecentSeason> findByDivNum(int i);
}
