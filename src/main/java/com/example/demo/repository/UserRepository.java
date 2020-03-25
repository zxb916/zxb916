package com.example.demo.repository;

import com.example.demo.model.Test;
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
public interface UserRepository extends CrudRepository<Test, Long> {

    Optional<Test> findById(Long id);

    @Query(value = "select * from t_user where name like %?1%", nativeQuery = true)
    List<Test> findByNameLike(String name);

    Page<Test> findAll(@PageableDefault(value = 15, sort = {"datetime"}, direction = Sort.Direction.DESC) Pageable pageable);
}
