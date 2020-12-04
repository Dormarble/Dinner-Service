package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.Menu;
import com.dudungtak.seproject.entity.MenuElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MenuElementRepository extends JpaRepository<MenuElement, Long> {
    @Transactional
    void deleteAllByMenu(Menu menu);
}
