package com.hiarc.Hiting.domain.admin.repository;

import com.hiarc.Hiting.domain.admin.entity.Date;
import com.hiarc.Hiting.domain.admin.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    boolean existsByHandle(String handle);
    List<Student> findAllByHandleIn(List<String> handles);
    Optional<Student> findByHandle(String handle);


}