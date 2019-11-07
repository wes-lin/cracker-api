package com.mockst.cracker.service;

import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.entity.WordExplanationEntity;
import com.mockst.cracker.entity.em.BookTypeEnum;
import com.mockst.cracker.entity.em.WordTypeEnum;
import com.mockst.cracker.model.WordInfo;
import com.mockst.cracker.repository.WordExplanationRepository;
import com.mockst.cracker.repository.WordRepository;
import com.mockst.cracker.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.mockst.cracker.util.ExcelUtil.isMergedRegion;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/30 21:58
 * @Description:
 */
@Service
public class WordService extends AbstractService<WordEntity> {

    @Autowired
    private WordExplanationRepository wordExplanationRepository;
    @Autowired
    private WordRepository wordRepository;

    public WordInfo findByKeyWord(String keyword) {
        String sql = "SELECT w.keyword,w.book_type,w.uk_voice,w.us_voice,cw.`level`,wb.book_name,cw.book_id "
                + "FROM tb_word w LEFT JOIN tb_customer_word cw ON cw.keyword = w.keyword LEFT JOIN tb_word_book wb ON wb.id = cw.book_id "
                + "WHERE w.keyword = ?";
        WordInfo wordInfo = jdbcTemplate.queryForObject(sql, new Object[]{keyword}, new BeanPropertyRowMapper<>(WordInfo.class));
        if (wordInfo == null) {
            return null;
        }
        //查询正确释义
        List<WordExplanationEntity> explanations = wordExplanationRepository.findByKeyword(keyword);
        Random r = new Random();
        List<String> ee = explanations.stream().map(WordExplanationEntity::getExplanation).collect(Collectors.toList());
        int re = r.ints(0, ee.size()).findFirst().getAsInt();

        //错误释义
        List<WordExplanationEntity> errors = wordExplanationRepository.findByKeywordNot(keyword);
        List<String> eeS = errors.stream().map(WordExplanationEntity::getExplanation).collect(Collectors.toList());
        //移除相同
        eeS.removeAll(ee);
        int rerror = r.ints(0, eeS.size()).findFirst().getAsInt();
        List<String> exps = new ArrayList<>();
        exps.add(ee.get(re));
        exps.add(eeS.get(rerror));
        Collections.shuffle(exps);
        wordInfo.setExplanations(exps);
        return wordInfo;
    }

    public void importWord(String xlsxFilePath) throws Exception {
        InputStream in = new FileInputStream(xlsxFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i=2;i<=sheet.getLastRowNum();i++){
            WordExplanationEntity wordExplanationEntity = new WordExplanationEntity();
            ExcelUtil.Result result = isMergedRegion(sheet, i, 1);
            wordExplanationEntity.setKeyword(result.cellValue);
            ExcelUtil.Result result1 = isMergedRegion(sheet, i, 2);
            wordExplanationEntity.setPhonetic(result1.cellValue);
            ExcelUtil.Result result2 = isMergedRegion(sheet, i, 3);
            String wordType = result2.cellValue.replace(".","");
            WordEntity wordEntity = wordRepository.findByKeyword(wordExplanationEntity.getKeyword());
            if (WordTypeEnum.n.name().equals(wordType)){
                wordExplanationEntity.setWordType(WordTypeEnum.n);
            }else if("vt".equals(wordType)||"vi".equals(wordType)||"v".equals(wordType)){
                wordExplanationEntity.setWordType(WordTypeEnum.v);
            }else if("a".equals(wordType)){
                wordExplanationEntity.setWordType(WordTypeEnum.adj);
            }
            if (wordEntity==null){
                wordEntity = new WordEntity();
                //暂时为1
                wordEntity.setSceneId("1");
                wordEntity.setKeyword(wordExplanationEntity.getKeyword());
                if (StringUtils.isBlank(wordExplanationEntity.getWordType().name())){
                    wordEntity.setBookType(BookTypeEnum.multi);
                }else {
                    wordEntity.setBookType(BookTypeEnum.valueOf(wordExplanationEntity.getWordType().name()));
                }
            }else {
                String bookType = wordEntity.getBookType().name();
                if (!bookType.equals(wordExplanationEntity.getWordType().name())){
                    if ("n".equals(bookType)){
                        if ("v".equals(wordExplanationEntity.getWordType().name())){
                            wordEntity.setBookType(BookTypeEnum.n_v);
                        }else if ("adj".equals(wordExplanationEntity.getWordType().name())){
                            wordEntity.setBookType(BookTypeEnum.n_adj);
                        }
                    } else if ("v".equals(bookType)){
                        if ("adj".equals(wordExplanationEntity.getWordType().name())){
                            wordEntity.setBookType(BookTypeEnum.v_adj);
                        }
                    }else {
                        wordEntity.setBookType(BookTypeEnum.multi);
                    }
                }
            }
            ExcelUtil.Result result3 = isMergedRegion(sheet, i, 4);
            wordExplanationEntity.setExplanation(result3.cellValue);
            save(wordEntity);
            wordExplanationRepository.save(wordExplanationEntity);
        }
    }
}
