package com.hiarc.Hiting.domain.hiting.repository;

import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitingRepository extends JpaRepository <Hiting, Long> {}
