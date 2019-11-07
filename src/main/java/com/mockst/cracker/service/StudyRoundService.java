package com.mockst.cracker.service;

import com.mockst.cracker.entity.*;
import com.mockst.cracker.entity.em.RoundStatus;
import com.mockst.cracker.exception.BusinessException;
import com.mockst.cracker.model.RoundResultInfo;
import com.mockst.cracker.model.SubmitResultInfo;
import com.mockst.cracker.model.WordInfo;
import com.mockst.cracker.repository.CustomerWordRepository;
import com.mockst.cracker.repository.StudyRoundResultRepository;
import com.mockst.cracker.repository.WordBookRepository;
import com.mockst.cracker.repository.WordExplanationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/31 22:05
 * @Description:
 */
@Service
@Slf4j
public class StudyRoundService extends AbstractService<StudyRoundEntity> {

    private static final Logger Log = LoggerFactory.getLogger(StudyRoundService.class);

    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private StudyRoundResultRepository studyRoundResultRepository;
    @Autowired
    private WordExplanationRepository wordExplanationRepository;
    @Autowired
    private CustomerWordRepository customerWordRepository;
    @Autowired
    private WordService wordService;

    private final static String VOICE_URL = "http://dict.youdao.com/dictvoice?type=%s&audio=%s";

    /**
     * 创建一个新轮次
     *
     * @return
     */
    public StudyRoundEntity createRound(String bookId, String customerId) {
        Optional<WordBookEntity> optional = wordBookRepository.findById(bookId);
        if (optional.isPresent()) {
            WordBookEntity wordBookEntity = optional.get();
            if (wordBookEntity.getLockStatus() == WordBookEntity.LockStatus.locked) {
                throw new BusinessException("词本未解锁");
            } else {
                WordEntity wordEntity = getRanWord(wordBookEntity.getSceneId(), wordBookEntity.getBookType().name(), bookId);
                if (wordEntity != null) {
                    StudyRoundEntity roundEntity = new StudyRoundEntity();
                    roundEntity.setBookId(bookId);
                    roundEntity.setCustomerId(customerId);
                    roundEntity.setRoundStatus(RoundStatus.start);
                    save(roundEntity);

                    //插入数据池
                    String sql = "INSERT INTO tb_study_round_word_pool(round_id,keyword) " +
                            "SELECT ? round_id ,keyword FROM tb_word WHERE scene_id=? AND book_type=? " +
                            "AND keyword NOT IN(SELECT keyword FROM tb_customer_word WHERE `level`=6 AND book_id =?) ";
                    int count = jdbcTemplate.update(sql, new Object[]{roundEntity.getId(), wordBookEntity.getSceneId(), wordBookEntity.getBookType().name(), wordBookEntity.getId()});
                    Log.info("插入数：{}", count);
                    return roundEntity;
                }
            }
        } else {
            throw new BusinessException("词本不存在");
        }
        return null;
    }

    private WordEntity getRanWord(String sceneId, String bookType, String bookId) {
        //查询词本中未满级的词汇
        String sql = "SELECT * FROM tb_word WHERE scene_id=? AND book_type=? AND keyword " +
                "NOT IN(SELECT keyword FROM tb_customer_word WHERE `level`=6 AND book_id =?) " +
                "ORDER BY RAND() LIMIT 1";
        try {
            WordEntity wordEntity =
                    jdbcTemplate.queryForObject(sql, new Object[]{sceneId, bookType, bookId}, new BeanPropertyRowMapper<>(WordEntity.class));
            return wordEntity;
        } catch (EmptyResultDataAccessException e) {
        }
        return null;
    }

    private WordEntity getRanWord(String roundId) {
        //查询词本中未满级的词汇
        String sql = "SELECT w.* FROM tb_word w INNER JOIN tb_study_round_word_pool p ON w.keyword = p.keyword WHERE p.round_id = ? ORDER BY RAND() LIMIT 1";
        try {
            WordEntity wordEntity =
                    jdbcTemplate.queryForObject(sql, new Object[]{roundId}, new BeanPropertyRowMapper<>(WordEntity.class));
            return wordEntity;
        } catch (EmptyResultDataAccessException e) {
        }
        return null;
    }

