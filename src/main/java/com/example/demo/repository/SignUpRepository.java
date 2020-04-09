package com.example.demo.repository;

import com.example.demo.model.SignUp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignUpRepository extends CrudRepository<SignUp, Long> {
    //根据工种获取报名信息
    @Query(value = "select * from sign_up where apply_work_type = :applyWorkType and create_time like :year%", nativeQuery = true)
    List<SignUp> getSignUpList(@Param("applyWorkType") String applyWorkType, @Param("year") String year);

    @Query(value = "select * from sign_up where user_id = :id and create_time like :year%", nativeQuery = true)
    List<SignUp> getList(Long id, String year);

    @Query(value = "select * from sign_up where user_id = :id", nativeQuery = true)
    SignUp getOne(Long id);

    @Query(value = "select * from sign_up where user_id = :user_id", nativeQuery = true)
    List<SignUp> findByUserId(Long user_id);

    @Query(value = "select * from sign_up where user_id = :user_id  and create_time like :year%", nativeQuery = true)
    SignUp findByUserIdAndYear(Long user_id, String year);

    @Query(value = "select * from sign_up where create_time like :year%", nativeQuery = true)
    List<SignUp> getListAll(@Param("year") String year);

    //查询去年相同工种和技能等级的报名情况
    @Query(value = "select * from sign_up where id = :id and create_time like :lastYear%", nativeQuery = true)
    SignUp findLastYear(@Param("id") Long id, @Param("lastYear") String lastYear);
}
