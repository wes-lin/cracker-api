package com.mockst.cracker.repository;

import com.mockst.cracker.entity.CustomerWordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/29 20:17
 * @Description:
 */
@Repository
public interface CustomerWordRepository extends AbstractRepository<CustomerWordEntity>{

    List<CustomerWordEntity> findByBookId(String bookId);

    List<CustomerWordEntity> findByKeywordAndCustomerId(String keyword,String customerId);

    long countByBookIdAndLevelLessThan(String bookId,int level);

}
