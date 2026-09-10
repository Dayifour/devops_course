package com.odv.devops.repositorie;

import com.odv.devops.entitie.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Students, Long> {
}
