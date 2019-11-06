package com.mockst.cracker;

import com.alibaba.fastjson.JSON;
import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.entity.StudyRoundEntity;
import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.entity.em.BookTypeEnum;
import com.mockst.cracker.model.RoundResultInfo;
import com.mockst.cracker.model.SubmitResultInfo;
import com.mockst.cracker.model.WordBook;
import com.mockst.cracker.model.WordInfo;
import com.mockst.cracker.repository.CustomerRepository;
import com.mockst.cracker.repository.WordExplanationRepository;
import com.mockst.cracker.repository.WordRepository;
import com.mockst.cracker.service.CustomerService;
import com.mockst.cracker.service.StudyRoundService;
import com.mockst.cracker.service.WordService;
import com.mockst.cracker.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 20:27
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrackerApplicationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void test(){
        customerService.register("18965835716","123456");
    }

    @Test
    public void customerWordTest(){
        String customerId = "402878816e178a66016e178a73ba0000";
        List<WordBook> list = customerService.findCustomerBooks(customerId, BookTypeEnum.v);
        System.out.println(JSON.toJSONString(list));
        list = customerService.findCustomerBooks(customerId, BookTypeEnum.n);
        System.out.println(JSON.toJSONString(list));
        list = customerService.findCustomerBooks(customerId, BookTypeEnum.n_v);
        System.out.println(JSON.toJSONString(list));
    }

    @Autowired
    private WordService wordService;
    @Test
    public void wordTest(){
        String keyword = "test";
        for (int i = 0; i < 100; i++) {
            WordInfo wordInfo = wordService.findByKeyWord(keyword);
            System.out.println("i:"+i+JSON.toJSONString(wordInfo));
        }

    }

    @Autowired
    private StudyRoundService roundService;

    @Test
    public void roundTest(){
        String customerId = "402878816e178a66016e178a73ba0000";
        String bookId = "402878816e178a66016e178a74620001";
        //创建轮次
        StudyRoundEntity roundEntity = roundService.createRound(bookId,customerId);
        if (roundEntity!=null){
            while(true){
                WordInfo wordInfo = roundService.createWord(roundEntity.getId());
                if (wordInfo!=null){
                    //获取新单词
                    System.out.println("获取新单词");
                    String explanation = wordInfo.getExplanation1();
                    SubmitResultInfo resultInfo = roundService.submitWord(wordInfo.getResultId(),explanation);
                    System.out.println("keyword:"+wordInfo.getKeyword());
                    System.out.println("isRight:"+resultInfo.isRight());
                    System.out.println("isHaveNext:"+resultInfo.isHaveNext());
                    if(!resultInfo.isHaveNext()){
                        break;
                    }
                }else {
                    break;
                }
            }
            RoundResultInfo resultInfo = roundService.stopRound(roundEntity.getId());
            System.out.println("resultInfo:"+JSON.toJSONString(resultInfo));
            System.out.println("完成");
        }

    }

}
