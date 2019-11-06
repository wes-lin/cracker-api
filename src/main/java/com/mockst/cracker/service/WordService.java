package com.mockst.cracker.service;

import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.entity.WordExplanationEntity;
import com.mockst.cracker.model.WordInfo;
import com.mockst.cracker.repository.WordExplanationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/30 21:58
 * @Description:
 */
@Service
public class WordService extends AbstractService<WordEntity> {

    @Autowired
    private WordExplanationRepository wordExplanationRepository;

    public WordInfo findByKeyWord(String keyword){
        String sql = "SELECT w.keyword,w.book_type,w.uk_voice,w.us_voice,cw.`level`,wb.book_name,cw.book_id "
                    +"FROM tb_word w LEFT JOIN tb_customer_word cw ON cw.keyword = w.keyword LEFT JOIN tb_word_book wb ON wb.id = cw.book_id "
                    +"WHERE w.keyword = ?";
        WordInfo wordInfo = jdbcTemplate.queryForObject(sql,new Object[]{keyword},new BeanPropertyRowMapper<>(WordInfo.class));
        if (wordInfo==null){
            return null;
        }
        //查询正确释义
        List<WordExplanationEntity> explanations = wordExplanationRepository.findByKeyword(keyword);
        Random r = new Random();
        List<String> ee = explanations.stream().map(WordExplanationEntity::getExplanation).collect(Collectors.toList());
        int re = r.ints(0,ee.size()).findFirst().getAsInt();

        //错误释义
        List<WordExplanationEntity> errors = wordExplanationRepository.findByKeywordNot(keyword);
        List<String> eeS = errors.stream().map(WordExplanationEntity::getExplanation).collect(Collectors.toList());
        //移除相同
        eeS.removeAll(ee);
        int rerror = r.ints(0,eeS.size()).findFirst().getAsInt();
        int index = r.ints(0,2).findAny().getAsInt();
        if (index%2==0){
            wordInfo.setExplanation1(ee.get(re));
            wordInfo.setExplanation2(eeS.get(rerror));
        }else {
            wordInfo.setExplanation1(eeS.get(rerror));
            wordInfo.setExplanation2(ee.get(re));
        }
        return wordInfo;
    }
}
