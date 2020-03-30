package com.example.demo.repository;

import com.example.demo.model.Resume;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
public interface ResumeRepository extends CrudRepository<Resume, Long> {
//    //查找指定id的简历信息
//    @Query(value = "select * from resume where id = :id", nativeQuery = true)
//    Optional<Test> findById(Long id);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update resume set description = :description where ip_address = :ipAddr", nativeQuery = true)
//    void update(@Param("id") Long id, @Param("description") String description);

}
