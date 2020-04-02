package com.example.demo.repository;

import com.example.demo.model.SignUp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface SignUpRepository extends CrudRepository<SignUp, Long> {
    //根据工种获取报名信息
    @Query(value = "select * from signup where apply_work_type = :applyWorkType and create_time like :year%", nativeQuery = true)
    List<SignUp> getSignUpList(@Param("year") String applyWorkType, @Param("year") String year);

    @Query(value = "select * from signup where user_id = :id and create_time like :year%", nativeQuery = true)
    List<SignUp> getList(Long id, String year);

    @Query(value = "select * from signup where user_id = :id", nativeQuery = true)
    SignUp getOne(Long id);

}
