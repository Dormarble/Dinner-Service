package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.MenuElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuElementRepository extends JpaRepository<MenuElement, Long> {
}
