package com.mockst.cracker.service;

import com.mockst.cracker.entity.AbstractEntity;
import com.mockst.cracker.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 10:14
 * @Description:
 */
public abstract class AbstractService<E extends AbstractEntity> {

    @Autowired
    protected AbstractRepository<E> repository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public E save(E e) {
        return repository.save(e);
    }

    public Optional<E> findById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<E> findAll(Example<E> example) {
        return repository.findAll(example);
    }

    public long count(Example<E> example) {
        return repository.count(example);
    }

}
