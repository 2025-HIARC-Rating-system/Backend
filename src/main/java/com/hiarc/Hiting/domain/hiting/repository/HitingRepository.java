package com.hiarc.Hiting.domain.hiting.repository;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HitingRepository extends JpaRepository <Hiting, Long> {
    Hiting findByStudents(Students student);

    @Modifying
    @Query("UPDATE Hiting h SET h.seasonHiting = 0")
    void resetSeasonHitingForAll();

}
