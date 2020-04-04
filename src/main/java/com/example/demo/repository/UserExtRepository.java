package com.example.demo.repository;

import com.example.demo.model.UserExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtRepository extends JpaRepository<UserExt, Long> {

    @Query(value = "delete from user_ext where user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);

}
