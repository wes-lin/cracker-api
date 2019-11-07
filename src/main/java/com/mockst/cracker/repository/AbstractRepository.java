package com.mockst.cracker.repository;

import com.mockst.cracker.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 09:49
 * @Description:
 */
public interface AbstractRepository<E extends AbstractEntity> extends JpaRepository<E, String>, JpaSpecificationExecutor<E> {
}
