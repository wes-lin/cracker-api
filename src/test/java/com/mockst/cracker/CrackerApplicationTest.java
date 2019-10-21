package com.mockst.cracker;

import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.repository.CustomerRepository;
import com.mockst.cracker.repository.WordRepository;
import com.mockst.cracker.service.CustomerService;
import com.mockst.cracker.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private WordRepository wordRepository;

    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void test(){
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setOpenId("11");
        customerEntity.setNickname("张三");
        customerEntity.setPhone("18965835716");
        customerService.save(customerEntity);
        redisUtil.set("aaa","12331");
    }

}
