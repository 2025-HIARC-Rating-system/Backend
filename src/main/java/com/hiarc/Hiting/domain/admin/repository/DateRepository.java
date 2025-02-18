package com.hiarc.Hiting.domain.admin.repository;

import com.hiarc.Hiting.domain.admin.entity.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {
    Optional<Date> findTopByOrderByIdAsc(); //오름차순 기준 id가 제일 작은거 불러옴
}
