package com.mockst.cracker.repository;

import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.entity.em.BookTypeEnum;
import org.springframework.stereotype.Repository;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 22:43
 * @Description:
 */
@Repository
public interface WordRepository extends AbstractRepository<WordEntity> {

    long countByBookTypeAndSceneId(BookTypeEnum bookType,String sceneId);


}
