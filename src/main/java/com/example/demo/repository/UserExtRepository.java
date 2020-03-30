package com.example.demo.repository;

import com.example.demo.model.UserExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtRepository extends JpaRepository<UserExt,Long> {
}