    /**
     * 创建一个单词
     *
     * @return
     */
    public WordInfo createWord(String roundId) {
        Optional<StudyRoundEntity> optional = findById(roundId);
        if (optional.isPresent()) {
            StudyRoundEntity roundEntity = optional.get();
            if (roundEntity.getRoundStatus() == RoundStatus.over) {
                throw new BusinessException("已结束");
            }
            WordEntity wordEntity = getRanWord(roundId);
            if (wordEntity != null) {
                //更新轮次信息
                if (roundEntity.getRoundStatus() == RoundStatus.start) {
                    roundEntity.setRoundStatus(RoundStatus.in_progress);
                    save(roundEntity);
                }
                WordInfo wordInfo = wordService.findByKeyWord(wordEntity.getKeyword());
                //添加结果信息
                StudyRoundResultEntity result = new StudyRoundResultEntity();
                result.setKeyword(wordInfo.getKeyword());
                result.setRight(false);
                result.setRoundId(roundEntity.getId());
                studyRoundResultRepository.save(result);
                wordInfo.setResultId(result.getId());
                //拼接发音
                wordInfo.setUSVoice(String.format(VOICE_URL, 0, wordInfo.getKeyword()));
                wordInfo.setUKVoice(String.format(VOICE_URL, 1, wordInfo.getKeyword()));
                return wordInfo;
            }
        }
        return null;
    }

    /**
     * 提交单词信息
     *
     * @param resultId
     * @param explanation
     * @return
     */
    public SubmitResultInfo submitWord(String resultId, String explanation) {
        Optional<StudyRoundResultEntity> optional = studyRoundResultRepository.findById(resultId);
        if (optional.isPresent()) {
            StudyRoundResultEntity resultEntity = optional.get();
            if (StringUtils.isNotBlank(resultEntity.getSubmitExplanation())) {
                throw new BusinessException("已提交过");
            }
            String keyword = resultEntity.getKeyword();
            List<String> explanations = wordExplanationRepository.findByKeyword(resultEntity.getKeyword())
                    .stream().map(w -> w.getExplanation()).collect(Collectors.toList());
            boolean isRight = explanations.contains(explanation);
            resultEntity.setRight(isRight);
            resultEntity.setSubmitExplanation(explanation);
            StudyRoundEntity round = findById(resultEntity.getRoundId()).get();
            //记录学习信息
            List<CustomerWordEntity> words = customerWordRepository.findByKeywordAndCustomerId(keyword, round.getCustomerId());
            CustomerWordEntity customerWordEntity;
            if (words.isEmpty()) {
                customerWordEntity = new CustomerWordEntity();
                customerWordEntity.setBookId(round.getBookId());
                customerWordEntity.setCustomerId(round.getCustomerId());
                customerWordEntity.setKeyword(keyword);
                customerWordEntity.setLevel(isRight ? 1 : 0);
            } else {
                customerWordEntity = words.get(0);
                int level = customerWordEntity.getLevel();
                level = isRight ? level + 1 : level - 1;
                if (level > 6) {
                    level = 6;
                }
                if (level < 0) {
                    level = 0;
                }
                customerWordEntity.setLevel(level);
            }
            customerWordRepository.save(customerWordEntity);
            //记录结果
            resultEntity.setLevel(customerWordEntity.getLevel());
            studyRoundResultRepository.save(resultEntity);
            //返回是否有下一个单词
            boolean isNext = customerWordRepository.countByBookIdAndLevelLessThan(round.getBookId(), 6) > 0;
            //单词全部满级
            if (!isNext) {
                round.setRoundStatus(RoundStatus.over);
                save(round);
            }
            SubmitResultInfo submitResultInfo = new SubmitResultInfo();
            submitResultInfo.setRight(isRight);
            submitResultInfo.setHaveNext(isNext);
            return submitResultInfo;
        }
        return new SubmitResultInfo();
    }

    /**
     * 结束轮次获取轮次结果
     */
    public RoundResultInfo stopRound(String roundId) {
        Optional<StudyRoundEntity> optional = findById(roundId);
        if (optional.isPresent()) {
            StudyRoundEntity studyRoundEntity = optional.get();
            if (studyRoundEntity.getRoundStatus() != RoundStatus.over) {
                studyRoundEntity.setRoundStatus(RoundStatus.over);
                save(studyRoundEntity);
            }
            RoundResultInfo resultInfo = new RoundResultInfo();
            List<StudyRoundResultEntity> results = studyRoundResultRepository.findByRoundId(roundId);
            long all = results.stream().map(StudyRoundResultEntity::getKeyword).collect(Collectors.toSet()).size();
            long rights = results.stream().filter(StudyRoundResultEntity::isRight).count();
            resultInfo.setStudyQuantity(all);
            resultInfo.setRightQuantity(rights);
            resultInfo.setErrorQuantity(results.size() - rights);
            return resultInfo;
        }
        return null;
    }
}
