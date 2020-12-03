package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
}
