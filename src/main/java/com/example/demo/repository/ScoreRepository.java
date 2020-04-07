package com.example.demo.repository;

import com.example.demo.model.Score;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
    //根据身份证和年份查询成绩列表
    @Query(value = "select * from score where signUp_id = :signId ", nativeQuery = true)
    Score getUserScore(@Param("signId") Long signId);

    //根据报名id获取成绩
    @Query(value = "select * from score where id = :id", nativeQuery = true)
    Score getScore(Long id);
}
