package com.hiarc.Hiting.domain.admin.repository;

import com.hiarc.Hiting.domain.admin.entity.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {
}
