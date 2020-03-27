package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Api(value = "测试")
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    UserRepository repository;

    //根据编号查询
    @ApiOperation(value = "根据ID查询用户信息")
    @PostMapping(value = "/findById")
    User findById(@RequestParam(value = "id") Long id) {
        Optional<User> user = repository.findById(id);
        return user.get();
    }

    @ApiOperation(value = "根据姓名查询用户信息")
    @PostMapping(value = "findName")
    List<User> findName(@RequestParam(value = "name") String name) {
        return repository.findByNameLike(name);
    }

    @ApiOperation(value = "查询所有")
    @PostMapping(value = "findAll")
        //currentPage 当前页，默认为0，如果传1的话是查的第二页数据
        //pageSize 每页数据条数
    Page<User> findAll(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "pageSize") int pageSize) {
        Pageable pageable = PageUtil.getPageable(currentPage, pageSize, "datetime");
        return repository.findAll(pageable);
    }
}
