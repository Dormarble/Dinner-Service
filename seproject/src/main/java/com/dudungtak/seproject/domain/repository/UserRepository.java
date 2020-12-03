package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.User;
import com.dudungtak.seproject.model.enumpackage.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByTypeEquals(UserType userType, Pageable pageable);

    Page<User> findByTypeNot(UserType userType, Pageable pageable);
}
