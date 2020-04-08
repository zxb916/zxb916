package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);

    @Query(value = "select * from user where user_name = :userName", nativeQuery = true)
    List<User> findByNameLike(String userName);

    @Query(value = "select * from user where id_card = :idCard", nativeQuery = true)
    List<User> findByIdCardLike(String idCard);

    Page<User> findAll(@PageableDefault(value = 15, sort = {"datetime"}, direction = Sort.Direction.DESC) Pageable pageable);

    @Override
    void delete(User user);

    @Query(value = "select * from user where id_card = :idCard and  user_name = :userName and mobile = :mobile", nativeQuery = true)
    User findUser(@Param("idCard") String idCard, @Param("userName") String userName, @Param("mobile") String mobile);
}
