package com.example.demo.repository;

import com.example.demo.model.Resume;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResumeRepository extends CrudRepository<Resume, Long> {

    @Query(value = "delete from resume where user_id = :userId", nativeQuery = true)
    void deleteByUserId(Long userId);

}
