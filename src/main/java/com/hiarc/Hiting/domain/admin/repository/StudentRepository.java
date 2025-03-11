package com.hiarc.Hiting.domain.admin.repository;

import com.hiarc.Hiting.domain.admin.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long>{
    Optional<Students> findByHandle(String handle);
    List<Students> findByDivNum(int div);
}