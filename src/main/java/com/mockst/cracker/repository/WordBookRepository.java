package com.mockst.cracker.repository;

import com.mockst.cracker.entity.WordBookEntity;
import com.mockst.cracker.entity.em.BookTypeEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/29 20:14
 * @Description:
 */
@Repository
public interface WordBookRepository extends AbstractRepository<WordBookEntity> {

    List<WordBookEntity> findByBookTypeAndCustomerId(BookTypeEnum bookType, String customerId);

}
