package com.mockst.cracker.repository;

import com.mockst.cracker.entity.StudyRoundResultEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/31 23:16
 * @Description:
 */
@Repository
public interface StudyRoundResultRepository extends AbstractRepository<StudyRoundResultEntity> {

    List<StudyRoundResultEntity> findByRoundId(String roundId);
}
