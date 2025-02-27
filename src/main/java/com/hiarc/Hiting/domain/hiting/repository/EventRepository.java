package com.hiarc.Hiting.domain.hiting.repository;

import com.hiarc.Hiting.domain.admin.entity.Students;
import com.hiarc.Hiting.domain.hiting.entity.Event;
import com.hiarc.Hiting.domain.hiting.entity.Hiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByStudents(Students student);
}
