package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);

    @Query(value = "select * from user where user_name like %?1%", nativeQuery = true)
    List<User> findByNameLike(String name);

    @Query(value = "select * from user where id_card like %?1%", nativeQuery = true)
    List<User> findByIdCardLike(String idCard);

    Page<User> findAll(@PageableDefault(value = 15, sort = {"datetime"}, direction = Sort.Direction.DESC) Pageable pageable);
}
