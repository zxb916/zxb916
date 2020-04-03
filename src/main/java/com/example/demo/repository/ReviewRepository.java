package com.example.demo.repository;

import com.example.demo.model.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<SignUp, Long> {


    @Query(value = "select * from SignUp where apply_work_type =:applyWorkType and create_time like :year%", nativeQuery = true)
    List<SignUp> getList(@Param("applyWorkType") String applyWorkType, @Param("year") String year);

    @Query(value = "select * from SignUp where user_id = :userId", nativeQuery = true)
    SignUp findByUserId(@Param("userId") Long id);

    @Query(value = "select * from SignUp where apply_work_type =:applyWorkType and create_time like ':year%' and check = 1", nativeQuery = true)
    List<SignUp> getReviewList(@Param("applyWorkType") String applyWorkType, @Param("year") String year);
}
