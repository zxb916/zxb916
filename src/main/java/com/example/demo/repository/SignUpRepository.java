package com.example.demo.repository;

import com.example.demo.model.SignUp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface SignUpRepository extends CrudRepository<SignUp, Long> {
    //根据工种获取报名信息
    @Query(value = "select * from signup where already_work_type = :alreadyWorkType /*and create_time =:createTime*/", nativeQuery = true)
    List<SignUp> getSignUpList(String alreadyWorkType/*, Date createTime*/);

    @Query(value = "select * from signup where user_id = :id and create_time =:createTime", nativeQuery = true)
    List<SignUp> getList(Long id, Date createTime);

    @Query(value = "select * from signup where user_id = :id", nativeQuery = true)
    SignUp getOne(Long id);

}
