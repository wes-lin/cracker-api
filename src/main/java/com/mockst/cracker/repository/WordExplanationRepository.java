package com.mockst.cracker.repository;

import com.mockst.cracker.entity.WordExplanationEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/29 20:13
 * @Description:
 */
@Repository
public interface WordExplanationRepository extends AbstractRepository<WordExplanationEntity> {

    List<WordExplanationEntity> findByKeyword(String keyWord);

    List<WordExplanationEntity> findByKeywordNot(String keyWord);

}
